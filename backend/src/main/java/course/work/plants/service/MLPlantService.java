package course.work.plants.service;

import org.springframework.web.multipart.MultipartFile;

public interface MLPlantService {

    String identifyDisease(MultipartFile multipartFile);
}
