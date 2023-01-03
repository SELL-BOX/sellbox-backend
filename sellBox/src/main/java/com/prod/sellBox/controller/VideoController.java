package com.prod.sellBox.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.prod.sellBox.domain.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@RestController
@Slf4j
public class VideoController {

    private static final Gson gson = new GsonBuilder().create();

    private final ConcurrentHashMap<String, UserSession> viewers = new ConcurrentHashMap<>();

    @Autowired
    private KurentoClient kurento;
    private MediaPipeline pipeline;
    private UserSession presenterUserSession;

    private final SimpMessagingTemplate so;

    @MessageMapping("stream/room/{roomId}")
    public synchronized void videoStream(String msg, StompHeaderAccessor ha, @DestinationVariable String roomId) throws IOException {
        JsonObject jsonMessage = gson.fromJson(msg, JsonObject.class);
        log.info("Incoming message from session '{}': {}", ha.getSessionId(), jsonMessage);
        log.info("request roomID : {}", roomId);

        switch (jsonMessage.get("id").getAsString()) {
            case "presenter":
                try {
                    presenter(ha, jsonMessage, roomId);
                } catch (Throwable t) {
                    handleErrorResponse(t, ha, "presenterResponse", roomId);
                }
                break;
            case "viewer":
                try {
                    viewer(ha, jsonMessage, roomId);
                } catch (Throwable t) {
                    handleErrorResponse(t, ha, "viewerResponse", roomId);
                }
                break;
            case "onIceCandidate": {
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                UserSession user = null;
                if (presenterUserSession != null) {
                    if (presenterUserSession.getSessionId() == ha.getSessionId()) {
                        user = presenterUserSession;
                    } else {
                        user = viewers.get(ha.getSessionId());
                    }
                }
                if (user != null) {
                    IceCandidate cand =
                            new IceCandidate(candidate.get("candidate").getAsString(), candidate.get("sdpMid")
                                    .getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand);
                }
                break;
            }
            case "stop":
                stop(ha, roomId);
                break;
            default:
                break;
        }
    }

    private void handleErrorResponse(Throwable throwable, StompHeaderAccessor ha, String responseId, String roomId)
            throws IOException {
        String destination = "/video/room/" + roomId;
        stop(ha, roomId);
        log.error(throwable.getMessage(), throwable);
        JsonObject response = new JsonObject();
        response.addProperty("id", responseId);
        response.addProperty("response", "rejected");
        response.addProperty("message", throwable.getMessage());
        so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));

    }

    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    private synchronized void presenter(final StompHeaderAccessor ha, JsonObject jsonMessage, String roomId) {
        String destination = "/video/room/" + roomId;
        log.info("videoStream presenter destination : {}", destination);

        if (presenterUserSession == null) {
            presenterUserSession = new UserSession(ha.getSessionId());

            pipeline = kurento.createMediaPipeline();
            presenterUserSession.setWebRtcEndpoint(new WebRtcEndpoint.Builder(pipeline).build());

            WebRtcEndpoint presenterWebRtc = presenterUserSession.getWebRtcEndpoint();

            presenterWebRtc.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

                @Override
                public void onEvent(IceCandidateFoundEvent event) {
                    JsonObject response = new JsonObject();
                    response.addProperty("id", "iceCandidate");
                    response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                    try {
                        synchronized (so) {
                            so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
                        }
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }
                }
            });

            String sdpOffer = jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString();
            String sdpAnswer = presenterWebRtc.processOffer(sdpOffer);

            JsonObject response = new JsonObject();
            response.addProperty("id", "presenterResponse");
            response.addProperty("response", "accepted");
            response.addProperty("sdpAnswer", sdpAnswer);
            synchronized (so) {
                so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
            }

            presenterWebRtc.gatherCandidates();
        } else {
            JsonObject response = new JsonObject();
            response.addProperty("id", "presenterResponse");
            response.addProperty("response", "rejected");
            response.addProperty("message",
                    "Another user is currently acting as sender. Try again later ...");
            synchronized (so) {
                so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
            }
        }


    }

    private synchronized void viewer(final StompHeaderAccessor ha, JsonObject jsonMessage, String roomId) {
        String destination = "/video/room/" + roomId;
        log.info("videoStream viewer destination : {}", destination);

        if (presenterUserSession == null || presenterUserSession.getWebRtcEndpoint() == null) {
            JsonObject response = new JsonObject();
            response.addProperty("id", "viewerResponse");
            response.addProperty("response", "rejected");
            response.addProperty("message",
                    "No active sender now. Become sender or . Try again later ...");
            so.convertAndSendToUser(ha.getSessionId(),destination, response.toString());
        } else {
            if (viewers.containsKey(ha.getSessionId())) {
                JsonObject response = new JsonObject();
                response.addProperty("id", "viewerResponse");
                response.addProperty("response", "rejected");
                response.addProperty("message", "You are already viewing in this session. "
                        + "Use a different browser to add additional viewers.");
                so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));

                return;
            }
            UserSession viewer = new UserSession(ha.getSessionId());
            viewers.put(ha.getSessionId(), viewer);

            WebRtcEndpoint nextWebRtc = new WebRtcEndpoint.Builder(pipeline).build();

            nextWebRtc.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

                @Override
                public void onEvent(IceCandidateFoundEvent event) {
                    JsonObject response = new JsonObject();
                    response.addProperty("id", "iceCandidate");
                    response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                    try {
                        synchronized (so) {
                            so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
                        }
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }
                }
            });

            viewer.setWebRtcEndpoint(nextWebRtc);
            presenterUserSession.getWebRtcEndpoint().connect(nextWebRtc);
            String sdpOffer = jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString();
            String sdpAnswer = nextWebRtc.processOffer(sdpOffer);

            JsonObject response = new JsonObject();
            response.addProperty("id", "viewerResponse");
            response.addProperty("response", "accepted");
            response.addProperty("sdpAnswer", sdpAnswer.toString());

            synchronized (so) {
                so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
            }
            nextWebRtc.gatherCandidates();
        }
    }


    private synchronized void stop(StompHeaderAccessor ha, String roomId) throws IOException {
        String destination = "/video/room/" + roomId;
        log.info("videoStream stop destination : {}", destination);

        String sessionId = ha.getSessionId();
        if (presenterUserSession != null && presenterUserSession.getSessionId().equals(sessionId)) {
            for (UserSession viewer : viewers.values()) {
                JsonObject response = new JsonObject();
                response.addProperty("id", "stopCommunication");
                so.convertAndSendToUser(ha.getSessionId(),destination, response.toString(), createHeaders(ha.getSessionId()));
            }

            log.info("Releasing media pipeline");
            if (pipeline != null) {
                pipeline.release();
            }
            pipeline = null;
            presenterUserSession = null;
        } else if (viewers.containsKey(sessionId)) {
            if (viewers.get(sessionId).getWebRtcEndpoint() != null) {
                viewers.get(sessionId).getWebRtcEndpoint().release();
            }
            viewers.remove(sessionId);
        }
    }
}
