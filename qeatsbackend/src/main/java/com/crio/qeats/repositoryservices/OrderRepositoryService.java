/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Order;
import com.crio.qeats.dto.Restaurant;

public interface OrderRepositoryService {


  /**
   * TODO: CRIO_TASK_MODULE_MENUAPI - Implement placeOrder.
   * Place order based on the cart.
   * @param cart - cart to use for placing order.
   * @return return - the order that was just placed.
   */
  Order placeOrder(Cart cart);

}
