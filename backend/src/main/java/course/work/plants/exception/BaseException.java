package course.work.plants.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;
    private final String details;

    public BaseException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
        this.details = null;
    }

    public BaseException(ErrorCodeEnum errorCodeEnum, Exception e) {
        super(e);
        this.errorCodeEnum = errorCodeEnum;
        this.details = null;
    }

    public BaseException(ErrorCodeEnum errorCodeEnum, String details) {
        this.errorCodeEnum = errorCodeEnum;
        this.details = details;
    }
}
