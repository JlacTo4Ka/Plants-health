package course.work.plants.config.client;

import course.work.plants.dto.ml.MLDiseaseResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "mlFeignClient")
public interface MLFeignClient {

    @PostMapping(value = "/predict",
            produces = "application/json",
            consumes = "multipart/form-data"
    )
    MLDiseaseResponseDTO identifyDiseaseByImage(@RequestBody MultipartFile multipartFile);
}
