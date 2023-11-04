package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public abstract class Contact { //

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) //GenerationType.IDENTITY
    @Column(name = "id")
    private Long id;  //Long!!! not long, not int

    @Column(name = "active",nullable = false)
    @ColumnDefault(value="TRUE")
    private boolean active;

    @Column(name = "prior",nullable = false)
    @ColumnDefault(value="FALSE")
    private boolean prior;

    public Contact(boolean isActive, boolean isPrior, Person owner) {
        this.active = isActive;
        this.prior = isPrior;
        this.owner = owner;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" (");
        sb.append(active ?"Активний":"Неактивний");
        sb.append(prior ?",Основний":"");
        sb.append(')');
        return sb.toString();
    }

    public String toStringWithOwner() {
        final StringBuilder sb = new StringBuilder(" (");
        sb.append(active ?"Активний":"Неактивний");
        sb.append(prior ?",Основний":"");
        sb.append(')');
        sb.append(", власник - ").append(getOwner().initialsToString());
        return sb.toString();
    }
}
