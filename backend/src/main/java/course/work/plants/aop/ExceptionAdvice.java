package course.work.plants.aop;

import course.work.plants.exception.BaseException;
import course.work.plants.exception.ErrorCodeEnum;
import course.work.plants.model.ErrorPayloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        if (ErrorCodeEnum.NOT_FOUND_ERROR.getStatus().equals(statusCode)) {
            return ResponseEntity
                    .status(ErrorCodeEnum.NOT_FOUND_ERROR.getStatus())
                    .body(buildNotFoundError());
        }
        if (ErrorCodeEnum.VALIDATION_ERROR.getStatus().equals(statusCode)) {
            return ResponseEntity
                    .status(ErrorCodeEnum.VALIDATION_ERROR.getStatus())
                    .body(buildValidationError());
        }

        log.warn("Unknown Internal Exception, status: {}", statusCode, ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorPayloadDTO> catchBaseException(BaseException exception) {
        ErrorCodeEnum errorCodeEnum = exception.getErrorCodeEnum();

        return ResponseEntity
                .status(errorCodeEnum.getStatus())
                .body(buildErrorPayloadResponse(errorCodeEnum, exception.getDetails()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorPayloadDTO> catchException(Exception exception) {
        log.error("Unknown exception", exception);
        return ResponseEntity
                .status(ErrorCodeEnum.INTERNAL_SERVICE_ERROR.getStatus())
                .body(buildInternalServiceError());
    }

    private ErrorPayloadDTO buildNotFoundError() {
        return buildErrorPayloadResponse(ErrorCodeEnum.NOT_FOUND_ERROR, null);
    }

    private ErrorPayloadDTO buildValidationError() {
        return buildErrorPayloadResponse(ErrorCodeEnum.VALIDATION_ERROR, null);
    }

    private ErrorPayloadDTO buildInternalServiceError() {
        return buildErrorPayloadResponse(ErrorCodeEnum.INTERNAL_SERVICE_ERROR, null);
    }

    private ErrorPayloadDTO buildErrorPayloadResponse(
            ErrorCodeEnum errorCodeEnum,
            @Nullable String details
    ) {
        return new ErrorPayloadDTO()
                .code(errorCodeEnum.getCode())
                .message(errorCodeEnum.getMessage())
                .details(details);
    }
}
