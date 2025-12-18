package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private long count = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(count++);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        if (!facultyMap.containsKey(id)) {
            return null;
        }
        return facultyMap.get(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        if (!facultyMap.containsKey(id)) {
            return null;
        }
        faculty.setId(id);
        facultyMap.put(id, faculty);
        return faculty;
    }

    public void removeFaculty(long id) {
        facultyMap.remove(id);
    }

    public Collection<Faculty> getAllFaculty () {
        return facultyMap.values();
    }

}
