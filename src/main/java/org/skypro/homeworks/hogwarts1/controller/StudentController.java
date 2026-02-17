package org.skypro.homeworks.hogwarts1.controller;

import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentResponseDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public Student createStudent(@Valid @RequestBody StudentCreateDto studentCreateDto) {
        return studentService.addStudent(studentCreateDto);
    }


    @GetMapping("{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(StudentResponseDto.fromEntity(student));
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> editStudent(@PathVariable long id, @Valid @RequestBody StudentUpdateDto studentUpdateDto) {

        Student editStudent = studentService.editStudent(id, studentUpdateDto);

        if (editStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> findStudentByNameOrColorOrAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age) {

        List<Student> students;

        if (name != null && !name.isBlank()) {
            students = studentService.findByNameContainingIgnoreCase(name);
        } else if (age != null && age > 0) {
            students = studentService.findByAge(age);
        } else {
            students = studentService.getAllStudent();
        }

        List<StudentResponseDto> responseDto = students.stream()
                .map(StudentResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/search/age/between")
    public ResponseEntity<List<Student>> findByAgeBetween(
            @RequestParam int ageMin,
            @RequestParam int ageMax) {
        return ResponseEntity.ok(studentService.findByAgeBetween(ageMin, ageMax));
    }


    @GetMapping("/quantity")
    public long getStudentQuantity() {
        return studentService.getStudentQuantity();
    }


    @GetMapping("/age_avg")
    public double getAvgStudentAge() {
        return studentService.getAvgStudentAge();
    }

    @GetMapping("/last_5_StudentById")
    public List<StudentResponseDto> getLast_5_StudentById() {

        return studentService.getLast_5_StudentById().stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    @GetMapping("/getStudentsStartsWith_A")
    public List<StudentResponseDto> getStudentsStartsWith_A() {
        return studentService.getStudentsStartsWith_A().stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }
}
