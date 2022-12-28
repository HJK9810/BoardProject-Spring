package spring.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // user error
    MEMBER_NOT_FOUND(NOT_FOUND, "등록되지 않은 사용자입니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),

    // token error
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    // token validate error
    WRONG_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_TOKEN_NOT_WORK(UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),

    // refresh token error
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "Refresh Token 이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다."),

    // file error
    FILE_NOT_SAVED(BAD_REQUEST, "파일이 저장되지 않았습니다."),
    FILE_NOT_FOUND(BAD_REQUEST, "파일을 찾지 못했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
