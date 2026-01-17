package org.skypro.homeworks.hogwarts1.dto;

import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.model.Student;

public record StudentResponseDto(
        Long id,
        String name,
        int age,
        FacultyInfoDto faculty
) {
    public static StudentResponseDto fromEntity(Student student) {
        FacultyInfoDto facultyInfo = student.getFaculty() != null ?
                FacultyInfoDto.fromEntity(student.getFaculty()) :
                null;

        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getAge(),
                facultyInfo
        );
    }

    public record FacultyInfoDto(
            Long id,
            String name,
            String color
    ) {
        public static FacultyInfoDto fromEntity(Faculty faculty) {
            return new FacultyInfoDto(
                    faculty.getId(),
                    faculty.getName(),
                    faculty.getColor()
            );
        }
    }
}
