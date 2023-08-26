package com.project.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String idStr;
    private String name;
    private String address;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String image;
    private double netSales;
    private String cuisines;
    private ArrayList<Long> menuIDs;
    private ArrayList<Long> orderIDs;

    public Restaurant(){ 
        this.menuIDs = new ArrayList<Long>();
        this.orderIDs = new ArrayList<Long>();
    }

    public Restaurant(JSONObject o){
        this.id = (long)o.get("restaurant_id");
        idStr = "1" + id;
        this.name = (String)o.get("restaurant_name");
        this.street = ((String)((JSONObject)o.get("address")).get("street")).replaceAll("&amp;", "&");
        this.state = (String)((JSONObject)o.get("address")).get("state");
        this.city = (String)((JSONObject)o.get("address")).get("city");
        this.zipcode = (String)((JSONObject)o.get("address")).get("postal_code");
        this.address = street + " " + city + ", " + state + " " + zipcode;
        this.menuIDs = new ArrayList<Long>();
        this.orderIDs = new ArrayList<Long>();

        JSONArray arr = (JSONArray)(o.get("cuisines"));
        cuisines = "";
        for(int i = 0; i < arr.size(); i++){
            cuisines += ((String) arr.get(i)).replaceAll("&amp;", "&");
            if(i != arr.size()-1) cuisines += ", ";
        }

        if(o.containsKey("menu_ids")){
            idStr = 0 + idStr.substring(1);
            image = (String)(o.get("image"));
            arr = (JSONArray)(o.get("menu_ids"));
            for(int i = 0; i < arr.size(); i++){
                menuIDs.add((long)arr.get(i));
            }
        }
    }

    /* get */
    public long getIdLong(){ return id; }
    public String getIdStr(){ return idStr; }
    public String getName(){ return name; }
    public String getStreet(){ return street; }
    public String getCity(){ return city; }
    public String getState(){ return state; }
    public String getZipcode(){ return zipcode; }
    public String getAddress(){ return address; }
    public double getNetSales(){ return netSales; }
    public String getCuisines(){ return cuisines; }
    public String getImage(){ return image; }
    public ArrayList<Long> getMenuIDs(){ return menuIDs; }
    public ArrayList<Long> getOrderIDs(){ return orderIDs; }
    public int totalOrders(){ return orderIDs.size(); }
    public String getCuisinesStr(){ return cuisines; }

    /* set */
    public void setID(long id){ this.id = id; }
    public void setIDStr(String idStr){ this.idStr = idStr; }
    public void setName(String name){ this.name = name; }
    public void setStreet(String street){ this.street = street; }
    public void setCity(String city){ this.city = city; }
    public void setState(String state){ this.state = state; }
    public void setZipcode(String zipcode){ this.zipcode = zipcode; }
    public void setImage(String image){ this.image = image; }
    public void setCuisines(String cuisines){ this.cuisines = cuisines; }
    public void setMenuIDs(ArrayList<Long> menuIDs){ this.menuIDs = menuIDs; }
   
    public void updateRestaurant(Restaurant r){
        name = r.getName();
        street = r.getStreet();
        city = r.getCity();
        state = r.getState();
        zipcode = r.getZipcode();
        cuisines = r.getCuisines();
        image = r.getImage();
    }

    /* menu */
    public boolean addItem(long id){
        if(menuIDs.contains(id)) return false;
        menuIDs.add(id);
        return true;
    }

    public boolean removeItem(long id){
        if(!menuIDs.contains(id)) return false;
        menuIDs.remove(id);
        return true;
    }

    /* Orders */
    public void addOrder(CustomerOrder o){
        orderIDs.add(o.getID());
        netSales += o.getTotal();
    }


}