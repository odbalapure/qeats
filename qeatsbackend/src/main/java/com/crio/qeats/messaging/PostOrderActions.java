
package com.crio.qeats.messaging;

import com.crio.qeats.dto.Order;

public interface PostOrderActions {
  void execute(Order order);
}
