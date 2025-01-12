package course.work.plants.service.impl;

import course.work.plants.dto.yandex.GenerationMessage;
import course.work.plants.dto.yandex.response.YandexGPTResultDTO;
import course.work.plants.dto.yandex.response.YandexGenerationResponseDTO;
import course.work.plants.exception.BaseException;
import course.work.plants.exception.ErrorCodeEnum;
import course.work.plants.model.*;
import course.work.plants.repository.UserRepository;
import course.work.plants.repository.UserRequestRepository;
import course.work.plants.service.MLPlantService;
import course.work.plants.service.PlantService;
import course.work.plants.service.S3Service;
import course.work.plants.service.YandexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

    private final MLPlantService mlPlantService;
    private final YandexService yandexService;
    private final UserRepository userRepository;
    private final UserRequestRepository userRequestRepository;
    private final AsyncService asyncService;
    private final S3Service s3Service;
    private final Converter<UserRequest, HistoryItemDTO> userRequestToHistoryItem;
    private static final List<String> SUPPORTED_IMAGE_TYPE = List.of(
            ContentType.IMAGE_JPEG.getMimeType(),
            ContentType.IMAGE_PNG.getMimeType(),
            ContentType.IMAGE_GIF.getMimeType()
    );

    @Override
    public DiseaseResponseDTO identifyDisease(MultipartFile image) {
        validateImageType(image);
        byte[] bytes;
        try {
            bytes = image.getBytes();
        } catch (IOException e) {
            log.error("Can't get bytes");
            throw new BaseException(ErrorCodeEnum.INTERNAL_SERVICE_ERROR);
        }

        String disease = mlPlantService.identifyDisease(image);
        DiseaseResponseDTO diseaseResponseDTO = new DiseaseResponseDTO()
                .disease(disease);
        Optional<YandexGenerationResponseDTO> oYandexGenerationResponseDTO = yandexService.getTreatmentPlan(disease);
        oYandexGenerationResponseDTO
                .map(YandexGenerationResponseDTO::result)
                .map(YandexGenerationResponseDTO.YandexGenerationResultDTO::alternatives)
                .filter(list -> !list.isEmpty())
                .map(List::stream)
                .map(stream -> stream.map(YandexGPTResultDTO::message))
                .map(stream -> stream.map(GenerationMessage::text))
                .map(this::collectToString)
                .ifPresent(diseaseResponseDTO::setDescription);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        asyncService.execute(
                () -> saveRequest(bytes, authentication, disease, diseaseResponseDTO.getDescription())
        );

        return diseaseResponseDTO;
    }

    @Override
    public HistoryResponseDTO getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel userModel = userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        List<HistoryItemDTO> userRequests = userRequestRepository.findAllByUserModel(userModel).stream()
                .map(userRequestToHistoryItem::convert)
                .toList();

        return new HistoryResponseDTO()
                .items(userRequests);
    }

    protected Void saveRequest(
            byte[] bytes,
            Authentication authentication,
            String disease,
            @Nullable String description
    ) {
        if (isNotAuthenticatedAndNotAnonymous(authentication)) {
            return null;
        }

        String login = authentication.getName();
        UserModel userModel = userRepository.findByLogin(login)
                .orElseThrow(() -> new BaseException(ErrorCodeEnum.INTERNAL_SERVICE_ERROR));

        Optional<String> oUrl = s3Service.uploadImage(bytes);

        UserRequest userRequest = UserRequest.builder()
                .userModel(userModel)
                .disease(disease)
                .description(description)
                .imageUrl(oUrl.orElse(null))
                .createdAt(LocalDateTime.now())
                .build();
        userRequestRepository.save(userRequest);
        return null;
    }

    private boolean isNotAuthenticatedAndNotAnonymous(Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser");
    }

    private String collectToString(Stream<String> stream) {
        return stream.collect(Collectors.joining("\n"));
    }

    private void validateImageType(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BaseException(ErrorCodeEnum.VALIDATION_ERROR, "Файл отсутствует или пустой");
        }

        String contentType = image.getContentType();
        if (!SUPPORTED_IMAGE_TYPE.contains(contentType)) {
            throw new BaseException(ErrorCodeEnum.VALIDATION_ERROR,
                    "Файл не является поддерживаемым изображением. Разрешены только JPEG, PNG и GIF."
            );
        }
    }
}
