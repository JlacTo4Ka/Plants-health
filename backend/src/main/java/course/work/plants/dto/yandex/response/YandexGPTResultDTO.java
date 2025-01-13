package course.work.plants.dto.yandex.response;

import course.work.plants.dto.yandex.GenerationMessage;

public record YandexGPTResultDTO(
        GenerationMessage message,
        String status
){ }
