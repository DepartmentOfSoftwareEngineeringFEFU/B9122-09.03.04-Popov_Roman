package plugin.prep.assessment.feature.material.api;

import io.swagger.v3.oas.annotations.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.dto.file.*;

public interface FileApi {

    @PostMapping("/files/")
    @Operation(description = "Добавление файла")
    FileCreateResponse createFile(FileCreateRequest request);

    @GetMapping("/files/{id}")
    @Operation(description = "Получение файла материала")
    ResponseEntity<InputStreamResource> getFile(@PathVariable Long id);

}
