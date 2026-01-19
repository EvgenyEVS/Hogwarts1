package org.skypro.homeworks.hogwarts1.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record FacultyUpdateDto(
        @NotBlank(message = "Name не может быть пустым")
        @Size(min = 2, max = 30, message = "Name должно быть от 2 до 30 символов")
        String name,

        @NotBlank(message = "Color не может быть пустым")
        @Size(min = 2, max = 30, message = "Color должно быть от 2 до 30 символов")
        String color
) {
}
