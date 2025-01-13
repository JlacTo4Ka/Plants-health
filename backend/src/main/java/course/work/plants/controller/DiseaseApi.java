package course.work.plants.controller;

import course.work.plants.api.DiseasesApi;
import course.work.plants.model.DiseaseResponseDTO;
import course.work.plants.model.HistoryResponseDTO;
import course.work.plants.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class DiseaseApi implements DiseasesApi {

    private final PlantService plantService;

    @Override
    public ResponseEntity<HistoryResponseDTO> getHistory() {
        return ResponseEntity.ok(
                plantService.getHistory()
        );
    }

    @Override
    public ResponseEntity<DiseaseResponseDTO> resolveDisease(MultipartFile img) {
        return ResponseEntity.ok(
                plantService.identifyDisease(img)
        );
    }
}
