package course.work.plants.config.properties;

import com.amazonaws.auth.AWSCredentials;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "s3")
public class S3Properties implements AWSCredentials {

    private String accessKey;
    private String keyId;
    private String bucketName;
    private String endpoint;
    private String region;

    @Override
    public String getAWSAccessKeyId() {
        return keyId;
    }

    @Override
    public String getAWSSecretKey() {
        return accessKey;
    }
}
