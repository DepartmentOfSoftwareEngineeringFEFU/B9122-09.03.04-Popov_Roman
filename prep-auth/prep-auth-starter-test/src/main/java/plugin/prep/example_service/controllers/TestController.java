package plugin.prep.example_service.controllers;

import lombok.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/open")
    @PreAuthorize("permitAll()")
    public void getOpen() {

    }

    @GetMapping("/secure")
    public void getSecure() {

    }

}
