package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public Faculty editFaculty(long id, Faculty faculty) {
        if (!facultyRepository.existsById(id)) {
            return null;
        }
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

}
