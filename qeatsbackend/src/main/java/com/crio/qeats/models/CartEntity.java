
package com.crio.qeats.models;

import com.crio.qeats.dto.Item;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "carts")
@NoArgsConstructor
public class CartEntity {

  @NotNull
  private String cartId;

  @NotNull
  private String restaurantId;

  @NotNull
  private String userId;

  @NotNull
  private List<Item> items;

  @NotNull
  private int total = 0;

  public void addItem(Item item) {
    items.add(item);
    total += item.getPrice();
  }

  public void removeItem(Item item) {
    boolean removed = items.remove(item);

    if (removed) {
      total -= item.getPrice();
    }
  }

  public void clearCart() {
    if (items.size() > 0) {
      items.clear();
      total = 0;
    }
  }
}
