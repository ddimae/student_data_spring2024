package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="phones")
@NoArgsConstructor
@Getter
@Setter
public class PhoneNumber extends Contact implements Comparable<PhoneNumber> {
    @Column(name = "phone_number", nullable=false, unique=true, length = 12)
    @NotNull
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        return phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }

    public PhoneNumber(boolean isActive, boolean isPrior, Person owner, String phoneNumber) {
        super(isActive, isPrior, owner);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PHONE: ");
        sb.append(phoneNumber).append(super.toString());
        return sb.toString();
    }

    @Override
    public String toStringWithOwner() {
        final StringBuilder sb = new StringBuilder("PHONE: ");
        sb.append(phoneNumber).append(super.toStringWithOwner());
        return sb.toString();
    }

    @Override
    public int compareTo(PhoneNumber o) {
        return this.phoneNumber.compareTo(o.phoneNumber);
    }
}
