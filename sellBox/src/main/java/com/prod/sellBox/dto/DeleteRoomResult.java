package com.prod.sellBox.dto;

import lombok.Getter;

@Getter
public enum DeleteRoomResult {
    FAIL(0, "삭제 실패"),
    SUCCESS(1, "삭제 성공");

    private final int code;
    private final String msg;

    DeleteRoomResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
