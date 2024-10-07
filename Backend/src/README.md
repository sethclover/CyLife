# SpringBoot JPA: One to Many relation example

### One to One relations
In object-oriented programming, real-life entities are represented as classes, and in MySQL, they are stored as tables. JPA/Hibernate is a framework that matches a Class to Table(
each variable represents a column) to ease the manipulation of data in Java. Entities almost always interact with one another and establish individual relationships among 
themselves Eg: A Person will have exactly one social security number(One to One), A person can have multiple cars(One to Many), etc. This example is created with the assumption
that one person can have only one laptop(made by poor people). It shows how a one to one relation can be represented between entities using JPA/Hibernate.

### One to Many relations
Very often the interrations between entities are not always limited to OneToOne. SpringBoot provided the @OneToMany and @ManyToOne to specify these type of relations. In this example it is assumed that one person can own more than one phone, and if at all 2 people own the same model phone the ids have to be different.


### Pre-requisites

0. Go through the springboot_unit1_2_hellopeople
1. Go through the springboot_unit2_1_onetoone example
2. Maven has to be installed on command line OR your IDE must be configured with maven
3. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean package</code></br>
<code>java -jar target/onetoone-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Available End points from POSTMAN: CRUDL
1. CREATE requests - 
POST request: 
    1. /laptops - Create a new Laptop. The request has to be of type application/JSON input format 
    {
        "cpuClock" : "1.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "1000000"
    }
    2. /users - Create a new User with laptop. Request Format : application/json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com",
        laptop   : {
            "cpuClock" : "2.3",
            "cpuCores"  : "4",
            "ram"   : "8",
            "manufacturer" : "Hp",
            "cost" : "600"
        }
    }. 
    3. /users - Create user without laptop. Request Format : application / json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com"
    }
    4. /users - create the user and associate it with an existing phone.
    {
        "name" : "John",
        "email" : "johndoe@somemail.com",
        "phones" : [
            { "id" : "1"},
            { "id" : "2" }
        ]
    }. Note that you cannot create phones(cascade option not present on @OneToMany), but you can associate a user to the phone which will overwrite the previous user for the phone.
    4. /phones - Create a new Phone. cannot create a new user(no cascade option), but you can associate an existing user to the phone 
    {
        "name" : "iPhone X",
        "price" : "2000",
        "cameraQuality": "13",
        "manufacturer: : "apple",
        "battery" : "3000",
        "user" : {
            "id" : 3
        }
    }
2. READ requests -
GET request:
    1. /laptops/{id} - Get laptop object for provided id
    2. /users/{id} - Get user object with associated laptop and phones for provided user id
    3. /phones/{id} - Get phone objects with provided id

3. UPDATE requests -
PUT request : 
    1. /laptops/{id} - Update info on a laptop whose id is provided on the URL. The modified info is sent in the body of the message
    {
        "cpuClock" : "2.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "2000000"
    }
    2. /users/{id} - Update User and optionally laptop and phone associated with him/her for id provided in the url
    {
        "name" : "John",
        "email"  : "johndoe@newmail.com",
        laptop   : {
            "id" : "5"
            "cpuClock" : "3.3",
            "cpuCores"  : "8",
            "ram"   : "16",
            "manufacturer" : "new Laptop",
            "cost" : "1000"
        },
        "phones" : [{"id":1},{"id":2} ]
    }.
    3. /phones/{id} - Update the Phone for given id and optionally modify the user_id associated with phone
     {
        "name" : "iPhone X",
        "price" : "2000",
        "cameraQuality": "13",
        "manufacturer: : "apple",
        "battery" : "3000",
        "user" : {
            "id" : 5
        }
    }
4. DELETE a record - 
 DELETE request:
    1. /laptops/{id} - Delete the entry with id in te url
    2. /users/{id} - delete a user with the id provided in the url
    3. /phones/{id} - delete the phone for the given id

5. LIST all -  
GET request
    1. /laptops - Get/List all the laptops stored in the db
    2. /users - Get/List all the users along with their associated laptops
    3. /phones = Get/List all the phones stores in the database

#

### Note :
### 1. /laptops APIs are defined in Laptops/LaptopController, 
### 2. /users APIs are defined in the Users/UserController
### 3. /phones APIs are defined in the Phones/PhoneController

# 
## Some helpful links:
[SpringBoot Entity](https://www.baeldung.com/jpa-entities)   
[SpringBoot OneToOne](https://www.baeldung.com/jpa-one-to-one)    
[SpringBoot OneToMany](https://www.baeldung.com/hibernate-one-to-many)

### Version Tested

|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |
