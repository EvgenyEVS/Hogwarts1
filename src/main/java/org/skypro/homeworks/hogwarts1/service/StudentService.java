package org.skypro.homeworks.hogwarts1.service;

import liquibase.sdk.Main;
import org.apache.el.stream.Stream;
import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentResponseDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }


    public List<Student> getLast_5_StudentById() {

        logger.info("Was invoked method for get last 5 student");
        return studentRepository.getLast_5_StudentById();
    }


    public List<Student> getStudentsStartsWithA() {
        List<Student> students = studentRepository.findAll().stream()
                .map(s -> {
                    s.setName(s.getName().toUpperCase());
                    return s;
                })
                .filter(s -> s.getName().startsWith("A"))
                .sorted(Comparator.comparing(Student::getName))
                .toList();
        return students;
    }

    public void studentsPrintParallel() {
        List<Student> students = studentRepository.findAll().stream()
                .limit(6)
                .toList();

        System.out.println(students.get(0).getName() + " 0");
        System.out.println(students.get(1).getName() + " 1");

        new Thread(() -> {
            System.out.println(students.get(2).getName() + " 2");
            System.out.println(students.get(3).getName() + " 3");
        }
        ).start();

        new Thread(() -> {
            System.out.println(students.get(4).getName() + " 4");
            System.out.println(students.get(5).getName() + " 5");
        }
        ).start();
    }


    public synchronized void studentsPrintSynchronized() {
        List<Student> students = studentRepository.findAll().stream()
                .limit(6)
                .toList();

        Object lock = new Object();
        AtomicInteger count = new AtomicInteger(0);

        printName(students, count, lock);
        printName(students, count, lock);


        new Thread(() -> {
            printName(students, count, lock);
            printName(students, count, lock);
        }
        ).start();

        new Thread(() -> {
            printName(students, count, lock);
            printName(students, count, lock);
        }
        ).start();
    }

    private void printName(List<Student> students, AtomicInteger count, Object lock) {

        int index;
        synchronized (lock) {
            index = count.getAndIncrement();
        }

        if (index < students.size()) {
            System.out.println(students.get(index).getName() + " = " + index + " count__"
                    + Thread.currentThread().getName());
        }
    }

}
