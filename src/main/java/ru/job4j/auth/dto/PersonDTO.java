package ru.job4j.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {

    @NotNull(message = "password must be non null")
    @Size(min = 6, message = "password must be more than {min} characters")
    private String password;
}
