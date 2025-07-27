package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findByNameIgnoreCase(String name);

    List<Person> findByAddress_CityIgnoreCase(String city);

    List<Person> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query("SELECT new telran.java58.person.dto.CityPopulationDto(p.address.city, COUNT(p)) " +
            "FROM Person p GROUP BY p.address.city")
    List<CityPopulationDto> getCityPopulation();
}
