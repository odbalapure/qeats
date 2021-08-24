package com.crio.qeats.exchanges;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetCartRequest {

  @NotNull
  private String userId;

}
