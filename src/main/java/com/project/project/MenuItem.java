package com.project.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String category;
    private String image;
    private ArrayList<String> options;
    private ArrayList<Double> prices;

    public MenuItem(){ 
        this.prices = new ArrayList<Double>();
        this.options = new ArrayList<String>();
    }

    public MenuItem(JSONObject o){ 
        this.id = (long)o.get("item_id");
        this.name = (String)o.get("menu_item_name");
        this.description = (String)o.get("menu_item_description");
        this.category = (String)o.get("subsection");
        this.prices = new ArrayList<Double>();
        this.options = new ArrayList<String>();

        JSONArray arr = (JSONArray)(o.get("menu_item_pricing"));
        for(int i = 0; i < arr.size(); i++){
            String str = (String)((JSONObject)arr.get(i)).get("priceString");
            double p = Double.parseDouble(str.substring(str.indexOf("$")+1));
            if(p == 0.0){ break; }
            else{ prices.add(p); }
        }

        if(o.containsKey("image")){ this.image = (String) o.get("image"); }

        if(o.containsKey("options")){
            arr = (JSONArray)(o.get("options"));
            for(int i = 0; i < arr.size(); i++){
                options.add((String)(arr.get(i)));
            }
        }
        else{
            if(prices.size() == 1){ options.add("Regular"); }
            if(prices.size() >= 2){ 
                options.add("Small");
                options.add("Large");
            }
            if(prices.size() >= 3){ options.add(1, "Medium"); }
            if(prices.size() == 4){ options.add("Extra Large"); }
        }
    }

    /* get */
    public long getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public String getCategory(){ return category; }
    public String getImage(){ return image; }
    public ArrayList<Double> getPrices(){ return prices; }
    public ArrayList<String> getOptions(){ return options; }
    public String getPriceStr(){ 
         String str = "";
         if(prices.size() <= 0){
            str = "Not Available";
         }
         else if(prices.size() == 1){
             str = "" + prices.get(0);
         }
         else{
             for(int i = 0; i < prices.size()-1; i++){
                str += "" + prices.get(i) + " - ";
            }
            str += "" + prices.get(prices.size()-1);
         }
            
         return str;
    }
    public ArrayList<String> getOptionsStr(){
        ArrayList<String> arr = new ArrayList<String>();
        if(options.size() > 0 && prices.size() > 0){
            for(int i = 0; i < options.size(); i++){
                arr.add("" + options.get(i) + " - $" + prices.get(i));
            }
        }
        
        return arr;
    }

    /* set */
    public void setId(long id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setDescription(String description){ this.description = description; }
    public void setCategory(String category){ this.category = category; }
    public void setImage(String image){ this.image = image; }
    public void setPrices(ArrayList<Double> prices){ this.prices = prices; }
    public void setOptions(ArrayList<String> options){ this.options = options; }

    public void editItem(MenuItem o){
        name = o.getName();
        category = o.getCategory();
        description = o.getDescription();
    }

    public void addOption(String option, double price){
        options.add(option);
        prices.add(price);
    }

    public void editOption(ItemOption o){
        int i = (int)o.getId();
        options.set(i, o.getOption());
        prices.set(i, o.getPrice());
    }

    public void removeOption(int i){
        options.remove(i);
        prices.remove(i);
    }

    @Override
    public boolean equals(Object obj){
        MenuItem item = (MenuItem) obj;
        if(item.getId() == id) return true;
        return false;
    }

}