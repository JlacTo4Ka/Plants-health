package course.work.plants.dto.yandex.response;

public record YandexUsageSettingsDTO(
        String inputTextTokens,
        String completionTokens,
        String totalTokens
) { }
