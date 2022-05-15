package model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter

@XmlRootElement(name = "citizen")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "citizen", propOrder = {"name", "surname", "city", "street", "building"})
public class CitizenAndAddress implements Serializable {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String surname;
    @XmlElement(required = true)
    private String city;
    @XmlElement(required = true)
    private String street;
    @XmlElement(required = true)
    private Integer building;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CitizenAndAddress that = (CitizenAndAddress) o;
        return name.equals(that.name) && surname.equals(that.surname) && city.equals(that.city) && street.equals(that.street) && building.equals(that.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, city, street, building);
    }

    public CitizenAndAddress() {
    }

    public CitizenAndAddress(String name, String surname, String city, String street, Integer building) {
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.street = street;
        this.building = building;
    }


    public String toString() {
        return name + " " + surname + " " + city + " " + street + " " + building;
    }
}
