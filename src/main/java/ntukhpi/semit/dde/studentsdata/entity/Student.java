package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.semit.dde.studentsdata.utils.Formats;
import ntukhpi.semit.dde.studentsdata.utils.KinshipDegree;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Milka Vladislav
 * @version 1.0
 * @created 27-Mar-2023 11:32:15 AM
 */

@Entity
@Table(name="students",uniqueConstraints = @UniqueConstraint(columnNames = {"sname", "fname","pname","birthday"}))
@NoArgsConstructor
@Getter
@Setter
public class Student extends Person {

    @Column(name = "contract",nullable = false)
    @ColumnDefault(value="FALSE")
    private boolean contract;
    @Column(name = "scholarship",nullable = false)
    @ColumnDefault(value="FALSE")
    private boolean takeScholarship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_group_id")
    AcademicGroup academicGroup;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "students_parents", joinColumns = @JoinColumn(name = "id_parent"))
    @MapKeyJoinColumn(name = "id_student")
    @Column(name = "kinship_degree", nullable = false)
	private Map<Parent, KinshipDegree> parents;

    //Constructors
    public Student(@NotNull String lastName, String firstName, String middleName) {
        super(lastName, firstName, middleName);
        contract = false;
        takeScholarship = false;

        parents = new HashMap<>();
    }

    public Student(@NotNull String lastName, String firstName, String middleName, LocalDate dateOfBirth) {
        super(lastName,firstName,middleName,dateOfBirth);
        contract = false;
        takeScholarship = false;
        parents = new HashMap<>();
    }

    public Student(String lastName, String firstName, String middleName, String dateOfBirthStr) {
        super(lastName, firstName, middleName, dateOfBirthStr);
        contract = false;
        takeScholarship = false;
        parents = new HashMap<>();
    }

    public Student(String lastName, String firstName, String middleName, LocalDate dateOfBirth, boolean isContract, boolean isScholarship) {
        super(lastName,firstName,middleName,dateOfBirth);
        contract = isContract;
        takeScholarship = isScholarship;
        parents = new HashMap<>();
    }



    //for parents
    public Map<Parent, KinshipDegree> getParents() {
        return Collections.unmodifiableMap(parents);
    }

    public void addParent(Parent parent, KinshipDegree kinshipDegree) {
        parents.put(parent,kinshipDegree);
    }

    public void delParent(Parent parent) {
        parents.remove(parent);
    }

    //for parsing
    public Student(Row row) {
        this(row.getCell(1).getStringCellValue());
    }

    public Student(String fullName){
        this(fullName.split(" ")[0], fullName.split(" ")[1], fullName.split(" ")[2], LocalDate.of(2004, Month.JANUARY, 1)
                .plusDays((long) (Math.random() * 365)));
    }

    public String showParents(){
        StringBuilder sb = new StringBuilder("Parents:");
        if (parents.isEmpty()){
            sb.append("absent");
        } else {
            parents.entrySet().stream().forEach(e->sb.append(System.lineSeparator())
                    .append(e.getValue()).append(" ").append(e.getKey().toString()));
        }
        return sb.toString();

    }
    public String isContractToString(){
        if (contract) {
            return "Контракт";
        }
            return "Бюджет";
        }

    public String isTakeScholarshipToString(){
        if (takeScholarship) {
            return "Так";
        }
        return "Ні";
    }

    public String showInfoWithGroup(){
        StringBuilder sb = new StringBuilder(super.toString());
        if (academicGroup!= null) {
            sb.append(" (").append(academicGroup.toString()).append(")");
        }
        sb.append(super.contactsPersonToString());
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getLastName());

        if (getFirstName() != null) {
            stringBuilder.append(" ").append(getFirstName());
        }

        if (getMiddleName() != null) {
            stringBuilder.append(" ").append(getMiddleName());
        }

        if (getDateOfBirth() != null) {
            stringBuilder.append(" ").append(getDateOfBirth().format(Formats.FORMAT_DATE_UA));
        }
        stringBuilder.append("\nContract: ").append(contract).append("; Scholarship: ").append(takeScholarship);

        return stringBuilder.toString();

    }
}