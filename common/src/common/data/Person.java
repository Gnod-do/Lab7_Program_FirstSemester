package common.data;

import java.io.Serializable;
import java.time.LocalDate;

public class Person implements Serializable {
    private String name;

    private java.time.LocalDate birthday;

    private Long height;

    private Double weight;

    private String passportID;

    public Person(String name, LocalDate birthday, Long height, Double weight, String passportID) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
    }

    /**
     * @return Name of person.
     */

    public String getName(){return name;}

    /**
     * @return Time of birthday.
     */

    public LocalDate getBirthday(){return birthday;}

    /**
     * @return height of this person
     */

    public Long getHeight(){return height;}

    /**
     * @return weight of this person
     */

    public Double getWeight(){return weight;}

    /**
     * @return number of passport
     */

    public String getPassportID(){return passportID;}

    @Override
    public String toString() {
        return " " + name + " was born on " + birthday + " with " + " height: " + height + " ,weight: " + weight +" and passportID: " + passportID + " was created ";
    }

    @Override
    public int hashCode() {return name.hashCode() + birthday.hashCode() + height.hashCode() + weight.hashCode() + + passportID.hashCode();}

}
