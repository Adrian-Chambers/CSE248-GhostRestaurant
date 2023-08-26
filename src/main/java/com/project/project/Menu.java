package com.project.project;

import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuItem> items;
    private ArrayList<String> categories;

    public Menu(ArrayList<MenuItem> arr){
        this.items = new ArrayList<MenuItem>();
        categories = new ArrayList<String>();
        for(MenuItem item : arr){
            if(item.getPriceStr() != null && !item.getPriceStr().equals("$0.00")){
                items.add(item);
                String category = item.getCategory();
                if(!categories.contains(category)) categories.add(category);
            }
            
        }
    }

    public ArrayList<MenuItem> getItems(){ return items; }
    public ArrayList<String> getCategories(){ return categories; }

    public void addItem(MenuItem item){
        items.add(item);
        String category = item.getCategory();
        if(!categories.contains(category)) categories.add(category);
    }

    public void editItem(MenuItem item){
        int i = items.indexOf(item);
        if(i == -1){ items.add(item); }
        else {
            items.set(i, item);
            String category = item.getCategory();
            if(!categories.contains(category)) categories.add(category);
        }
    }

    public void removeItem(MenuItem item){
        items.remove(item);
    }
    
}