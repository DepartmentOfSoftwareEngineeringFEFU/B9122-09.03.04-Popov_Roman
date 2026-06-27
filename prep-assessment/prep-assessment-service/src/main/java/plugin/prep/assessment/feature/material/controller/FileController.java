package plugin.prep.assessment.feature.material.controller;

import lombok.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.material.api.*;
import plugin.prep.assessment.feature.material.dto.file.*;
import plugin.prep.assessment.feature.material.service.*;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final MaterialService materialService;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public FileCreateResponse createFile(FileCreateRequest request) {
        return null;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<InputStreamResource> getFile(Long id) {
        return materialService.getMaterialFile(id);
    }

}
