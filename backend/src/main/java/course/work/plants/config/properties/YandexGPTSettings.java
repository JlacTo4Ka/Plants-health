package course.work.plants.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "yandex.gpt")
public class YandexGPTSettings {

    private Boolean stream;
    private Double temperature;
    private String maxTokens;
    private String folderId;
    private String modelUri;
}
