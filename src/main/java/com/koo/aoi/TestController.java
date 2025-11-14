package com.koo.aoi;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(HttpServletResponse response) throws IOException {
        return "test succeed";
    }
}
