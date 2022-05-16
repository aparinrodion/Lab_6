package bsu.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1 style="color:green">BSU FAMCS</h1>
 * <h2 style="color:green">LAB_6</h2>
 * @author Aparin Rodion
 * @see Serializable
 */
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

    /**
     * default <i>Constructor</i>
     */
    public CitizenAndAddress() {

    }

    /**
     * @param o Object to compare with
     * @return <ul>
     *     <li>true - if objects are equal</li>
     *     <li>false - if objects are not equal</li>
     * </ul>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CitizenAndAddress that = (CitizenAndAddress) o;
        return name.equals(that.name) && surname.equals(that.surname) && city.equals(that.city) && street.equals(that.street) && building.equals(that.building);
    }

    /**
     * @return int hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, surname, city, street, building);
    }

    /**
     *
     * @param name name of citizen
     * @param surname surname of citizen
     * @param city  city where citizen lives
     * @param street street where citizen lives
     * @param building building where citizen lives
     */
    public CitizenAndAddress(String name, String surname, String city, String street, Integer building) {
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.street = street;
        this.building = building;
    }

    /**
     * <p>Generate {@link String String} from object</p>
     * @return {@link String String} from object
     */
    public String toString() {
        return name + " " + surname + " " + city + " " + street + " " + building;
    }
}
