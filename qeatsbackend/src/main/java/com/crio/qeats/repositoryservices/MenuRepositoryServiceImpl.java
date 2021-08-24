
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Menu;
import com.crio.qeats.models.MenuEntity;
import com.crio.qeats.repositories.MenuRepository;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuRepositoryServiceImpl implements MenuRepositoryService {

  @Autowired
  private MenuRepository menuRepository;

  @Override
  public Menu findMenu(String restaurantId) {
    Optional<MenuEntity> optionalMenuEntity = menuRepository.findMenuByRestaurantId(restaurantId);

    if (optionalMenuEntity.isPresent()) {
      MenuEntity menuEntity = optionalMenuEntity.get();

      ModelMapper modelMapper = new ModelMapper();
      Menu menu = modelMapper.map(menuEntity, Menu.class);

      return menu;
    }

    return null;
  }

}
