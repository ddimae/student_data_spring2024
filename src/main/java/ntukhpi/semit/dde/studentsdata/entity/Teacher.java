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
@Table(name="teachers",uniqueConstraints = @UniqueConstraint(columnNames = {"sname", "fname","pname","birthday"}))
@NoArgsConstructor
@Getter
@Setter
public class Teacher extends Person {

    //Constructors
    public Teacher(@NotNull String lastName, String firstName, String middleName, LocalDate dateOfBirth) {
        super(lastName, firstName, middleName, dateOfBirth);
    }

    public Teacher(@NotNull String lastName, String firstName, String middleName, String dateOfBirthStr) {
        super(lastName, firstName, middleName, dateOfBirthStr);
    }

    public Teacher(@NotNull String lastName, String firstName, String middleName) {
        super(lastName, firstName, middleName);
    }
}
