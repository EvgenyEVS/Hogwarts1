package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.model.Avatar;
import org.skypro.homeworks.hogwarts1.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface AvatarService {
    void uploadAvatar(Long studentID, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(Long studentId);

}
