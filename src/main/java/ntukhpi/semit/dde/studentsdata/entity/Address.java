package ntukhpi.semit.dde.studentsdata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ntukhpi.semit.dde.studentsdata.utils.KinshipDegree;
import org.hibernate.annotations.ColumnDefault;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "adresses")
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  //Long!!! not long, not int

    @Column(nullable = false)
    private String country;
    private String region;
    private String city;
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "persons_adresses", joinColumns = @JoinColumn(name = "id_address"))
    @MapKeyJoinColumn(name = "id_owner")
    @Column(name = "current", nullable = false)
    @ColumnDefault(value = "FALSE")
	private Map<Person, Boolean> owners;

    public Address(String country, String region, String city, String address) {
        this.id = 0L;
        this.country = country;
        this.region = region;
        this.city = city;
        this.address = address;
        owners = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address1 = (Address) o;

        if (!country.equals(address1.country)) return false;
        if (!Objects.equals(region, address1.region)) return false;
        if (!Objects.equals(city, address1.city)) return false;
        return Objects.equals(address, address1.address);
    }

    @Override
    public int hashCode() {
        int result = country.hashCode();
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    //for owners
    public Map<Person, Boolean> getOwners() {
        return Collections.unmodifiableMap(owners);
    }

    public void addOwner(Person owner, Boolean isCurrent) {
        owners.put(owner, isCurrent);
    }

    public void delOwner(Person owner) {
        owners.remove(owner);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (!Objects.isNull(country) && !country.isEmpty()) {
            sb.append(country);
        }
        if (!Objects.isNull(region) && !region.isEmpty()) {
            sb.append(", ").append(region);
        }
        if (!Objects.isNull(city) && !city.isEmpty()) {
            sb.append(", ").append(city);
        }
        if (!Objects.isNull(address) && !address.isEmpty()) {
            sb.append(", ").append(address);
        }
        return sb.toString();
    }

    public String toStringWithOwners() {
        final StringBuilder sb = new StringBuilder("Address: ");
        if (!Objects.isNull(country) && country.length() > 0) {
            sb.append(System.lineSeparator()).append("country='").append(country).append('\'');
        }
        if (!Objects.isNull(region) && !region.isEmpty() && region.length() > 0) {
            sb.append(System.lineSeparator()).append("region='").append(region).append('\'');
        }
        if (!Objects.isNull(city) && !city.isEmpty() && city.length() > 0) {
            sb.append(System.lineSeparator()).append(System.lineSeparator()).append("city='").append(city).append('\'');
        }
        if (!Objects.isNull(address) && !address.isEmpty() && address.length() > 0) {
            sb.append(System.lineSeparator()).append("address='").append(address).append('\'');
        }
        if (owners.isEmpty()) {
            sb.append(System.lineSeparator()).append("OWNERS: no owners");
        } else {
            sb.append(System.lineSeparator()).append("OWNERS: " + owners.size());
            owners.entrySet().stream().forEach(entry -> sb.append(System.lineSeparator()).append(entry.getKey().toString() + " " + (entry.getValue() ? "(CURRENT)" : "")));
        }
        return sb.toString();
    }
}
