package org.skypro.homeworks.hogwarts1;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.skypro.homeworks.hogwarts1.controller.FacultyController;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.repository.FacultyRepository;
import org.skypro.homeworks.hogwarts1.service.AvatarServiceImpl;
import org.skypro.homeworks.hogwarts1.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    AvatarServiceImpl avatarService;


    @Test
    public void createFacultyTest() throws Exception {

        final String name = "ТестовыйФакультет";
        final String color = "ТестовыйЦвет";
        final long id = 1;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        when(facultyService.addFaculty(any(org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }


    @Test
    public void getFacultyTest() throws Exception {

        final String name = "ТестовыйФакультет";
        final String color = "ТестовыйЦвет";
        final long id = 1;

        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        when(facultyService.findFaculty(id)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }


    @Test
    public void getFacultyIdNotFoundTest() throws Exception {

        final long id = Long.MAX_VALUE;

        when(facultyService.findFaculty(id)).thenReturn(null);

        mockMvc.perform(get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
