package course.work.plants.service;

import java.util.Optional;

public interface S3Service {

    Optional<String> uploadImage(byte[] inputStream);
}
