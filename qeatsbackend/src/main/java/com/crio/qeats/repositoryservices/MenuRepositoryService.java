
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Menu;
import org.springframework.stereotype.Service;

@Service
public interface MenuRepositoryService {

  /**
   * Return the restaurant menu.
   * @param restaurantId id of the restaurant
   * @return the restaurant's menu
   */
  Menu findMenu(String restaurantId);

}
