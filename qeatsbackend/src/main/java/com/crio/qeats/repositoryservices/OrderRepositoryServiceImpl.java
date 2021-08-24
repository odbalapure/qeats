
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Order;
import com.crio.qeats.models.OrderEntity;
import com.crio.qeats.models.RestaurantEntity;
import com.crio.qeats.repositories.OrderRepository;
import com.crio.qeats.repositories.RestaurantRepository;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRepositoryServiceImpl implements OrderRepositoryService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Override
  public Order placeOrder(Cart cart) {  
    System.out.println("Cart in place order: ");
    System.out.println(cart);

    Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository
        .getRestaurantById(cart.getRestaurantId());
    String restaurantName = optionalRestaurantEntity.get().getName();
    
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setRestaurantName(restaurantName);
    orderEntity.setTotal(cart.getTotal());
    orderEntity.setCartId(cart.getCartId());

    System.out.println("Order to be saved: " + orderEntity);
    orderRepository.save(orderEntity);

    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(orderEntity, Order.class);
  }

}