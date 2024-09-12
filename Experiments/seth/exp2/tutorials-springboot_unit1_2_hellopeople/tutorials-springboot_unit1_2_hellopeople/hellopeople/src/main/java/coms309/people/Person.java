package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String homePlanet;

    private String telephone;

    public Person(){
        
    }

    public Person(String firstName, String lastName, String homePlanet, String telephone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.homePlanet = homePlanet;
        this.telephone = telephone;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHomePlanet() {
        return this.homePlanet;
    }

    public void setHomePlanet(String homePlanet) {
        this.homePlanet = homePlanet;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + homePlanet + " "
               + telephone;
    }
}
