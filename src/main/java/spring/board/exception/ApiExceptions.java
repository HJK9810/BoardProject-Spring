package spring.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiExceptions extends RuntimeException{

    private final ErrorCode errorCode;
}
