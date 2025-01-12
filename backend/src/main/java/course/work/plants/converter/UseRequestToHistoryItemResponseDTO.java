package course.work.plants.converter;

import course.work.plants.model.HistoryItemDTO;
import course.work.plants.model.UserRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UseRequestToHistoryItemResponseDTO
        implements Converter<UserRequest, HistoryItemDTO> {

    @Override
    public HistoryItemDTO convert(UserRequest source) {
        return new HistoryItemDTO()
                .disease(source.getDisease())
                .imageUrl(source.getImageUrl())
                .description(source.getDescription());
    }
}
