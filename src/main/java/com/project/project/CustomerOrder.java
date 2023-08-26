package com.project.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class CustomerOrder{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int userID;
    private String restaurantID;

    private ArrayList<Long> cartID;
    private ArrayList<String> cartName;
    private ArrayList<String> cartSize;
    private ArrayList<Double> cartPrice;
    private ArrayList<Integer> cartQuantity;
    
    private boolean orderComplete;
    private String payment;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private double total;

    public CustomerOrder(){ }

    public CustomerOrder(int userID, String restaurantID, Checkout checkout, Cart cart){
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.cartID = new ArrayList<Long>();
        this.cartName = new ArrayList<String>();
        this.cartSize = new ArrayList<String>();
        this.cartPrice =  new ArrayList<Double>();
        this.cartQuantity =  new ArrayList<Integer>();

        this.street = checkout.getShipStreet();
        this.city = checkout.getShipCity();
        this.state = checkout.getShipState();
        this.zipcode = checkout.getShipZip();

        this.orderComplete = false;
        this.payment = checkout.getPayment();
        
        for(OrderItem item : cart.getItems()){
            cartID.add(item.getId());
            cartName.add(item.getName());
            cartSize.add(item.getOption());
            cartPrice.add(item.getPrice());
            cartQuantity.add(item.getQuantity());
            total = cart.getTotal();
        }
    }

    /* get */
    public long getID(){ return id; }
    public long getUserID(){ return userID; }
    public String getRestaurantID(){ return restaurantID; }
    public boolean orderComplete(){ return orderComplete; }
    public String getPayment(){ return payment; }
    public String getStreet(){ return street; }
    public String getCity(){ return city; }
    public String getState(){ return state; }
    public String getZipcode(){ return zipcode; }
    public double getTotal(){ return total; }

    /* set */
    public void setID(int id){ this.id = id; }
    public void setUserID(int userID){ this.userID = userID; }
    public void setRestaurantID(String restaurantID){ this.restaurantID = restaurantID; }
    public void setOrderStatus(boolean orderComplete){ this.orderComplete = orderComplete; }
    public void setPayment(String payment){ this.payment = payment; }
    public void setStreet(String street){ this.street = street; }
    public void setCity(String city){ this.city = city; }
    public void setState(String state){ this.state = state; }
    public void setZipcode(String zipcode){ this.zipcode = zipcode; }
    public void setTotal(double total){ this.total = total; }

    /* items */
    public ArrayList<OrderItem> getCart(){
        ArrayList<OrderItem> items = new ArrayList<OrderItem>();
        for(int i = 0; i < cartName.size(); i++){
            items.add(new OrderItem(cartID.get(i), cartName.get(i), cartSize.get(i), cartPrice.get(i), cartQuantity.get(i)));
        }
        return items;
    }

}