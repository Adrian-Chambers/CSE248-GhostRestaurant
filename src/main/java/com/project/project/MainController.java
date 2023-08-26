package com.project.project;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@Controller
public class MainController {
	private CustomerOrderRepository orders;
	private MenuItemRepository menuItems;
	private RestaurantRepository restaurants;
	private UserRepository users;

	private User currentUser;
	private Restaurant currentRestaurant;
	private Menu currentMenu;
	private MenuItem currentItem;
	private Cart cart;

	private Search search;

	@Autowired
	public MainController(UserRepository userRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuRepository, CustomerOrderRepository orderRepository) {
		search = new Search();
		cart = new Cart();

		JSONArray arr = search.readArray("items.json");
		for(Object o : arr){
			MenuItem i = new MenuItem((JSONObject) o);
        	menuRepository.save(i);
        }

		arr = search.readArray("restaurants.json");
		for(Object o : arr){
			Restaurant r = new Restaurant((JSONObject) o);
			restaurantRepository.save(r);
        }
		
		userRepository.save(new User(1, 0, "guest", " ", "guest", "guest", "-1"));
        userRepository.save(new User(2, 2, "restaurant1", "pass", "first", "last", "01"));
		userRepository.save(new User(3, 2, "restaurant2", "pass", "first", "last", "02"));
		userRepository.save(new User(4, 2, "restaurant3", "pass", "first", "last", "03"));
		userRepository.save(new User(5, 2, "restaurant4", "pass", "first", "last", "04"));

		currentUser = userRepository.findById(1).get();

		orders = orderRepository;
		menuItems = menuRepository;
		restaurants = restaurantRepository;
		users = userRepository;
	}

/* Index */	
	@RequestMapping(value={"/index.html", "/", "/home", "/index"})
	public String index(Model model) {
		if(currentUser.getRole() == 2) return "redirect:" + "restaurant-index";
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "index";
	}

/* Search */
	public Restaurant findRestaurantByID(String idStr){
		long id = Long.parseLong(idStr.substring(1));
		if(idStr.charAt(0) == '0') return restaurants.findById(id).get();
		else return search.searchByID(id);
	}

	public ArrayList<Restaurant> findRestaurantByZipcode(String zipcode){
		ArrayList<Restaurant> results = new ArrayList<Restaurant>();
		results.addAll((ArrayList<Restaurant>) restaurants.findAllByZipcode(zipcode));
		results.addAll(search.searchByZipcode(zipcode));
		return results;
	}

	public ArrayList<Restaurant> findRestaurantByAddress(String address){
		ArrayList<Restaurant> results = new ArrayList<Restaurant>();
		results.addAll((ArrayList<Restaurant>) restaurants.findAllByAddress(address));
		results.addAll((ArrayList<Restaurant>) restaurants.findAllByStreet(address));
		results.addAll(search.searchByCoords(search.getCoordsByAddress(address)));
		return results;
	}

	public ArrayList<Restaurant> findRestaurantByKeyword(String address, String keyword){
		ArrayList<Restaurant> results = new ArrayList<Restaurant>();
		ArrayList<Restaurant> temp;
		keyword = keyword.toLowerCase().replaceAll("%20", " ");

		if(address.length() == 5 && address.matches("[0-9]+")) temp = (ArrayList<Restaurant>) restaurants.findAllByZipcode(address);
		else temp = (ArrayList<Restaurant>) restaurants.findAllByAddress(address);

		for(Restaurant r : temp){
			if(r.getName().equalsIgnoreCase(keyword)) results.add(r);
			else if(r.getCuisines().toLowerCase().indexOf(keyword) != -1) results.add(r);
		}
		
		results.addAll(search.searchWithKeyword(keyword, search.getCoordsByAddress(address)));
		return results;
	}

