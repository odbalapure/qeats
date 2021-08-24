
package com.crio.qeats.services;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Item;
import com.crio.qeats.dto.Order;
import com.crio.qeats.exceptions.EmptyCartException;
import com.crio.qeats.exceptions.ItemNotFromSameRestaurantException;
import com.crio.qeats.exchanges.CartModifiedResponse;
import com.crio.qeats.repositoryservices.CartRepositoryService;
import com.crio.qeats.repositoryservices.OrderRepositoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartAndOrderServiceImpl implements CartAndOrderService {

  @Autowired
  private CartRepositoryService cartRepositoryService;

  @Autowired
  private OrderRepositoryService orderRepositoryService;

  @Autowired
  private MenuService menuService;

  public CartAndOrderServiceImpl() {
  }

  @Override
  public Order postOrder(String cartId) throws EmptyCartException {
    Cart cart = cartRepositoryService.findCartByCartId(cartId);
    Order placedOrder = orderRepositoryService.placeOrder(cart);

    return placedOrder;
  }

  @Override
  public Cart findOrCreateCart(String userId) {
    Optional<Cart> optionalCart = cartRepositoryService.findCartByUserId(userId);

    if (optionalCart.isPresent()) {
      Cart cart = optionalCart.get();
      return cart;
    } 

    Integer id = 1; 
    List<Cart> cartList = cartRepositoryService.getAllCarts();
    System.out.println("Current cart size: " + cartList.size());

    if (cartList.size() > 0) {
      id = Integer.parseInt(cartList.get(cartList.size() - 1).getCartId());
      id += 1;
    }  

    Cart createCart = new Cart();
    createCart.setCartId(String.valueOf(id));
    createCart.setRestaurantId("");
    createCart.setUserId(userId);
    createCart.setItems(new ArrayList<Item>());
    createCart.setTotal(0);

    String createdCartUserId = cartRepositoryService.createCart(createCart);
    System.out.println("Cart created with userId:  " + createdCartUserId);

    return createCart;
  }

  @Override
  public Cart findCartById(String cartId) {
    Cart cart = cartRepositoryService.findCartByCartId(cartId);

    if (cart.getCartId() != null) {
      return cart;
    }

    return new Cart();
  }

  @Override
  public CartModifiedResponse addItemToCart(String itemId, String cartId, String restaurantId)
      throws ItemNotFromSameRestaurantException {
    Item item = menuService.findItem(itemId, restaurantId);
    Cart cart = cartRepositoryService.addItem(item, cartId, restaurantId);
    
    return new CartModifiedResponse(cart);
  }

  @Override
  public CartModifiedResponse removeItemFromCart(String itemId, String cartId, 
      String restaurantId) {
    Item item = menuService.findItem(itemId, restaurantId);
    Cart cart = cartRepositoryService.removeItem(item, cartId, restaurantId);
    
    return new CartModifiedResponse(cart);
  }

  @Override
  public String clearCart(String cartId) {
    return cartRepositoryService.clearCart(cartId);
  }

}

