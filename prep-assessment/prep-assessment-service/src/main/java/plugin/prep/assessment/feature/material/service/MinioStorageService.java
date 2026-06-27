package plugin.prep.assessment.feature.material.service;

import java.io.*;

import io.minio.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Getter
    @Value("${minio.bucket}")
    private String bucket;

    public void upload(String objectKey, MultipartFile file) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            throw new PrepException("Ошибка сохранения файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public InputStream download(String objectKey) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build()
            );
        } catch (Exception e) {
            throw new PrepException("Ошибка получения файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public StatObjectResponse stat(String objectKey) {
        try {
            return minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build()
            );
        } catch (Exception e) {
            throw new PrepException("Файл не найден", HttpStatus.NOT_FOUND);
        }
    }

    public void delete(String objectKey) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build()
            );
        } catch (Exception e) {
            throw new PrepException("Ошибка удаления файла", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
