package course.work.plants.dto.yandex.request;

import course.work.plants.dto.yandex.GenerationMessage;
import lombok.Builder;

import java.util.List;

@Builder
public record YandexGenerationRequestDTO (
        String modelUri,
        YandexCompletionOptions completionOptions,
        List<GenerationMessage> messages


) {
    @Builder
    public record YandexCompletionOptions(
       boolean stream,
       double temperature,
       String maxTokens
    ) {}
}
