
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Item;
import com.crio.qeats.exceptions.CartNotFoundException;
import com.crio.qeats.models.CartEntity;
import com.crio.qeats.repositories.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CartRepositoryServiceImpl implements CartRepositoryService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Cart> getAllCarts() {
    List<CartEntity> cartEntities = cartRepository.findAll();
    List<Cart> cartList = new ArrayList<>();

    cartEntities.forEach(cartEntity -> {
      cartList.add(modelMapper.map(cartEntity, Cart.class));
    });

    return cartList;
  }

  @Override
  public String createCart(Cart cart) {
    CartEntity cartEntity = modelMapper.map(cart, CartEntity.class);
    System.out.println("Creating a cart: " + cartEntity);
    cartRepository.save(cartEntity);

    return cart.getCartId();
  }  

  @Override
  public Optional<Cart> findCartByUserId(String userId) {
    Optional<CartEntity> optionalCartEntity = cartRepository.getCartByUserName(userId);

    if (optionalCartEntity.isPresent()) {
      CartEntity cartEntity = optionalCartEntity.get();

      return Optional.of(modelMapper.map(cartEntity, Cart.class));
    }

    return Optional.empty();
  }  

  @Override
  public Cart findCartByCartId(String cartId) throws CartNotFoundException {
    Optional<CartEntity> optionalCartEntity = cartRepository.getCartByCartId(cartId);

    if (optionalCartEntity.isPresent()) {
      CartEntity cartEntity = optionalCartEntity.get();
      Cart cart = modelMapper.map(cartEntity, Cart.class);
      return cart;
    }

    System.out.println("Cart not found...returning empty cart object");
    return new Cart();
  } 

  @Override
  public Cart addItem(Item item, String cartId, String restaurantId) throws CartNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("cartId").is(cartId));

    Update update = new Update();
    
    // set the restaurant id for empty cart object
    update.set("restaurantId", restaurantId);

    CartEntity cartEntity = cartRepository.getCartByCartId(cartId).get();
    System.out.println(cartEntity);
    List<Item> items = cartEntity.getItems();

    Integer total = 0;
    for (Item ele : items) {
      System.out.println("Item price: " + ele.getPrice());
      total += ele.getPrice();
    }

    total += item.getPrice();

    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    System.out.println("Item list: " + items);
    System.out.println("Total price: " + total);
    items.add(item);

    // update the item list of a given cart 
    update.set("items", items);
    // update the total cost of cart items
    update.set("total", total);

    mongoTemplate.upsert(query, update, CartEntity.class);

    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(cartEntity, Cart.class);
  } 

  @Override
  public Cart removeItem(Item item, String cartId, String restaurantId) throws 
      CartNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("cartId").is(cartId));

    Update update = new Update();
    
    // set the restaurant id for empty cart object
    update.set("restaurantId", restaurantId);

    /* get cart and remove the item specified */
    CartEntity cartEntity = cartRepository.getCartByCartId(cartId).get();
    System.out.println(cartEntity);
    List<Item> items = cartEntity.getItems();

    Integer total = 0;
    if (items.size() == 0) {
      total = 0;
    }

    for (Item ele : items) {
      System.out.println("Item price removeItem: " + ele.getPrice());
      total += ele.getPrice();
    }

    total -= item.getPrice();
    items.remove(item);
    /* Also decrement the total cart price */

    System.out.println("####################################################");
    System.out.println("Item list: " + items);
    System.out.println("Total price: " + total);


    // update the item list of a given cart 
    update.set("items", items);
    // update the total cost of cart items
    update.set("total", total);

    mongoTemplate.upsert(query, update, CartEntity.class);

    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(cartEntity, Cart.class);
  }

  @Override
  public String clearCart(String cartId) throws CartNotFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where("cartId").is(cartId));

    Update update = new Update();
    
    CartEntity cartEntity = cartRepository.getCartByCartId(cartId).get();
    System.out.println(cartEntity);
    List<Item> items = cartEntity.getItems();

    // clear the items list in the cart 
    items.clear();
    // update the item list of a given cart  
    update.set("items", items);
    update.set("total", 0);

    mongoTemplate.upsert(query, update, CartEntity.class);

    return "Cart is cleared!";
  }
  
}

