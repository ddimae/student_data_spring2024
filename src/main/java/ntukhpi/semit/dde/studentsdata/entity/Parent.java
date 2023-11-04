package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "parents",uniqueConstraints = @UniqueConstraint(columnNames = {"sname", "fname","pname","birthday"}))
@NoArgsConstructor
@Getter
@Setter
public class Parent extends Person {

    //Constructors
    public Parent(@NotNull String lastName, String firstName, String middleName, LocalDate dateOfBirth) {
        super(lastName, firstName, middleName, dateOfBirth);
    }
    public Parent(@NotNull String lastName, String firstName, String middleName, String dateOfBirthStr) {
        super(lastName, firstName, middleName, dateOfBirthStr);
    }

    public Parent(@NotNull String lastName, String firstName, String middleName) {
        super(lastName, firstName, middleName);
    }
}
