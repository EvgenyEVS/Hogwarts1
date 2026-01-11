package org.skypro.homeworks.hogwarts1.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record StudentUpdateDto(
        @NotBlank(message = "Name не может быть пустым")
        @Size(min = 2, max = 30, message = "Name должно быть от 2 до 30 символов")
        String name,

        @NotBlank(message = "Хоть какой-то возраст укажите!")
        @Size(min = 0, max = Integer.MAX_VALUE, message = "В Хогвартс не принимают " +
                "с возрастом меньше 0 или больше Integer.MAX_VALUE")
        int age
) { }
