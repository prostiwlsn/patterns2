package ru.hits.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.entity.FCMEntity;
import ru.hits.core.repository.FCMRepository;
import ru.hits.core.service.FCMService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {

    private final FCMRepository fcmRepository;

    @Override
    public void saveFCMToken(UUID userId, Boolean isManager, String FCMToken) {
        var fcmToken = fcmRepository.findById(userId).orElse(null);

        if (fcmToken != null) {
            fcmToken.setFcm(FCMToken);
            fcmRepository.save(fcmToken);
            return;
        }

        fcmRepository.save(new FCMEntity(userId, isManager, FCMToken));
    }

    @Override
    public FCMEntity getFCMToken(UUID userId) {
        return fcmRepository.findById(userId).orElse(null);
    }

    @Override
    public List<String> getEmployeeTokens() {
        return fcmRepository.findAllByIsManager(true)
                .stream().map(FCMEntity::getFcm).collect(Collectors.toList());
    }
}
