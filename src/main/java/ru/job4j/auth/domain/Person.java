package ru.job4j.auth.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.auth.validation.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @NotNull(message = "Id must be non null", groups = Operation.OnUpdate.class)
    private Integer id;

    @NotBlank(message = "login must be non empty", groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    private String login;

    @NotNull(message = "password must be non null", groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    @Size(min = 6, message = "password must be more than {min} characters", groups = {Operation.OnUpdate.class, Operation.OnCreate.class})
    private String password;
}
