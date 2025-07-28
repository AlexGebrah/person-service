package telran.java58.person.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "persons")
public class Person {
    @Id
    private int id;
    @Setter
    private String name;
    private LocalDate birthDate;
    @Setter
    private Address address;
}
