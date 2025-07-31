package telran.java58.person.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.*;
import telran.java58.person.dto.exception.PersonExistException;
import telran.java58.person.dto.exception.PersonNotFoundException;
import telran.java58.person.model.Address;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
import telran.java58.person.model.Person;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistException();
        }
        if(personDto instanceof EmployeeDto){
            personRepository.save(modelMapper.map(personDto, Employee.class));
            return;
        }
        if(personDto instanceof ChildDto){
            personRepository.save(modelMapper.map(personDto, Child.class));
            return;
        }
        personRepository.save(modelMapper.map(personDto, Person.class));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if(person instanceof Child){
            return modelMapper.map(person, ChildDto.class);
        }
        if(person instanceof Employee){
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if(person instanceof Child){
            return modelMapper.map(person, ChildDto.class);
        }
        if(person instanceof Employee){
            return modelMapper.map(person, EmployeeDto.class);
        }
        personRepository.delete(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(Integer id, String newName) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(newName);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(Integer id, AddressDto newAddress) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        Address address = modelMapper.map(newAddress, Address.class);
        person.setAddress(address);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonByName(String name) {
       return personRepository.findByNameIgnoreCase(name)
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonByCity(String city) {
        return personRepository.findByAddress_CityIgnoreCase(city)
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonsBetweenAges(Integer minAge, Integer maxAge) {
        LocalDate maxBirthDate = LocalDate.now().minusYears(minAge);
        LocalDate minBirthDate = LocalDate.now().minusYears(maxAge);

        return personRepository.findByBirthDateBetween(minBirthDate, maxBirthDate)
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personRepository.getCityPopulation();
    }

    @Override
    public void run(String... args) throws Exception {
        if(personRepository.count() == 0) {
            Person person = new Person(1000, "Jhon", LocalDate.of(1985, 3, 11),
            new Address("Tel Aviv", "Rotshild", "81"));
            Child child = new Child(2000, "Peter", LocalDate.of(2019, 7, 5),
                    new Address("Ashkelon", "Gert", "4"), "Shalom");
            Employee employee = new Employee(3000, "Mary", LocalDate.of(1997, 7,23),
                    new Address("Kfa Saba", "Ben Sion", "54"), "MMM", 10000);
            personRepository.save(person);
            personRepository.save(child);
            personRepository.save(employee);
        }
    }
}
