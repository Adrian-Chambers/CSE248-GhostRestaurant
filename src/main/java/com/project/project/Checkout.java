package com.project.project;

public class Checkout{
    private String payNumber;
    private String payMonth;
    private String payYear;
    private String firstName;
    private String lastName;
    private String shipStreet;
    private String shipCity;
    private String shipZip;
    private String shipState;

    public Checkout(){  }

    public String getPayment(){
        return payNumber + " " + payMonth + "/" + payYear;
    }

    // Set
    public void setPayNumber(String payNumber){ this.payNumber = payNumber; }
    public void setPayMonth(String payMonth){ this.payMonth = payMonth; }
    public void setPayYear(String payYear){ this.payYear = payYear; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName){ this.lastName = lastName; }
    public void setShipStreet(String shipStreet){ this.shipStreet = shipStreet; }
    public void setShipCity(String shipCity){ this.shipCity = shipCity; }
    public void setShipZip(String shipZip){ this.shipZip = shipZip; }
    public void setShipState(String shipState){ this.shipState = shipState; }

    // Get
    public String getPayNumber(){ return payNumber; }
    public String getPayMonth(){ return payMonth; }
    public String getPayYear(){ return payYear; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public String getShipStreet(){ return shipStreet; }
    public String getShipCity(){ return shipCity; }
    public String getShipZip(){ return shipZip; }
    public String getShipState(){ return shipState; }

}