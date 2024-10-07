package onetomany.Phones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetomany.Users.User;

@Entity
public class Phone {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String manufacturer;
    private double cameraQuality;
    private double battery;
    private int price;

    /*
     * @ManyToOne tells springboot that multiple instances of Phone can map to one instance of OR multiple rows of the phone table can map to one user row
     * @JoinColumn specifies the ownership of the key i.e. The Phone table will contain a foreign key from the User table and the column name will be user_id
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/phone objects (phone->user->[phones]->...)
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

     // =============================== Constructors ================================== //

    public Phone(){

    }
    
    public Phone( String manufacturer, double cameraQuality, double battery, String name, int price) {
        this.manufacturer = manufacturer;
        this.cameraQuality = cameraQuality;
        this.battery = battery;
        this.name = name;
        this.price = price;
    }


    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getCameraQuality() {
        return cameraQuality;
    }

    public void setCameraQuality(double cameraQuality) {
        this.cameraQuality = cameraQuality;
    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
