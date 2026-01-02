package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.model.Avatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class AvatarServiceImpl implements AvatarService {
    @Override
    public void uploadAvatar(Long studentID, MultipartFile avatarFile) throws IOException {

    }

    @Override
    public Avatar findAvatar(Long studentId) {
        return null;
    }
}
