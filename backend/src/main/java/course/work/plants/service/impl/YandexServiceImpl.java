package course.work.plants.service.impl;

import course.work.plants.config.client.YandexClient;
import course.work.plants.config.properties.YandexGPTSettings;
import course.work.plants.dto.yandex.GenerationMessage;
import course.work.plants.dto.yandex.request.YandexGenerationRequestDTO;
import course.work.plants.dto.yandex.request.YandexGenerationRequestDTO.YandexCompletionOptions;
import course.work.plants.dto.yandex.response.YandexGenerationResponseDTO;
import course.work.plants.service.YandexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexServiceImpl implements YandexService {

    private final YandexClient yandexClient;
    private final YandexGPTSettings yandexGPTSettings;
    private static final String SYSTEM_MESSAGE = "Напиши причину и лечение болезни растения";
    private static final String PROMT_PATTERN = "Болезнь %s";
    private static final String USER_ROLE = "user";
    private static final String SYSTEM_ROLE = "system";

    @Override
    public Optional<YandexGenerationResponseDTO> getTreatmentPlan(String disease) {
        String pattern = yandexGPTSettings.getModelUri();
        String modelUri = pattern.formatted(yandexGPTSettings.getFolderId());
        String userMessage = PROMT_PATTERN.formatted(disease);
        GenerationMessage systemGenerationMessage = new GenerationMessage(
                SYSTEM_ROLE,
                SYSTEM_MESSAGE
        );
        GenerationMessage userGenerationMessage = new GenerationMessage(
                USER_ROLE,
                userMessage
        );
        YandexCompletionOptions completionOptions = YandexCompletionOptions.builder()
                .stream(yandexGPTSettings.getStream())
                .temperature(yandexGPTSettings.getTemperature())
                .maxTokens(yandexGPTSettings.getMaxTokens())
                .build();

        YandexGenerationRequestDTO yandexGenerationRequestDTO = YandexGenerationRequestDTO.builder()
                .completionOptions(completionOptions)
                .modelUri(modelUri)
                .messages(
                        List.of(systemGenerationMessage, userGenerationMessage)
                ).build();

        try {
            log.info("Call getTreatmentPlan()");
            return Optional.ofNullable(
                    yandexClient.generateAnswer(yandexGenerationRequestDTO)
            );
        } catch (Exception exception) {
            log.warn("Catch error while processing getTreatmentPlan()", exception);
            return Optional.empty();
        }
    }
}
