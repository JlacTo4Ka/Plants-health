package course.work.plants.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ml-plants")
public class MLFeignProperties {

    private boolean isMock;
}
