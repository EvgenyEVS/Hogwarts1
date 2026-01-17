package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.dto.FacultyUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.hibernate.criterion.Projections.id;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(FacultyCreateDto facultyCreateDto) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyCreateDto.name());
        faculty.setColor(facultyCreateDto.color());

        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(long id, FacultyUpdateDto facultyUpdateDto) {

        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);

        if (optionalFaculty.isEmpty()) {
            return null;
        }

        Faculty faculty = optionalFaculty.get();

        faculty.setName(facultyUpdateDto.name());
        faculty.setColor(facultyUpdateDto.color());

        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        facultyRepository.deleteById(id);
    }


    public List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String search) {
        if (search == null || search.isBlank()) {
            return facultyRepository.findAll();
        }
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(search, search);
    }
}
