package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.dto.FacultyUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(FacultyCreateDto facultyCreateDto) {

        logger.info("Was invoked method for add faculty");
        Faculty faculty = new Faculty();
        logger.debug("Creating faculty with name: {}, color: {}",
                facultyCreateDto.name(), facultyCreateDto.color());
        faculty.setName(facultyCreateDto.name());
        faculty.setColor(facultyCreateDto.color());

        Faculty savedFaculty = facultyRepository.save(faculty);
        logger.debug("Faculty created successfully with ID: {}", savedFaculty.getId());
        return savedFaculty;
    }

    public Faculty findFaculty(long id) {

        logger.info("Was invoked method for find faculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(long id, FacultyUpdateDto facultyUpdateDto) {

        logger.info("Was invoked method for edit faculty");
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);

        if (optionalFaculty.isEmpty()) {
            logger.warn("Faculty with ID = {} is not found", id);
            return null;
        }

        Faculty faculty = optionalFaculty.get();

        faculty.setName(facultyUpdateDto.name());
        faculty.setColor(facultyUpdateDto.color());

        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }


    public List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String search) {
        logger.info("Was invoked method for find by name contains or color contains faculty");
        if (search == null || search.isBlank()) {
            return facultyRepository.findAll();
        }
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(search, search);
    }
}
