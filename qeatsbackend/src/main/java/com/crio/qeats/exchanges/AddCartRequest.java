package com.crio.qeats.exchanges;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCartRequest {

  @NotNull
  private String cartId;

  @NotNull
  private String itemId;

  @NotNull
  private String restaurantId;

}