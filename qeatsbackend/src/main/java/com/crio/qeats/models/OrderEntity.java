
package com.crio.qeats.models;

import com.crio.qeats.dto.Item;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "orders")
@NoArgsConstructor
public class OrderEntity {

  @NotNull
  private String cartId;

  @NotNull
  private String restaurantName;

  @NotNull
  private int total = 0;

}
