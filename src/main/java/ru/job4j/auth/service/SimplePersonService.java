package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePersonService implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public boolean delete(Person person) {
        if (checkPersonExist(person)) {
            personRepository.delete(person);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean update(Person person) {
        if (checkPersonExist(person)) {
            personRepository.save(person);
            return true;
        }
        return false;
    }

    private boolean checkPersonExist(Person person) {
        var optionalPerson = personRepository.findById(person.getId());
        return optionalPerson.isPresent();
    }
}
