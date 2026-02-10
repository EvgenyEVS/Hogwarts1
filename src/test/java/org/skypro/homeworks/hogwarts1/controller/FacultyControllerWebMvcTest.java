package org.skypro.homeworks.hogwarts1.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.dto.FacultyUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
@ActiveProfiles("test")
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;


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

        when(facultyService.addFaculty(any(FacultyCreateDto.class))).thenReturn(faculty);

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

        when(facultyService.findFaculty(eq(id))).thenReturn(faculty);

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


    @Test
    public void editFacultyTest() throws Exception {

        final String name = "ТестовыйФакультет";
        final String color = "ТестовыйЦвет";
        final long id = 1;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty updetedFaculty = new Faculty(name, color);
        updetedFaculty.setId(id);

        when(facultyService.editFaculty(eq(id), any(FacultyUpdateDto.class)))
                .thenReturn(updetedFaculty);

        mockMvc.perform(put("/faculty/" + id)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }


    @Test
    public void removeFacultyTest() throws Exception {

        final long id = 1;

        doNothing().when(facultyService).removeFaculty(eq(id));

        mockMvc.perform(delete("/faculty/" + id)).andExpect(status().isOk());
        verify(facultyService).removeFaculty(eq(id));
    }


    @Test
    public void findByNameCaseOrColorContainingTest() throws Exception {

        String search = "Поиск";
        final long id = 1;
        List<Faculty> facultyList = Arrays.asList(
                new Faculty("ПоискИмя", "ПоискЦвет"),
                new Faculty("ПростоИмя", "ПоискЦвет"));

        when(facultyService.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(eq(search)))
                .thenReturn(facultyList);

        mockMvc.perform(get("/faculty/search")
                        .param("search", search)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("ПоискИмя"))
                .andExpect(jsonPath("$.[0].color").value("ПоискЦвет"))
                .andExpect(jsonPath("$.[1].name").value("ПростоИмя"))
                .andExpect(jsonPath("$.[1].color").value("ПоискЦвет"));
    }
}