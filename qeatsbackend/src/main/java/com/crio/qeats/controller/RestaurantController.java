
package com.crio.qeats.controller;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Item;
import com.crio.qeats.dto.Order;
import com.crio.qeats.exchanges.AddCartRequest;
import com.crio.qeats.exchanges.CartModifiedResponse;
import com.crio.qeats.exchanges.DeleteCartRequest;
import com.crio.qeats.exchanges.GetCartRequest;
import com.crio.qeats.exchanges.GetMenuResponse;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.OrderRepositoryService;
import com.crio.qeats.services.CartAndOrderService;
import com.crio.qeats.services.MenuService;
import com.crio.qeats.services.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class RestaurantController {

  public static final String RESTAURANT_API_ENDPOINT = "/qeats/v1";
  public static final String RESTAURANTS_API = "/restaurants";
  public static final String MENU_API = "/menu";
  public static final String CART_API = "/cart";
  public static final String CART_ITEM_API = "/cart/item";
  public static final String CART_CLEAR_API = "/cart/clear";
  public static final String POST_ORDER_API = "/order";
  public static final String GET_ORDERS_API = "/orders";
  private static final String CONFIRM_ORDER = "/confirm";
  private static final String PLACE_ORDER = "/placeorder";
  private static final String ADD_CART_ITEM = "/additem";
  private static final String REMOVE_CART_ITEM = "/deleteitem";
  private static final String CLEAR_CART_API_URI = RESTAURANT_API_ENDPOINT + CART_CLEAR_API;


  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private CartAndOrderService cartAndOrderService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private OrderRepositoryService orderRepositoryService;

  // dummy controller for testing
  @GetMapping("/hello")
  public String hello() {
    return "Hello World...";
  }

  @GetMapping(RESTAURANT_API_ENDPOINT + RESTAURANTS_API)
  public ResponseEntity<GetRestaurantsResponse> getRestaurants(
      @Valid GetRestaurantsRequest getRestaurantsRequest) throws IOException {

    GetRestaurantsResponse getRestaurantsResponse;

    // throw 400 error if lat/long values are incorrect
    if (!(getRestaurantsRequest.getLatitude() >= -90.00 
         && getRestaurantsRequest.getLongitude() <= 90.00)
         || getRestaurantsRequest.getLatitude() == 0.0) {
      log.error("Invalid latitude value");
      return new ResponseEntity<GetRestaurantsResponse>(HttpStatus.BAD_REQUEST);
    } else if (!(getRestaurantsRequest.getLatitude() >= -180.00 
         && getRestaurantsRequest.getLongitude() <= 180.00)
         || getRestaurantsRequest.getLongitude() == 0.0) {
      log.error("Invalid longitude value");
      return new ResponseEntity<GetRestaurantsResponse>(HttpStatus.BAD_REQUEST);
    }

    // return results according to the search parameter
    if (getRestaurantsRequest.getSearchFor() != null) {
      System.out.println("searchFor parameter NOT empty!");
      getRestaurantsResponse = restaurantService
        .findRestaurantsBySearchQuery(getRestaurantsRequest, LocalTime.now());
      return ResponseEntity.ok().body(getRestaurantsResponse);
    } 

    getRestaurantsResponse = restaurantService
         .findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());
    
    // remove non ascii characters 
    ObjectMapper objectMapper = new ObjectMapper();
    String responseString = objectMapper.writeValueAsString(getRestaurantsResponse);
    responseString = responseString.replaceAll("[^\\x00-\\x7F]", "");
    getRestaurantsResponse = objectMapper.readValue(responseString, GetRestaurantsResponse.class);
  
    return ResponseEntity.ok().body(getRestaurantsResponse);
  }
  
  @GetMapping(RESTAURANT_API_ENDPOINT + MENU_API + "/{restaurantId}")
  public ResponseEntity<GetMenuResponse> getRestaurantMenu(@PathVariable String restaurantId) {
    GetMenuResponse getMenuResponse = menuService.findMenu(restaurantId);

    if (getMenuResponse == null) {
      // if no matching restaurant is found 
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(getMenuResponse, HttpStatus.OK);
  }  

  @GetMapping(RESTAURANT_API_ENDPOINT + CART_API)
  public ResponseEntity<List<String>> getCart(@Valid GetCartRequest getCartRequest) {
    try {
      String userId = getCartRequest.getUserId();
      Cart cart = cartAndOrderService.findOrCreateCart(userId);

      if (cart.getUserId() == null) {
        // create cart if cart with the i/p userId is NOT present
        System.out.println("Cart not found!");
        Cart createCart = cartAndOrderService.findOrCreateCart(userId);
        System.out.println("Creating cart now: " + createCart);
        return new ResponseEntity<>(new ArrayList<String>(), HttpStatus.CREATED);
      } 

      // return if cart with the i/p userId is present 
      System.out.println("Returning cart for given userId:" + cart);
      List<String> cartItems = new ArrayList<>();
      for (Item item : cart.getItems()) {
        cartItems.add(item.getName());
      }

      return new ResponseEntity<>(cartItems, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong while fetching cart...");
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping(RESTAURANT_API_ENDPOINT + CART_API + ADD_CART_ITEM)
  public ResponseEntity<CartModifiedResponse> addItem(@RequestBody AddCartRequest addCartRequest) {
    
    System.out.println("Request params: " + addCartRequest);

    CartModifiedResponse cartModifiedResponse = cartAndOrderService
          .addItemToCart(addCartRequest.getItemId(), addCartRequest.getCartId(),
           addCartRequest.getRestaurantId());    

    return new ResponseEntity<>(cartModifiedResponse, HttpStatus.OK);
  }

  @PutMapping(RESTAURANT_API_ENDPOINT + CART_API + REMOVE_CART_ITEM)
  public ResponseEntity<CartModifiedResponse> deleteItem(@RequestBody 
      DeleteCartRequest deleteCartRequest) {
    System.out.println("Request params: " + deleteCartRequest);
    CartModifiedResponse cartModifiedResponse = cartAndOrderService
          .removeItemFromCart(deleteCartRequest.getItemId(), deleteCartRequest.getCartId(),
           deleteCartRequest.getRestaurantId());    

    return new ResponseEntity<>(cartModifiedResponse, HttpStatus.OK);
  }

  @PostMapping(RESTAURANT_API_ENDPOINT + PLACE_ORDER + "/{cartId}")
  public ResponseEntity<Order> placeOrder(@PathVariable String cartId) {
    System.out.println("User id for place order: " + cartId);
    
    Cart cart = cartAndOrderService.findCartById(cartId);
    System.out.println("Cart fetched for placeorder: " + cart);

    Order order = orderRepositoryService.placeOrder(cart);

    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @GetMapping(RESTAURANT_API_ENDPOINT + CONFIRM_ORDER + "/{cartId}")
  public ResponseEntity<String> confirmOrder(@PathVariable String cartId) {
    return new ResponseEntity<>("Order has been placed for cart id: " + cartId, HttpStatus.CREATED);
  } 

  @PutMapping(CLEAR_CART_API_URI + "/{cartId}")
  public ResponseEntity<String> clearCart(@PathVariable String cartId)  {
    String clearCartMsg = cartAndOrderService.clearCart(cartId);

    return new ResponseEntity<>(clearCartMsg, HttpStatus.NO_CONTENT);
  } 
  
}

