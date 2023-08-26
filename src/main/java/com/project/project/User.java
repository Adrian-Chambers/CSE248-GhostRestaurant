package com.project.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private int role;    // 0 = guest, 1 = customer, 2 = owner
    private String restaurantID;

    public User(){}

    public User(int id, int role, String username, String password, String firstName, String lastName, String restaurantID){
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.restaurantID = restaurantID;
    }
    
    // Get
    public int getID(){ return id; }
    public int getRole(){ return role; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public String getRestaurantID(){ return restaurantID; }

    // Set
    public void setID(int id){ this.id = id; }
    public void setRole(int role){ this.role = role; }
    public void setUsername(String username){ this.username = username; }
    public void setPassword(String password){ this.password = password; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName){ this.lastName = lastName; }
    public void setRestaurantID(String restaurantID){ this.restaurantID = restaurantID; }

    public void update(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }


}