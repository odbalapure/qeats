/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositories;

import com.crio.qeats.models.MenuEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MenuRepository extends MongoRepository<MenuEntity, String> {

  @Query(value = "{ restaurantId : ?0 }")
  Optional<MenuEntity> findMenuByRestaurantId(String restaurantId);

  Optional<List<MenuEntity>> findMenusByItemsItemIdIn(List<String> itemIdList);

}