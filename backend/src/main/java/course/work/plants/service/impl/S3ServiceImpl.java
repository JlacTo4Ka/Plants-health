package course.work.plants.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import course.work.plants.config.properties.S3Properties;
import course.work.plants.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client s3Client;
    private final String bucket;

    public S3ServiceImpl(AmazonS3 s3Client, S3Properties s3Properties) {
        this.s3Client = (AmazonS3Client) s3Client;
        this.bucket = s3Properties.getBucketName();
    }

    @Override
    public Optional<String> uploadImage(byte[] bytes) {
        try {
            String uuid = UUID.randomUUID() + ".png";
            log.info("Loading image with uuid: {}", uuid);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            s3Client.putObject(bucket, uuid, inputStream, objectMetadata);
            return Optional.ofNullable(s3Client.getResourceUrl(bucket, uuid));
        } catch (Exception e) {
            log.warn("Catch exception while uploading image to s3", e);
            return Optional.empty();
        }
    }
}
