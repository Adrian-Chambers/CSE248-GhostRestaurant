package com.project.project;

import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    Iterable<Restaurant> findAllByZipcode(String zipcode);
    Iterable<Restaurant> findAllByStreet(String street);
    Iterable<Restaurant> findAllByAddress(String address);

}