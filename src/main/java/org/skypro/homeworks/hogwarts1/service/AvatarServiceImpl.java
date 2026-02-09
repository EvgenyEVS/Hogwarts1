package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.model.Avatar;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.AvatarRepository;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;


@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void uploadAvatar(Long studentID, MultipartFile avatarFile) throws IOException {

        logger.info("Was invoked method for upload avatar for student ID {}", studentID);

        Student student = studentRepository.getReferenceById(studentID);

        logger.debug("Creating file path and directory");
        Path filePath = Path.of(avatarsDir, student + "."
                + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        logger.debug("Stream was opened");
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentID);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateDataForDB(filePath));
        avatarRepository.save(avatar);
        logger.debug("Stream was closed");
    }


    private byte[] generateDataForDB(Path filePath) throws IOException {
        logger.info("Was invoked method for generate data from DB avatar");
        logger.debug("Generating preview for file: {}", filePath);
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            logger.debug("Reading from file: {}", filePath);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                logger.error("Cannot read image from {}", filePath);
                throw new IOException("Cannot read from {}" + filePath);
            }

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            logger.debug("Image preview was generated");
            return baos.toByteArray();
        }
    }


    @Override
    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtensions(String fileName) {

        logger.debug("Extracting extensions from {}", fileName);
        if (fileName == null) {
            logger.error("Filename is null");
            throw new IllegalArgumentException("Filename cannot be null");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    @Override
    public Page<Avatar> getAllAvatars(int page, int size) {

        logger.info("Was invoked method for get all avatars");
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest);
    }

}
