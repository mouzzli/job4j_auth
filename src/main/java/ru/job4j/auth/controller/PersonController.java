package ru.job4j.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDTO;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.NoSuchElementException;

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

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        validate(person);
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        validate(person);
        return new ResponseEntity<>(persons.update(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        return new ResponseEntity<>(persons.delete(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        validate(person);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        persons.save(person);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Person> patchDTO(@RequestBody PersonDTO personDTO, @PathVariable int id) {
        var person = persons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        person.setPassword(passwordEncoder.encode(personDTO.getPassword()));
        return new ResponseEntity<>(persons.update(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    private void validate(Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NoSuchElementException("login and password must be present");
        }
        if (person.getLogin().equals("") || person.getPassword().equals("")) {
            throw new NullPointerException("login and password mustn't be empty");
        }
    }
}
