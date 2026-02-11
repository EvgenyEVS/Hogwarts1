package org.skypro.homeworks.hogwarts1.service;

import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student addStudent(StudentCreateDto studentCreateDto) {

        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name: {}, age: {}",
                studentCreateDto.name(), studentCreateDto.age());

        Student student = new Student();
        student.setName(studentCreateDto.name());
        student.setAge(studentCreateDto.age());

        Student savedStudent = studentRepository.save(student);

        logger.debug("Student created successfully with ID: {}", savedStudent.getId());

        return savedStudent;
    }

    public Student findStudent(long id) {

        logger.info("Was invoked method for find student");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(long id, StudentUpdateDto studentUpdateDto) {

        logger.info("Was invoked method for edit student");

        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            logger.warn("Not found student with ID= {} ", id);
            return null;
        }

        Student student = optionalStudent.get();

        student.setName(studentUpdateDto.name());
        student.setAge(studentUpdateDto.age());
        return studentRepository.save(student);
    }

    public void removeStudent(long id) {

        logger.info("Was invoked method for remove student");
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudent() {

        logger.info("Was invoked method for get all student");
        return studentRepository.findAll();
    }

    public List<Student> findByNameContainingIgnoreCase(String name) {

        logger.info("Was invoked method for find by name student");
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> findByAge(int age) {

        logger.info("Was invoked method for find by age student");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int ageMin, int ageMax) {

        logger.info("Was invoked method for find by age between student");
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }


    public long getStudentQuantity() {

        logger.info("Was invoked method for get student quantity");
        return studentRepository.getStudentQuantity();
    }


    public double getAvgStudentAge() {

        logger.info("Was invoked method for get student age");
        return studentRepository.getAvgStudentAge().
                orElse(0.0);
    }


    public List<Student> getLast_5_StudentById() {

        logger.info("Was invoked method for get last 5 student");
        return studentRepository.getLast_5_StudentById();
    }

}
