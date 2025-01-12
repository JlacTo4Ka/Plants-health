package course.work.plants.dto.yandex.response;


import java.util.List;

public record YandexGenerationResponseDTO(
        YandexGenerationResultDTO result
) {
    public record YandexGenerationResultDTO(
            List<YandexGPTResultDTO> alternatives,
            YandexUsageSettingsDTO usage,
            String modelVersion
    ) {
    }
}