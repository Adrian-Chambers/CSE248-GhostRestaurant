package com.project.project;


public class OrderItem {
    private long id;
    private String name;
    private String option;
    private double price;
    private int quantity;

    public OrderItem(){
        quantity = 1;
    }

    public OrderItem(MenuItem item, ItemOption itemOption){
        this.id = item.getId();
        this.name = item.getName();
        this.option = itemOption.getOption();
        this.price = itemOption.getPrice();
        this.quantity = itemOption.getQuantity();
    }

    public OrderItem(long id, String name, String option, double price, int quantity){
        this.id = id;
        this.name = name;
        this.option = option;
        this.price = price;
        this.quantity = quantity;
    }

    /* get */
    public long getId(){ return id; }
    public String getName(){ return name; }
    public String getOption(){ return option; }
    public double getPrice(){ return price; }
    public int getQuantity(){ return quantity; }
    public double getTotal(){ 
        return Math.round(price * quantity * 100.0) / 100.0; 
    }
    
    /* set */
    public void setId(long id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setOption(String option){ this.option = option;}
    public void setPrice(double price){ this.price = price; }
    public void setQuantity(int quantity){ this.quantity = quantity; }

    public void addQuantity(int amount){
        quantity = quantity + amount;
    }

    public void removeQuantity(int amount){
        quantity = quantity - amount;
    }

    @Override
    public boolean equals(Object o){
        OrderItem item = (OrderItem) o;
        return (item.id == id);
    }

}