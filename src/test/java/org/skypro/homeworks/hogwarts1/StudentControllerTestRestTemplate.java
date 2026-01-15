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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import java.util.List;

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
    public void removeStudentTest() throws Exception {

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


    @Test
    public void findStudentByNameOrColorOrAllTestWhenName() throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto1 = new StudentCreateDto("Тестируемый поиск1", 111);
        StudentCreateDto studentCreateDto2 = new StudentCreateDto("Тестируемый поиск2", 222);
        StudentCreateDto studentCreateDto3 = new StudentCreateDto("Неподходящее НеИмя", 333);

        ResponseEntity<Student> createResponse1 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto1,
                Student.class
        );
        ResponseEntity<Student> createResponse2 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto2,
                Student.class
        );
        ResponseEntity<Student> createResponse3 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto3,
                Student.class
        );

        String searchUrl = "http://localhost:" + port + "/student/search?name=Тестируемый поиск";

        ResponseEntity<List<StudentResponseDto>> responseSearch = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentResponseDto>>() {
                }
        );

        assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSearch.getBody()).isNotNull();

        List<StudentResponseDto> foundStudents = responseSearch.getBody();

        for (StudentResponseDto student : foundStudents) {
            assertThat(student.name()).contains("Тестируемый поиск");
            assertThat(student.name()).doesNotContain("Неподходящее НеИмя");
        }

        for (StudentResponseDto student : foundStudents) {
            assertThat(student.age()).isIn(111, 222);
            assertThat(student.age()).isNotEqualTo(333);
        }

    }



    @Test
    public void findStudentByNameOrColorOrAllTestWhenIsNotNameNotAge () throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto1 = new StudentCreateDto("Тестируемый поиск1", 111);
        StudentCreateDto studentCreateDto2 = new StudentCreateDto("Тестируемый поиск2", 222);
        StudentCreateDto studentCreateDto3 = new StudentCreateDto("Неподходящее НеИмя", 333);

        ResponseEntity<Student> createResponse1 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto1,
                Student.class
        );
        ResponseEntity<Student> createResponse2 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto2,
                Student.class
        );
        ResponseEntity<Student> createResponse3 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto3,
                Student.class
        );

        String searchUrl = "http://localhost:" + port + "/student/search";

        ResponseEntity<List<StudentResponseDto>> responseSearch = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentResponseDto>>() {
                }
        );

        List<StudentResponseDto> foundStudents = responseSearch.getBody();

       boolean hasAdded1 = foundStudents.stream()
               .anyMatch(s -> s.name().contains("Тестируемый поиск"));
        boolean hasAdded2 = foundStudents.stream()
                .anyMatch(s -> s.name().contains("Неподходящее НеИмя"));
       assertThat(hasAdded1).isTrue();
       assertThat(hasAdded2).isTrue();
    }


    @Test
    public void findByAgeBetween() throws Exception {

        String createUrl = "http://localhost:" + port + "/student";
        StudentCreateDto studentCreateDto1 = new StudentCreateDto("Тестируемый поиск1", 111);
        StudentCreateDto studentCreateDto2 = new StudentCreateDto("Тестируемый поиск2", 222);
        StudentCreateDto studentCreateDto3 = new StudentCreateDto("Неподходящее НеИмя", 333);

        ResponseEntity<Student> createResponse1 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto1,
                Student.class
        );
        ResponseEntity<Student> createResponse2 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto2,
                Student.class
        );
        ResponseEntity<Student> createResponse3 = restTemplate.postForEntity(
                createUrl,
                studentCreateDto3,
                Student.class
        );

        String searchUrl = "http://localhost:" + port + "/student/search/age/between?ageMin=110&ageMax=223";

        ResponseEntity<List<StudentResponseDto>> responseSearch = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentResponseDto>>() {
                }
        );

        List<StudentResponseDto> foundStudents = responseSearch.getBody();

        boolean hasAdded1 = foundStudents.stream()
                .anyMatch(s -> s.age() == 111);
        boolean hasAdded2 = foundStudents.stream()
                .anyMatch(s -> s.age() == 222);
        boolean hasAdded3 = foundStudents.stream()
                .anyMatch(s -> s.age() != 333);
        assertThat(hasAdded1).isTrue();
        assertThat(hasAdded2).isTrue();
        assertThat(hasAdded3).isTrue();

    }

}


