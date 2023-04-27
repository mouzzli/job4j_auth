package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.service.PersonService;
import ru.job4j.auth.validation.Operation;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService persons;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        var body = persons.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .header("MyCustomHeader", "job4j_auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable int id) {
        return persons.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "person ID does not exist"
        ));
    }

    @PutMapping("/")
    public ResponseEntity<Map<String, String>> update(@Validated(Operation.OnUpdate.class) @RequestBody Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        if (!persons.update(person)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "person ID does not exist");
        }
        return ResponseEntity.of(Optional.of(Map.of("status", "updated")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (!persons.delete(person)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "person ID does not exist");
        }
        return ResponseEntity.of(Optional.of(Map.of("status", "deleted")));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUp(@Validated(Operation.OnCreate.class) @RequestBody Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        persons.save(person);
        return ResponseEntity.of(Optional.of(Map.of("status", "created")));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Person> patchDTO(@Valid @RequestBody PersonDTO personDTO, @PathVariable int id) {
        var person = persons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        person.setPassword(passwordEncoder.encode(personDTO.getPassword()));
        return new ResponseEntity<>(persons.update(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
