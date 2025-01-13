package course.work.plants.service;

import course.work.plants.model.DiseaseResponseDTO;
import course.work.plants.model.HistoryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PlantService {

    DiseaseResponseDTO identifyDisease(MultipartFile img);

    HistoryResponseDTO getHistory();
}
