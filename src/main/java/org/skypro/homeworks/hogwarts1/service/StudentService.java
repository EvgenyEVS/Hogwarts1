package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Student editStudent(long id, StudentUpdateDto studentUpdateDto) {

        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return null;
        }

        Student student = optionalStudent.get();

        student.setName(studentUpdateDto.name());
        student.setAge(studentUpdateDto.age());
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<Student> findByNameContainingIgnoreCase(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }


    public long getStudentQuantity() {
        return studentRepository.getStudentQuantity();
    }


    public double getAvgStudentAge() {
        return studentRepository.getAvgStudentAge().
                orElse(0.0);
    }


    public List<Student> getLast_5_StudentById() {
        return studentRepository.getLast_5_StudentById();
    }

}
