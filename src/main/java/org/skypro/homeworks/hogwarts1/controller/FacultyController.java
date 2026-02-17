package org.skypro.homeworks.hogwarts1.controller;

import org.skypro.homeworks.hogwarts1.dto.FacultyCreateDto;
import org.skypro.homeworks.hogwarts1.dto.FacultyUpdateDto;
import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public Faculty createFaculty(@Valid @RequestBody FacultyCreateDto facultyCreateDto) {
        return facultyService.addFaculty(facultyCreateDto);
    }


    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.findFaculty(id);

        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(faculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> editFaculty(@PathVariable long id, @Valid @RequestBody FacultyUpdateDto facultyUpdateDto) {
        Faculty updateFaculty = facultyService.editFaculty(id, facultyUpdateDto);

        if (updateFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updateFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeFaculty(@PathVariable long id) {
        facultyService.removeFaculty(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public ResponseEntity<List<Faculty>> findFacultyByNameOrColorOrAll(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(facultyService.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(search));
    }

    @GetMapping("/longestNameFaculty")
    public String longestNameFaculty() {
        return facultyService.maxLengthFacultyName();
    }

    @GetMapping("sumParallelStream")
    public long sumParallelStream() {
        return facultyService.sumParallelStream();
    }

}
