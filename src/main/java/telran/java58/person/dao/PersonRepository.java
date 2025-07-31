package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Stream<Person> findByNameIgnoreCase(String name);

    Stream<Person> findByAddress_CityIgnoreCase(String city);

    Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT new telran.java58.person.dto.CityPopulationDto(p.address.city, COUNT(p)) " +
            "FROM Person p GROUP BY p.address.city")
    List<CityPopulationDto> getCityPopulation();

    Stream<Child> findByType();

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    Stream<Employee> findBySalaryBetween(@Param("min") Integer min, @Param("max") Integer max);
}
