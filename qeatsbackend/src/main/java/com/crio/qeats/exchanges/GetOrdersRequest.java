package com.crio.qeats.exchanges;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersRequest {

  @NotNull
  private String restaurantName;

  @NotNull
  private Integer cartTotal;

}
