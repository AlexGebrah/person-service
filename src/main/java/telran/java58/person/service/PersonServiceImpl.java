package telran.java58.person.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;
import telran.java58.person.dto.exception.PersonExistException;
import telran.java58.person.dto.exception.PersonNotFoundException;
import telran.java58.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService{
    private  final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if(personRepository.existsById(personDto.getId())){
            throw new PersonExistException();
        }
        personRepository.save(modelMapper.map(personDto, Person.class));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto deletePerson(int id) {
        return null;
    }

    @Override
    public PersonDto updatePersonName(Integer id, String newName) {
        return null;
    }

    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto newAddress) {
        return null;
    }

    @Override
    public PersonDto[] findPersonByName(String name) {
        return new PersonDto[0];
    }

    @Override
    public PersonDto[] findPersonByCity(String name) {
        return new PersonDto[0];
    }

    @Override
    public PersonDto[] findPersonsBetweenAges(Integer minAge, Integer maxAge) {
        return new PersonDto[0];
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return null;
    }
}
