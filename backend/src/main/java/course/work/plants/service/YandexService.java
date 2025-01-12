package course.work.plants.service;

import course.work.plants.dto.yandex.response.YandexGenerationResponseDTO;

import java.util.Optional;

public interface YandexService {

    Optional<YandexGenerationResponseDTO> getTreatmentPlan(String disease);
}
