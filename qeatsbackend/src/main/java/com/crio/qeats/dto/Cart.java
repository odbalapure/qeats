
package com.crio.qeats.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

  @NotNull
  private String cartId;

  @NotNull
  private String restaurantId;

  @NotNull
  private String userId;

  @NotNull
  private List<Item> items;

  @NotNull
  private int total;

}