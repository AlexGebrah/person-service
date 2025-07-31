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
import java.util.Arrays;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    private Class<?> getEntityClass(PersonDto dto) {
        if (dto instanceof EmployeeDto) return Employee.class;
        if (dto instanceof ChildDto) return Child.class;
        return Person.class;
    }

    private Class<? extends PersonDto> getDtoClass(Person person) {
        if (person instanceof Child) return ChildDto.class;
        if (person instanceof Employee) return EmployeeDto.class;
        return PersonDto.class;
    }


    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistException();
        }
        Class<?> xClass = getEntityClass(personDto);
        personRepository.save((Person) modelMapper.map(personDto, xClass));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return modelMapper.map(person, getDtoClass(person));
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return modelMapper.map(person, getDtoClass(person));
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(Integer id, String newName) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(newName);
        return modelMapper.map(person, getDtoClass(person));
    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(Integer id, AddressDto newAddress) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        Address address = modelMapper.map(newAddress, Address.class);
        person.setAddress(address);
        return modelMapper.map(person, getDtoClass(person));
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
    @Transactional(readOnly = true)
    public ChildDto[] findAllChildren() {
            return personRepository.findByType()
                    .map(child -> modelMapper.map(child, ChildDto.class))
                    .toArray(ChildDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto[] findEmployeesBySalary(Integer minSalary, Integer maxSalary) {
            return personRepository.findBySalaryBetween(minSalary, maxSalary)
                    .map(e -> modelMapper.map(e, EmployeeDto.class))
                    .toArray(EmployeeDto[]::new);
    }


    @Override
    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person = new Person(1000, "John", LocalDate.of(1985, 3, 11),
                    new Address("Tel Aviv", "Ben Gvirol", 81));
            Child child = new Child(2000, "Peter", LocalDate.of(2019, 7, 5),
                    new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
            Employee employee = new Employee(3000, "Mary", LocalDate.of(1995, 11, 23),
                    new Address("Rehovot", "Ben Herzl", 7), "Microsoft", 20_000);
            personRepository.saveAll(Arrays.asList(person, child, employee));
        }
    }
}
