# Ghost Restaurant Project
CSE-238 Final Project


## Summary
Users can search for local restaurants and place orders.
Restaurant owners to upload/edit their menus and keep track of orders.


## Features

### Search
The search bar allows users to enter a zipcode, town, or address to find nearby restaurants. Adding a keyword can also help narrow down the results even further.

Search results are made up of a combination of restaurants from the database and restaurants found using an external "US Restaurants" API. 
Only the restaurants created with the database have user accounts associated with them. 

For testing purposes, all the initial database restaurants share a common zipcode (11784) so they can be found all at once by searching for the zipcode.

### Menu
After searching for and clicking on a restaurant, the customer will be directed to the restaurant's menu. 
The items on the menu are separated based on their categories (ex. "Appetizers", "Burgers", "Drinks").

Items have a name, description (optional), image (optional), and options that the user can choose from. The price of the item depends on the option. 
All of this information is displayed to the user when clicking on an item on the menu. From here, the user can choose an option and set the quantity before adding the item to the cart.

### Cart
The cart shows the user what items they added to their order and what the total cost will be. 
Customers can alter the items in their cart in the following ways: change quantity, change option, delete item.

### Checkout
During checkout, the customer must enter their payment and shipping information so that their order will be delivered to their location. 
When the checkout is complete, the order is added to the user's "order history" and the restaurant's "orders" sections.

### Register
There are two types of accounts: one for customers and one for restaurant owners. 
Thus, there are separate areas for customers to register for an account and restaurant owners to create their restaurant and register for an account. 
The account type is denoted by a user's "role", where 0=guest, 1=customer, 2=restaurant.

Guests will be prompted to register for a customer account if they try to place an order.

### Customer Accounts
Having an account allows users to keep track of their past orders. They can also change their username, password, and name through the "account settings" page.

### Restaurant Dashboard
The restaurant dashboard allows restaurant owners to keep track of customer orders, update restaurant information, and add/edit/remove items from the menu.

Menu items are required to have a name and a category. The description and image source are not necessary. 
Adding "options" are also not necessary however customers will not be able to purchase an item if no options are set (it will be listed as "unavailable").

## Things to improve
If I had more time I would figure out a way to make the code less lengthy and more efficient. 
I would also improve on some of the html/css, especially for the restaurant dashboard.