package com.crio.qeats.exchanges;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PostOrderRequest {

  @NotNull
  private String cartId;

}