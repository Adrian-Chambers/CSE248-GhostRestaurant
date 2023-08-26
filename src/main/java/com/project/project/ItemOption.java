package com.project.project;

public class ItemOption {
    private  int id;
    private  String optionStr;
    private  String option;
    private  double price;
    private  int quantity;

    public ItemOption(){
        optionStr = " ";
        option = "";
        price = 0.0;
        quantity = 1;
    }

    public ItemOption(OrderItem o){
        id = (int)o.getId();
        option = o.getOption();
        price = o.getPrice();
        quantity = o.getQuantity();
        optionStr = option + " - $" + price;
    }

    public ItemOption(int id, String option, double price){
        this.id = id;
        this.option = option;
        this.price = price;
    }

    /* get */
    public int getId(){ return id; }
    public String getOptionStr(){ return optionStr; }
    public String getOption(){ return option; }
    public double getPrice(){ return price; }
    public int getQuantity(){ return quantity; }

    /* set */
    public void setId(int id){ this.id = id; }
    public void setOptionStr(String optionStr){ 
        this.optionStr = optionStr;
        option = optionStr.substring(0, optionStr.indexOf("-")-1);
        price = Double.parseDouble(optionStr.substring(optionStr.indexOf("$")+1));
    }
    public void setOption(String option){ this.option = option;}
    public void setPrice(double price){ this.price = price; }
    public void setQuantity(int quantity){ this.quantity = quantity; }


}