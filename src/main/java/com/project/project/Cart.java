package com.project.project;

import java.util.ArrayList;

public class Cart {
    private String restaurantID;
    private String restaurantName;
    private ArrayList<OrderItem> items;
    private double total;

    public Cart(){
        this("-1", "");
    }

    public Cart(String id, String name){
        restaurantID = id;
        restaurantName = name;
        items = new ArrayList<OrderItem>();
        total = 0.0;
    }

    /* get */
    public String getRestaurantID(){ return restaurantID; }
    public String getRestaurantName(){ return restaurantName;}
    public ArrayList<OrderItem> getItems(){ return items; }
    public double getTotal(){ return Math.round(total * 100.0) / 100.0; }

    /* set */
    public void setRestaurantID(String restaurantID){ this.restaurantID = restaurantID; }
    public void setRestaurantName(String restaurantName){ this.restaurantName = restaurantName; }

    /* items */
    public void addItem(OrderItem item){
        if(items.contains(item)){
            OrderItem i = items.get(items.indexOf(item));
            if(i.getOption().equals(item.getOption())) i.addQuantity(item.getQuantity());
            else items.add(item);
        }
        else{
            items.add(item);
        }
        total += item.getTotal();
    }

    public OrderItem getItem(long id){
        for(OrderItem i : items){
            if(i.getId() == id) return i;
        }
        return null;
    }

    public void removeItem(long id){
        for(OrderItem item : items){
            if(item.getId() == id){
                total -= item.getTotal();
                items.remove(item);
                break;
            }
        }
    }

    public void editItem(ItemOption options){
        OrderItem item = getItem(options.getId());
        total -= item.getTotal();
        int i = items.indexOf(item);

        item.setOption(options.getOption());
        item.setPrice(options.getPrice());
        item.setQuantity(options.getQuantity());

        items.set(i, item);
        total += item.getTotal();
    }

}