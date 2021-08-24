/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositories;

import com.crio.qeats.models.CartEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CartRepository extends MongoRepository<CartEntity, String> {

  // get cart using userId 
  @Query("{'userId': '?0'}")
  Optional<CartEntity> getCartByUserName(String userId);
  
  // get cart using cartId
  @Query("{'cartId': '?0'}")
  Optional<CartEntity> getCartByCartId(String userId);
  
}
