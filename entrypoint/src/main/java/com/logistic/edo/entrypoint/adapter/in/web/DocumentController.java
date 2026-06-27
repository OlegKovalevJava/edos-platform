package com.logistic.edo.entrypoint.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    @GetMapping("/ping")
    public String ping() {
        return "OK. Document service is running!";
    }
}
