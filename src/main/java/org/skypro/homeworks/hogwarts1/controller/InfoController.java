package org.skypro.homeworks.hogwarts1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${custom.message: Default Message}")
    private String message;

    @GetMapping("/port")
    public String getPort() {
        return String.format(
                "Active profile: %s | Port: %s | Message: %s",
                activeProfile, serverPort, message
        );
    }
}
