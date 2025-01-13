package course.work.plants.config.client;

import course.work.plants.dto.yandex.request.YandexGenerationRequestDTO;
import course.work.plants.dto.yandex.response.YandexGenerationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "yandexFeignClient")
public interface YandexClient {

    @PostMapping(
            value = "/foundationModels/v1/completion",
            produces = "application/json",
            consumes = "application/json"
    )
    YandexGenerationResponseDTO generateAnswer(@RequestBody YandexGenerationRequestDTO yandexGenerationRequestDTO);
}
