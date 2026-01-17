package org.skypro.homeworks.hogwarts1.controller;

import org.junit.jupiter.api.Test;
import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;


    @Test
    public void createFacultyTest() throws Exception {

        long initialCount = facultyRepository.count();
        String url = "http://localhost:" + port + "/faculty";
        FacultyCreateDto facultyCreateDto = new FacultyCreateDto(
                "ТестовыйФакультет", "Красно-рыжый");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                url,
                facultyCreateDto,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty responseFaculty = response.getBody();

        assertNotNull(responseFaculty);
        assertThat(responseFaculty.getId()).isNotNull();
        assertThat(responseFaculty.getName()).isEqualTo("ТестовыйФакультет");
        assertThat(responseFaculty.getColor()).isEqualTo("Красно-рыжый");

        assertThat(facultyRepository.count()).isEqualTo(initialCount + 1);
    }


    @Test
    public void getFacultyTest() throws Exception {


        String url1 = "http://localhost:" + port + "/faculty";
        FacultyCreateDto facultyCreateDto = new FacultyCreateDto(
                "ТестовыйФакультет", "Красно-рыжый");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                url1,
                facultyCreateDto,
                Faculty.class
        );

        assertNotNull(response.getBody());
        Long createFacultyId = response.getBody().getId();

        String url = "http://localhost:" + port + "/faculty/" + createFacultyId;

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                url,
                Faculty.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("ТестовыйФакультет");
        assertThat(response.getBody().getColor()).isEqualTo("Красно-рыжый");
    }


    @Test
    public void editFacultyTest() throws Exception {

        String url1 = "http://localhost:" + port + "/faculty";
        FacultyCreateDto facultyCreateDto = new FacultyCreateDto(
                "ТестовыйФакультет", "Красно-рыжый");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                url1,
                facultyCreateDto,
                Faculty.class
        );

        assertNotNull(response.getBody());
        Long createFacultyId = response.getBody().getId();

        String updateUrl = "http://localhost:" + port + "/faculty/" + createFacultyId;
        Faculty newFaculty = new Faculty("НовыйТестовый", "Иссиня-синий");

        HttpEntity<Faculty> request = new HttpEntity<>(newFaculty);
        ResponseEntity<Faculty> updateResponse = restTemplate.exchange(
                updateUrl,
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getName()).isEqualTo("НовыйТестовый");
        assertThat(updateResponse.getBody().getColor()).isEqualTo("Иссиня-синий");

    }


    @Test
    public void removeFacultyTest() throws Exception {

        String url1 = "http://localhost:" + port + "/faculty";
        FacultyCreateDto facultyCreateDto = new FacultyCreateDto(
                "ТестовыйФакультет", "Красно-рыжый");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                url1,
                facultyCreateDto,
                Faculty.class
        );

        assertNotNull(response.getBody());
        Long createFacultyId = response.getBody().getId();

        String removeUrl = "http://localhost:" + port + "/faculty/" + createFacultyId;
        restTemplate.delete(removeUrl);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                removeUrl,
                Faculty.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getBody()).isNull();
    }


    @Test
    public void findFacultyByNameOrColorOrAllTest() throws Exception {

        String url1 = "http://localhost:" + port + "/faculty";
        FacultyCreateDto facultyCreateDto1 = new FacultyCreateDto(
                "ТестовыйФакультет", "Красно-рыжый");
        FacultyCreateDto facultyCreateDto2 = new FacultyCreateDto(
                "Недляпоиска", "Нецвет");

        ResponseEntity<Faculty> createResponse1 = restTemplate.postForEntity(
                url1,
                facultyCreateDto1,
                Faculty.class
        );

        ResponseEntity<Faculty> createResponse2 = restTemplate.postForEntity(
                url1,
                facultyCreateDto2,
                Faculty.class
        );

        String searchUrl = "http://localhost:" + port + "/faculty/search?search=ТестовыйФакультет";

        ResponseEntity<List<Faculty>> responseSearch = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSearch.getBody()).isNotNull();

        List<Faculty> foundFaculty = responseSearch.getBody();

        for (Faculty faculty : foundFaculty) {
            assertThat(faculty.getName()).isEqualTo("ТестовыйФакультет");
            assertThat(faculty.getColor()).isEqualTo("Красно-рыжый");
            assertThat(faculty.getName()).isNotEqualTo("Недляпоиска");
            assertThat(faculty.getColor()).isNotEqualTo("Нецвет");
        }
    }


    @Test
    public void whenSearchIsEmptyTest() throws Exception {
        String searchUrl = "http://localhost:" + port + "/faculty/search";

        ResponseEntity<List<Faculty>> responseSearch = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseSearch.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSearch.getBody()).isNotNull();

        List<Faculty> foundFaculty = responseSearch.getBody();

        assertThat(foundFaculty.size()).isEqualTo(facultyRepository.count());
    }
}