package ru.hits.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.core.domain.dto.user.FCMDTO;
import ru.hits.core.service.FCMService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FCMController {

    private final FCMService fcmService;

    @PostMapping
    private void saveFcmToken(
            @RequestBody FCMDTO fcmdto
    ) {
        fcmService.saveFCMToken(fcmdto.getUserId(), fcmdto.getIsManager(), fcmdto.getFcmToken());
    }

}
