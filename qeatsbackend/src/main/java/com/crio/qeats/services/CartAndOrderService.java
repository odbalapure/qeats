
package com.crio.qeats.services;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Order;
import com.crio.qeats.exceptions.EmptyCartException;
import com.crio.qeats.exceptions.ItemNotFromSameRestaurantException;
import com.crio.qeats.exchanges.CartModifiedResponse;

public interface CartAndOrderService {

  Cart findOrCreateCart(String userId);

  /**
   * Add item to the given cart.
   *  - All items added should be from same restaurant
   *  - If the above constraint is not satisfied, throw ItemNotFromSameRestaurantException exception
   * @param itemId - id of the item to be added
   * @param cartId - id of the cart where item should be added
   * @param restaurantId - id of the restaurant where the given item comes from
   * @return - return - CartModifiedResponse
   * @throws ItemNotFromSameRestaurantException - when item to be added comes from 
   *     different restaurant. You should set cartResponseType to 102(ITEM_NOT_FROM_SAME_RESTAURANT)
   */
  CartModifiedResponse addItemToCart(String itemId, String cartId, String restaurantId)
      throws ItemNotFromSameRestaurantException;

  /**
   * Remove item from the given cart.
   * @param itemId - id of the item to be removed
   * @param cartId - id of the cart where item should be removed
   * @param restaurantId - id of the restaurant where the given item comes from
   * @return - return - CartModifiedResponse, set cartResponseType to 0
   */
  CartModifiedResponse removeItemFromCart(String itemId, String cartId, String restaurantId);

  Order postOrder(String cartId) throws EmptyCartException;

  String clearCart(String cartId);

  Cart findCartById(String cartId);

}