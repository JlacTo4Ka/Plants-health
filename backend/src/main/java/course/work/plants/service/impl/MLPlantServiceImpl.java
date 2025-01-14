package course.work.plants.service.impl;

import course.work.plants.config.client.MLFeignClient;
import course.work.plants.config.properties.MLFeignProperties;
import course.work.plants.dto.ml.MLDiseaseResponseDTO;
import course.work.plants.exception.BaseException;
import course.work.plants.exception.ErrorCodeEnum;
import course.work.plants.service.MLPlantService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MLPlantServiceImpl implements MLPlantService {

    private final MLFeignClient mlFeignClient;
    private final MLFeignProperties properties;

    @Override
    public String identifyDisease(MultipartFile multipartFile) {
        if (properties.isMock()) {
            return "Cercospora";
        }
        Optional<MLDiseaseResponseDTO> oDisease = Optional.empty();
        try {
            log.info("Call identifyDisease()");
            oDisease = Optional.ofNullable(
                    mlFeignClient.identifyDiseaseByImage(multipartFile)
            );
        } catch (Exception exception) {
            log.warn("Catch error while processing identifyDisease()", exception);
            throwBaseException(exception);
        }
        return oDisease
                .map(MLDiseaseResponseDTO::disease)
                .orElseThrow(() -> new BaseException(ErrorCodeEnum.NOT_FOUND_ERROR));
    }

    private void throwBaseException(Exception exception) {
        if (exception instanceof FeignException feignException
                && feignException.status() == 404) {
            throw new BaseException(ErrorCodeEnum.NOT_FOUND_ERROR, exception);
        }

        throw new BaseException(ErrorCodeEnum.EXTERNAL_SERVICE_ERROR, exception);
    }
}
