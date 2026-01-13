package org.skypro.homeworks.hogwarts1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.homeworks.hogwarts1.controller.StudentController;
import org.skypro.homeworks.hogwarts1.dto.StudentCreateDto;
import org.skypro.homeworks.hogwarts1.dto.StudentResponseDto;
import org.skypro.homeworks.hogwarts1.dto.StudentUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.skypro.homeworks.hogwarts1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void createStudentTest() throws Exception {

        long initialCount = studentRepository.count();
        String url = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto = new StudentCreateDto("Тест Создаванов", 999);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                url,
                studentCreateDto,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student responseStudent = response.getBody();

        assertNotNull(responseStudent);
        assertThat(responseStudent.getId()).isNotNull();
        assertThat(responseStudent.getName()).isEqualTo("Тест Создаванов");
        assertThat(responseStudent.getAge()).isEqualTo(999);

        assertThat(studentRepository.count()).isEqualTo(initialCount + 1);
    }


    @Test
    public void getStudentByIdTest() throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto = new StudentCreateDto("Тест Создаванов", 999);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                createUrl,
                studentCreateDto,
                Student.class
        );


        Long createStudentId = createResponse.getBody().getId();
        String url = "http://localhost:" + port + "/student/" + createStudentId;

        ResponseEntity<StudentResponseDto> getResponse = restTemplate.getForEntity(
                url,
                StudentResponseDto.class
        );


        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().name()).isEqualTo("Тест Создаванов");
        assertThat(getResponse.getBody().age()).isEqualTo(999);
    }


    @Test
    public void getNonExistingStudent() throws Exception {
        long nonExistingId = Long.MAX_VALUE;
        String url = "http://localhost:" + port + "/student/" + nonExistingId;

        ResponseEntity<StudentResponseDto> response = restTemplate.getForEntity(
                url,
                StudentResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void editStudentById() throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto = new StudentCreateDto("Старый Создаванов", 999);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                createUrl,
                studentCreateDto,
                Student.class
        );


        Long createStudentId = createResponse.getBody().getId();
        String updateUrl = "http://localhost:" + port + "/student/" + createStudentId;
        StudentUpdateDto studentUpdateDto = new StudentUpdateDto("Новый Создаванов", 1000);

        HttpEntity<StudentUpdateDto> request = new HttpEntity<>(studentUpdateDto);
        ResponseEntity<Student> updateResponse = restTemplate.exchange(
                updateUrl,
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getName()).isEqualTo("Новый Создаванов");
        assertThat(updateResponse.getBody().getAge()).isEqualTo(1000);
        assertThat(updateResponse.getBody().getId()).isEqualTo(createStudentId);
    }


    @Test
    public void removeStudentTest () throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto = new StudentCreateDto("Старый Создаванов", 999);

        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                createUrl,
                studentCreateDto,
                Student.class
        );


        Long createStudentId = createResponse.getBody().getId();
        String removeUrl = "http://localhost:" + port + "/student/" + createStudentId;

        restTemplate.delete(removeUrl);

        ResponseEntity<StudentResponseDto> getResponse = restTemplate.getForEntity(
                removeUrl,
                StudentResponseDto.class
        );
       assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
       assertThat(getResponse.getBody()).isNull();
    }


}


