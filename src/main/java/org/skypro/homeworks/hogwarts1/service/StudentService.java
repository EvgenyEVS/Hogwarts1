package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student addStudent(StudentCreateDto studentCreateDto) {
        Student student = new Student();
        student.setName(studentCreateDto.name());
        student.setAge(studentCreateDto.age());

        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(long id, Student student) {
        if (!studentRepository.existsById(id)) {
            return null;
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<Student> findByNameContainingIgnoreCase(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

}
