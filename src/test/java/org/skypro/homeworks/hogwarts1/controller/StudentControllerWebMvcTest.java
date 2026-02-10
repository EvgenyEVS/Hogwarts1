package org.skypro.homeworks.hogwarts1.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@ActiveProfiles("test")
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;


    @Test
    public void createStudentTest() throws Exception {

        final String name = "ТестовоеИмя";
        final int age = 999;
        final long id = 1;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student(name, age);
        student.setId(id);

        when(studentService.addStudent(any(StudentCreateDto.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }


    @Test
    public void getStudentTest() throws Exception {

        final String name = "ТестовоеИмя";
        final int age = 999;
        final long id = 1;

        Student student = new Student(name, age);
        student.setId(id);

        when(studentService.findStudent(eq(id))).thenReturn(student);

        mockMvc.perform(get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }


    @Test
    public void getStudentIdNotFoundTest() throws Exception {

        final long id = Long.MAX_VALUE;

        when(studentService.findStudent(id)).thenReturn(null);

        mockMvc.perform(get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void editStudentTest() throws Exception {

        final String name = "ТестовоеИмя";
        final int age = 999;
        final long id = 1;

        Student student = new Student(name, age);
        student.setId(id);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", 999);

        when(studentService.editStudent(eq(id), any(StudentUpdateDto.class)))
                .thenReturn(student);

        mockMvc.perform(put("/student/" + id)
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }


    @Test
    public void removeStudentTest() throws Exception {

        final long id = 1;

        doNothing().when(studentService).removeStudent(eq(id));

        mockMvc.perform(delete("/student/" + id)).andExpect(status().isOk());
        verify(studentService).removeStudent(eq(id));
    }


    @Test
    public void findStudentByNameOrColorOrAll_WhenNameTest() throws Exception {

        String searchName = "Имя";
        int searchAge = 4;
        List<Student> studentList = Arrays.asList(
                new Student("Имя1", 1),
                new Student("Имя2", 2),
                new Student("Имя3", 3),
                new Student("Не4", 4)
        );

        when(studentService.findByNameContainingIgnoreCase(searchName))
                .thenReturn(studentList.stream()
                        .filter(s -> s.getName().contains(searchName))
                        .collect(Collectors.toList()));

        mockMvc.perform(get("/student/search")
                        .param("name", searchName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Имя1"))
                .andExpect(jsonPath("$[0].age").value(1))
                .andExpect(jsonPath("$[1].name").value("Имя2"))
                .andExpect(jsonPath("$[1].age").value(2))
                .andExpect(jsonPath("$[2].name").value("Имя3"))
                .andExpect(jsonPath("$[2].age").value(3));

    }


    @Test
    public void findStudentByNameOrColorOrAll_WhenAgeTest() throws Exception {

        int searchAge = 4;
        List<Student> studentList = Arrays.asList(
                new Student("Имя1", 1),
                new Student("Имя2", 2),
                new Student("Имя3", 3),
                new Student("Не4", 4)
        );

        List<Student> filteredList = studentList.stream()
                .filter(s -> s.getAge() == searchAge)
                .collect(Collectors.toList());

        when(studentService.findByAge(searchAge)).thenReturn(filteredList);


        mockMvc.perform(get("/student/search")
                        .param("age", String.valueOf(searchAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Не4"))
                .andExpect(jsonPath("$[0].age").value(4));
    }

    @Test
    public void findByAgeBetweenTest() throws Exception {

        int ageMin = 1;
        int ageMax = 4;
        List<Student> studentList = Arrays.asList(
                new Student("Имя1", 1),
                new Student("Имя2", 2),
                new Student("Имя3", 3),
                new Student("Не4", 4)
        );

        List<Student> filteredList = studentList.stream()
                .filter(s -> s.getAge() > ageMin && s.getAge() < ageMax)
                .collect(Collectors.toList());

        when(studentService.findByAgeBetween(ageMin, ageMax)).thenReturn(filteredList);


        mockMvc.perform(get("/student/search/age/between")
                        .param("ageMin", String.valueOf(ageMin))
                        .param("ageMax", String.valueOf(ageMax))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Имя2"))
                .andExpect(jsonPath("$[0].age").value(2))
                .andExpect(jsonPath("$[1].name").value("Имя3"))
                .andExpect(jsonPath("$[1].age").value(3));
    }
}