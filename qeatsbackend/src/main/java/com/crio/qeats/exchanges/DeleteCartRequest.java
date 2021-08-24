package com.crio.qeats.exchanges;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Item;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCartRequest {
  
  @NotNull
  private String cartId;

  @NotNull
  private String itemId;

  @NotNull
  private String restaurantId;

}
