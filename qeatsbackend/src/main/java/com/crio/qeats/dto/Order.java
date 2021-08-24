
package com.crio.qeats.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

  @NotNull
  private String cartId;

  @NotNull
  private String restaurantName;

  @NotNull
  private int total;

}