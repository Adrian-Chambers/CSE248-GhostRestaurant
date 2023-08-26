package com.project.project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;
import java.io.FileNotFoundException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class Search {

    private RestTemplate rest = new RestTemplate();
    private String key = "AIzaSyAvsG967a-l6FqlgkYAZNgdT74O2bv4pgo";
    private HttpEntity<String> request;
    
    public Search(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", "us-restaurant-menus.p.rapidapi.com");
        headers.set("X-RapidAPI-Key", "7f1ac3ea81msh93dc47a4102b2d6p10d681jsn7bb8b86cf3e6");
        request = new HttpEntity<String>(headers);
    }
    
/* US Restaurant API */
    public Restaurant searchByID(long id){
        ResponseEntity<String> response = rest.exchange("https://us-restaurant-menus.p.rapidapi.com/restaurant/" + id, HttpMethod.GET, request, String.class);
        String result = response.getBody();
        result = result.substring(result.indexOf("result")+8, result.lastIndexOf("}"));
        write(result, "search.json");

        Restaurant r = new Restaurant(readObject("search.json"));
        return r;
    }

    public ArrayList<Restaurant> searchByZipcode(String zipcode){
        ResponseEntity<String> response = rest.exchange("https://us-restaurant-menus.p.rapidapi.com/restaurants/zip_code/" + zipcode + "?page=1", HttpMethod.GET, request, String.class);
        String result = response.getBody();
        result = result.substring(result.indexOf("data")+6, result.lastIndexOf("]")+1);
        write(result, "search.json");

        ArrayList<Restaurant> results = new ArrayList<Restaurant>();
        JSONArray array = readArray("search.json");
        for(Object o : array){
            Restaurant r = new Restaurant((JSONObject) o);
            results.add(r);
        }
        return results;
    }

    public ArrayList<Restaurant> searchByCoords(double[] coords){
        ArrayList<Restaurant> results = new ArrayList<Restaurant>();
        if(coords[0] == 0.0 && coords[1] == 0.0) return results;
        
        ResponseEntity<String> response = rest.exchange("https://us-restaurant-menus.p.rapidapi.com/restaurants/search/geo?page=1&lat=" + coords[0] + "&lon=" + coords[1] + "&distance=2", HttpMethod.GET, request, String.class);
        String result = response.getBody();
        result = result.substring(result.indexOf("data")+6, result.lastIndexOf("]")+1);
        write(result, "search.json");
        
        JSONArray array = readArray("search.json");
        for(Object o : array){
            Restaurant r = new Restaurant((JSONObject) o);
            results.add(r);
        }
        return results;
    }

    public ArrayList<Restaurant> searchWithKeyword(String q, double[] coords){
        ResponseEntity<String> response = rest.exchange("https://us-restaurant-menus.p.rapidapi.com/restaurants/search?page=1&q=" + q + "&lat=" + coords[0] + "&lon=" + coords[1] + "&distance=1", HttpMethod.GET, request, String.class);
        String result = response.getBody();
        result = result.substring(result.indexOf("data")+6, result.lastIndexOf("]")+1);
        write(result, "search.json");

        JSONArray array = readArray("search.json");
        ArrayList<Restaurant> results = new ArrayList<Restaurant>();
        for(Object o : array){
            Restaurant r = new Restaurant((JSONObject) o);
            results.add(r);
        }
        return results;
    }

    /* menu */
    public ArrayList<MenuItem> getMenu(long id){
        ResponseEntity<String> response = rest.exchange("https://us-restaurant-menus.p.rapidapi.com/restaurant/" + id + "/menuitems/", HttpMethod.GET, request, String.class);
        String result = response.getBody();
        result = result.substring(result.indexOf("data")+6, result.lastIndexOf("]")+1);
        write(result, "search.json");

        JSONArray array = readArray("search.json");
        ArrayList<MenuItem> results = new ArrayList<MenuItem>();
        for(Object o : array){
            MenuItem i = new MenuItem((JSONObject) o);
            results.add(i);
        }
        return results;
    }

/* GoogleMaps API */
    public double[] getCoordsByAddress(String address){
        double[] coords = new double[2];
        String s = rest.getForObject("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + key, String.class);
        if(s.indexOf("location") <= 0){
            coords[0] = 0.0;
            coords[1] = 0.0;
        }
        else{
            s = s.substring(s.indexOf("location"), s.indexOf("location_type")-2);

            coords[0] = Double.parseDouble(s.substring(s.indexOf("lat")+6, s.indexOf(",")));
            coords[1] = Double.parseDouble(s.substring(s.indexOf("lng")+6, s.indexOf("}")));
        }
        
        return coords;
    }
    

    public void write(String str, String filename){
        try(FileWriter file = new FileWriter("search.json")){
            file.write(str);
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public JSONObject readObject(String filename){
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader(filename)){
            return (JSONObject) jsonParser.parse(reader);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray readArray(String filename){
       JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader(filename)){
            JSONArray array = (JSONArray) jsonParser.parse(reader);
            return array;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}