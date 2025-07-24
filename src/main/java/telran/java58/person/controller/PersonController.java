package telran.java58.person.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;
import telran.java58.person.service.PersonService;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody PersonDto personDto) {
        personService.addPerson(personDto);
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable int id) {
        return personService.getPerson(id);
    }

    @DeleteMapping("/{id}")
    public PersonDto deletePerson(@PathVariable int id) {
        return personService.getPerson(id);
    }

    @PatchMapping("/{id}/name/{newName}")
    public PersonDto updatePersonName(@PathVariable Integer id, @PathVariable String newName) {
        return personService.updatePersonName(id, newName);
    }

    @PatchMapping("/{id}/address")
    public PersonDto updatePersonAddress(@PathVariable Integer id, @RequestBody AddressDto newAddress) {
        return personService.updatePersonAddress(id, newAddress);
    }

    @GetMapping("/name/{name}")
    public PersonDto[] findPersonByName(@PathVariable String name) {
        return personService.findPersonByName(name);
    }

    @GetMapping("/city/{city}")
    public PersonDto[] findPersonByCity(@PathVariable String city) {
        return personService.findPersonByCity(city);
    }

    @GetMapping("/ages/{minAge}/{maxAge}")
    public PersonDto[] findPersonsBetweenAges(@PathVariable Integer minAge, @PathVariable Integer maxAge) {
        return personService.findPersonsBetweenAges(minAge, maxAge);
    }

    @GetMapping("/population/city")
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personService.getCityPopulation();
    }
}
