
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class RestaurantTest {

  @Test
  public void serializeAndDeserializeRestaurantJson() throws IOException, JSONException {
    final String jsonString =
        "{\n"
            + "  \"restaurantId\": \"10\",\n"
            + "  \"name\": \"A2B\",\n"
            + "  \"city\": \"Hsr Layout\",\n"
            + "  \"imageUrl\": \"www.google.com\",\n"
            + "  \"latitude\": 20.027,\n"
            + "  \"longitude\": 30.0,\n"
            + "  \"opensAt\": \"18:00\",\n"
            + "  \"closesAt\": \"23:00\",\n"
            + "  \"attributes\": [\n"
            + "    \"Tamil\",\n"
            + "    \"South Indian\"\n"
            + "  ]\n"
            + "}";

    Restaurant restaurant = new Restaurant();
    restaurant = new ObjectMapper().readValue(jsonString, Restaurant.class);

    String actualJsonString = "";
    actualJsonString = new ObjectMapper().writeValueAsString(restaurant);
    JSONAssert.assertEquals(jsonString, actualJsonString, true);
  }
}
