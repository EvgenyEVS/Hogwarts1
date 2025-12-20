package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.model.Student;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private long count = 0;

    public Student addStudent(Student student) {
        student.setId(++count);
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student findStudent(long id) {
        if (!studentMap.containsKey(id)) {
            return null;
        }
        return studentMap.get(id);
    }

    public Student editStudent(long id, Student student) {
        if (!studentMap.containsKey(id)) {
            return null;
        }
        student.setId(id);
        studentMap.put(id, student);
        return student;
    }

    public void removeStudent(long id) {
        studentMap.remove(id);
    }

    public Collection<Student> getAllStudent () {
        return studentMap.values();
    }


}
