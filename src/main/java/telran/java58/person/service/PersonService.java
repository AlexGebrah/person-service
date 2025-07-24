package telran.java58.person.service;

import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;

public interface PersonService {
    void addPerson(PersonDto personDto);

    PersonDto getPerson(int id);

    PersonDto deletePerson(int id);

    PersonDto updatePersonName(Integer id, String newName);

    PersonDto updatePersonAddress(Integer id, AddressDto newAddress);

    PersonDto[] findPersonByName (String name);

    PersonDto[] findPersonByCity (String name);

    PersonDto[] findPersonsBetweenAges(Integer minAge, Integer maxAge);

        Iterable<CityPopulationDto> getCityPopulation();
}