	@GetMapping(value={"/search.html", "/search"})
	public String search(@RequestParam(name="location", required=true, defaultValue="") String location, @RequestParam(name="keyword", required=false, defaultValue="") String keyword, Model model) {
		ArrayList<Restaurant> results;
		if(keyword.length() <= 0){
			if(location.length() == 5 && location.matches("[0-9]+")) results = findRestaurantByZipcode(location);
			else results = findRestaurantByAddress(location);
			keyword = location;
		}
		else{
			results = findRestaurantByKeyword(location, keyword);
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("results", results);
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "search";
	}

/* Menu */
	public Menu getMenu(String idStr){
		long id = Long.parseLong(idStr.substring(1));
		ArrayList<MenuItem> result = new ArrayList<MenuItem>();
		if(idStr.charAt(0) == '0'){
			if(!currentRestaurant.getMenuIDs().isEmpty()){
				for(long itemID : currentRestaurant.getMenuIDs()){
					result.add(menuItems.findById(itemID).get());
				}
			}
			
		}
		else{ result = search.getMenu(id); }
		return new Menu(result);
	}

	public MenuItem getItem(long id){
		for(MenuItem item : currentMenu.getItems()){
			if(item.getId() == id) return item;
		}
		return null;
	}

	/* View Menu */
	@GetMapping(value={"/menu.html", "/menu"})
	public String menu(@RequestParam(name="id", required=true, defaultValue="") String id, Model model) {
		currentRestaurant = findRestaurantByID(id);
		currentMenu = getMenu(id);
		model.addAttribute("restaurant", currentRestaurant);
		model.addAttribute("menu", currentMenu);
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "menu";
	}

	/* View Menu Item*/
	@GetMapping(value={"/item.html", "/item"})
	public String viewItem(@RequestParam(name="id", required=true, defaultValue="") long id, Model model){
		currentItem = getItem(id);
		model.addAttribute("option", new ItemOption());
		model.addAttribute("item", currentItem);
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "item";
	}

	@PostMapping(value={"/item.html", "/item"})
	public String viewItem(@ModelAttribute ItemOption option, Model model){
		if(!cart.getRestaurantID().equals("-1") && !cart.getRestaurantID().equals(currentRestaurant.getIdStr())) return "redirect:" + "order-reset";
		cart.addItem(new OrderItem(currentItem, option));
		if(cart.getRestaurantID().equals("-1")){
			cart.setRestaurantID(currentRestaurant.getIdStr());
			cart.setRestaurantName(currentRestaurant.getName());
		} 
		return "redirect:" + "item-added";
	}

	/* Item Added to Cart */
	@GetMapping(value={"/item-added.html", "/item-added"})
	public String itemAdded(Model model){
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "item-added";
	}

	@PostMapping(value={"/item-added.html", "/item-added"})
	public String backToMenu(Model model){
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "redirect:" + "menu?id=" + currentRestaurant.getIdStr();
	}

	/* Start new order */
	@GetMapping(value={"/order-reset.html", "/order-reset"})
	public String orderReset(Model model){
		model.addAttribute("restaurant", cart.getRestaurantName());
		return "order-reset";
	}

	@PostMapping(value={"/order-reset.html", "/order-reset"})
	public String confirmOrderReset(Model model){
		cart = new Cart(currentRestaurant.getIdStr(), currentRestaurant.getName());
		return "redirect:" + "menu?id=" + currentRestaurant.getIdStr();
	}

/* Checkout */
	@GetMapping(value={"/cart.html", "/cart"})
	public String cart(Model model){
		if(currentUser.getID() == 1) return "redirect:" + "sign-in";
		model.addAttribute("user", currentUser);
		model.addAttribute("restaurant", currentRestaurant);
		model.addAttribute("cart", cart);
	return "cart";
	}

	@GetMapping(value={"/cart-item.html", "/cart-item"})
	public String cartItem(@RequestParam(name="id", required=true, defaultValue="") long id, Model model){
		currentItem = getItem(id);
		OrderItem o = cart.getItem(id);
		model.addAttribute("item", currentItem);
		model.addAttribute("option", new ItemOption(o));
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
	return "cart-item";
	}

	@PostMapping(value={"/cart-item.html", "/cart-item"})
	public String cartItem(@ModelAttribute ItemOption option, Model model){
		cart.editItem(option);
		return "redirect:" + "cart";
	}

	@RequestMapping(value={"/cart-remove"})
	public String cartRemove(@RequestParam(name="id", required=true, defaultValue="") long id, Model model){
		cart.removeItem(id);
		return "redirect:" + "cart";
	}

	@GetMapping(value={"/checkout.html", "/checkout"})
	public String checkout(String invalid, Model model){
		if(currentUser.getID() == 1) return "redirect:" + "sign-in";
		model.addAttribute("invalid", invalid);
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		model.addAttribute("check", new Checkout());
		return "checkout";
	}

	@PostMapping(value={"/checkout.html", "/checkout"})
	public String checkout(@ModelAttribute Checkout check, Model model){
		CustomerOrder order = new CustomerOrder(currentUser.getID(), currentRestaurant.getIdStr(), check, cart);
		orders.save(order);
		currentRestaurant.addOrder(order);
		restaurants.save(currentRestaurant);
		int i = (int)order.getID();
		return "redirect:" + "checkout-complete?id=" + i;
	}

	@GetMapping(value={"/checkout-complete.html", "/checkout-complete"})
	public String checkoutComplete(@RequestParam(name="id", required=true, defaultValue="") int id, Model model){
		currentRestaurant = null;
		currentMenu = null;
		cart = new Cart();
		model.addAttribute("order", orders.findById(id).get());
		return "checkout-complete";
	}


/* Register */
	public boolean validUsername(User user){
		for(User u : users.findAll()){
			String username = u.getUsername();
			if(username != null && user.getUsername().equals(username)) return false;
		}
		return true;
	}

	/* Register Customer*/
	@GetMapping(value={"/register.html", "register"})
	public String register(@RequestParam(name="invalid", required=false, defaultValue=" ") String invalid, Model model){
		if(currentUser.getRole() != 0) return "redirect:" + "sign-out";		// if signed in, redirect to sign out
		model.addAttribute("invalid", invalid);
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping(value={"/register.html", "register"})
	public String addUser(@ModelAttribute User user, Model model){
		if(validUsername(user) == false) return "redirect:" + "register?invalid=username";	// if username is invalid, stay on register page
		user.setRole(1);
		user.setRestaurantID("");
		users.save(user);
		return "register-complete";
	}

	/* Register Restaurant */
	@GetMapping(value={"/register-restaurant.html", "/register-restaurant"})
	public String registerRestaurant(@RequestParam(name="invalid", required=false, defaultValue=" ") String invalid, Model model){
		if(currentUser.getRole() != 0) return "redirect:" + "sign-out";		// if signed in, redirect to sign out
		model.addAttribute("invalid", invalid);
		model.addAttribute("user", new User());
		model.addAttribute("restaurant", new Restaurant());
		return "register-restaurant";
	}

	@PostMapping(value={"/register-restaurant.html", "/register-restaurant"})
	public String addRestaurant(@ModelAttribute User user, @ModelAttribute Restaurant restaurant, Model model){
		if(validUsername(user) == false) return "redirect:" + "register-restaurant?invalid=username";	// if username is invalid, stay on register page
		restaurants.save(restaurant);
		restaurant.setIDStr("0" + restaurant.getIdLong());
		user.setRole(2);
		user.setRestaurantID(restaurant.getIdStr());
		users.save(user);
		return "register-complete";
	}

/* Sign In */
	@GetMapping(value={"/sign-in.html", "/sign-in"})
	public String signin(@RequestParam(name="invalid", required=false, defaultValue=" ") String invalid, Model model){
		if(currentUser.getRole() != 0) return "redirect:" + "sign-out";
		model.addAttribute("invalid", invalid);
		model.addAttribute("user", new User());
		return "sign-in";
	}

	@PostMapping(value={"/sign-in.html", "/sign-in"})
	public String checkSignin(@ModelAttribute User user, Model model){
		String username = user.getUsername();
		String pass = user.getPassword();
		for(User u : users.findAll()){
			if(u.getUsername().equals(username)){	// Check for correct username
				if(u.getPassword().equals(pass)){ 	// Check for correct password
					currentUser = u;
					if(u.getRole() == 2){	// if user is a restaurant owner
						currentRestaurant = findRestaurantByID(u.getRestaurantID());
						currentMenu = getMenu(currentRestaurant.getIdStr());
					} 
					return "redirect:" + "index"; 
				}
				else{ return "redirect:" + "sign-in?invalid=password"; }
			}
		}
		return "redirect:" + "sign-in?invalid=username";	// Username not found
	}

/* Sign Out */
	@GetMapping(value={"/sign-out.html", "/sign-out"})
	public String signoutPage(Model model){
		model.addAttribute("user", currentUser);
		return "sign-out";
	}

	@RequestMapping(value={"/sign-out-action"})
	public String signoutAction(Model model){
		currentUser = users.findById(1).get();
		cart = new Cart();
		currentRestaurant = null;
		currentMenu = null;
		return "redirect:" + "index";
	}

/* Customer */
	/* Account */
	@GetMapping(value={"/account.html", "/account"})
	public String account(Model model){
		if(currentUser.getRole() == 0) return "redirect:" + "sign-in";	// if user is a guest, prompt to sign in
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "account";
	}

	@PostMapping(value={"/account.html", "/account"})
	public String account(@ModelAttribute User user, Model model){
		if(!user.getUsername().equals(currentUser.getUsername()) && validUsername(user) == false) return "redirect:" + "account?invalid=username";
		currentUser.update(user);
		users.save(currentUser);
		return "redirect:" + "info-updated";
	}

	@GetMapping(value={"/info-updated.html", "/info-updated"})
	public String infoUpdated(Model model){
		model.addAttribute("user", currentUser);
		model.addAttribute("cart", cart);
		return "info-updated";
	}

	/* Order History */
	@GetMapping(value={"/order-history.html", "/order-history"})
	public String orderHistory(Model model){
		ArrayList<CustomerOrder> pastOrders = new ArrayList<CustomerOrder>();
		for(CustomerOrder o : orders.findAll()){
			if(o.getUserID() == currentUser.getID()) pastOrders.add(o);
		}
		model.addAttribute("cart", cart);
		model.addAttribute("user", currentUser);
		model.addAttribute("orders", pastOrders);
		return "order-history";
	}

/* Restaurant Owner */
	/* Index */
	@GetMapping(value={"/restaurant-index.html", "/restaurant-index"})
	public String restaurantIndex(Model model){
		model.addAttribute("restaurant", currentRestaurant);
		return "restaurant-index";
	}

	/* Edit Menu*/
	@GetMapping(value={"/menu-edit.html", "/menu-edit"})
	public String editMenu(Model model){
		model.addAttribute("menu", currentMenu);
		return "menu-edit";
	}

	/* Edit Menu Item */
	@GetMapping(value={"/item-add.html", "/item-add"})
	public String addItem(Model model){
		model.addAttribute("item", new MenuItem());
		return "item-add";
	}

	@PostMapping(value={"/item-add.html", "/item-add"})
	public String addItem(@ModelAttribute MenuItem item, Model model){
		menuItems.save(item);
		currentMenu.addItem(item);
		currentRestaurant.addItem(item.getId());
		restaurants.save(currentRestaurant);
		return "redirect:" + "item-edit?id=" + item.getId();
	}

	@GetMapping(value={"/item-edit.html", "/item-edit"})
	public String editItem(@RequestParam(name="id", required=true, defaultValue="") long id, Model model){
		currentItem = menuItems.findById(id).get();
		model.addAttribute("item", currentItem);
		return "item-edit";
	}

	@PostMapping(value={"/item-edit.html", "/item-edit"})
	public String editItem(@ModelAttribute MenuItem item, Model model){
		currentItem.editItem(item);
		currentMenu.editItem(currentItem);
		menuItems.save(currentItem);
		return "redirect:" + "menu-edit";
	}

	@RequestMapping(value={"/item-remove"})
	public String deleteItem(@RequestParam(name="id", required=true, defaultValue="") long id, Model model){
		currentMenu.removeItem(getItem(id));
		currentRestaurant.removeItem(id);
		restaurants.save(currentRestaurant);
		return "redirect:" + "menu-edit";
	}


	/* Edit Menu Item Options */
	@GetMapping(value={"/option-add", "/option-add.html"})
	public String addOption(Model model){
		model.addAttribute("itemOption", new ItemOption());
		return "option-add";
	}

	@PostMapping(value={"/option-add", "/option-add.html"})
	public String addOption(@ModelAttribute MenuItem item, @ModelAttribute ItemOption itemOption, Model model){
		currentItem.addOption(itemOption.getOption(), itemOption.getPrice());
		currentMenu.editItem(currentItem);
		menuItems.save(currentItem);
		return "redirect:" + "item-edit?id=" + currentItem.getId();
	}


	@GetMapping(value={"/option-edit.html", "/option-edit"})
	public String editOption(@RequestParam(name="id", required=true, defaultValue="") int id, Model model){
		model.addAttribute("item", currentItem);
		model.addAttribute("option", new ItemOption(id, currentItem.getOptions().get(id), currentItem.getPrices().get(id)));
		return "option-edit";
	}

	@PostMapping(value={"/option-edit.html", "/option-edit"})
	public String editOption(@ModelAttribute MenuItem item, @ModelAttribute ItemOption option, Model model){
		currentItem.editOption(option);
		currentMenu.editItem(currentItem);
		menuItems.save(currentItem);
		return "redirect:" + "item-edit?id=" + currentItem.getId();
	}

	@RequestMapping(value={"/option-remove"})
	public String deleteOption(@RequestParam(name="id", required=true, defaultValue="") int id, Model model){
		MenuItem item = menuItems.findById(currentItem.getId()).get();
		item.removeOption(id);
		currentMenu.editItem(item);
		menuItems.save(item);
		return "redirect:" + "item-edit?id=" + currentItem.getId();
	}

	/* Orders */
	@GetMapping(value={"/restaurant-orders.html", "/restaurant-orders"})
	public String orders(Model model){
		ArrayList<CustomerOrder> restaurantOrders = new ArrayList<CustomerOrder>();
		for(CustomerOrder o : orders.findAll()){
			if(o.getRestaurantID().equals(currentRestaurant.getIdStr())) restaurantOrders.add(o);
		}
		model.addAttribute("orders", restaurantOrders);
		return "restaurant-orders";
	}

	/* Restaurant Info */
	@GetMapping(value={"/restaurant-info.html", "/restaurant-info"})
	public String restaurantInfo(Model model){
		model.addAttribute("restaurant", currentRestaurant);
		return "restaurant-info";
	}

	@PostMapping(value={"/restaurant-info.html", "/restaurant-info"})
	public String restaurantInfo(@ModelAttribute Restaurant restaurant, Model model){
		currentRestaurant.updateRestaurant(restaurant);
		restaurants.save(currentRestaurant);
		return "redirect:" + "restaurant-info";
	}

	/* Invoice */
	@GetMapping(value={"/invoice.html", "/invoice"})
	public String invoice(@RequestParam(name="id", required=true, defaultValue="") int id, String invalid, Model model){
		CustomerOrder order = orders.findById(id).get();
		model.addAttribute("order", order);
		return "invoice";
	}

}