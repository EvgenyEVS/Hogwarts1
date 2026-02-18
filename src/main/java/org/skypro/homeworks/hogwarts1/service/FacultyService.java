package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.dto.FacultyUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

    public String maxLengthFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("Не найдено самое длинное название факультета");
    }

    public long sumParallelStream() {
        //Stream работает, но int переполняется, выдает некорректный результат.

//        int sum = Stream
//                .iterate(1, a -> a + 1)
//                .limit(1_000_000)
//                .parallel()
//                .reduce(0, (a, b) -> a + b);
//        return sum;

        // Перешел на Long и rangeClosed. Сократил лишние шаги, ответ стал быстрее всего на 5%
        long sum = LongStream.rangeClosed(1, 1000000)
                .parallel()
                .sum();
        return sum;
    }
}
