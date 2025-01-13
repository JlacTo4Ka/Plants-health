package course.work.plants.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    EXTERNAL_SERVICE_ERROR("01", HttpStatus.BAD_GATEWAY, "Ошибка от стороннего сервиса"),
    INTERNAL_SERVICE_ERROR("02", HttpStatus.INTERNAL_SERVER_ERROR, "Неизвестная ошибка"),
    NOT_FOUND_ERROR("03", HttpStatus.NOT_FOUND, "Не найдено"),
    VALIDATION_ERROR("04", HttpStatus.BAD_REQUEST, "Ошибка валидации");

    private final String code;
    private final HttpStatusCode status;
    private final String message;
}
