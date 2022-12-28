package spring.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spring.board.web.dto.ErrorResponse;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ApiExceptions e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error(errorCode.getDetail());
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(IOException e) {
        ErrorCode errorCode = ErrorCode.FILE_NOT_FOUND;
        log.error(errorCode.getDetail());
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getDetail())
                .build();
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
