package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="emails")
@NoArgsConstructor
@Getter
@Setter
public class Email extends Contact implements Comparable<Email> {
    @Column(name = "email", nullable=false, unique=true, length = 80)
    @NotNull
    private String email;

    public Email(boolean isActive, boolean isPrior, Person owner, String email) {
        super(isActive, isPrior, owner);
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EMAIL: ");
        sb.append(email).append(super.toString());
        return sb.toString();
    }

    @Override
    public String toStringWithOwner() {
        final StringBuilder sb = new StringBuilder("EMAIL: ");
        sb.append(email).append(super.toStringWithOwner());
        return sb.toString();
    }

    @Override
    public int compareTo(Email o) {
        return this.email.compareTo(o.email);
    }
}
