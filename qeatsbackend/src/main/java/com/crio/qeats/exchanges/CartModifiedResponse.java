package com.crio.qeats.exchanges;

import com.crio.qeats.dto.Cart;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartModifiedResponse {
  @NotNull
  Cart cart;
}
