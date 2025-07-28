package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java58.person.dto.CityPopulationDto;
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
}
