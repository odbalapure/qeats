/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Java class that maps to Mongo collection.
@Data
@Document(collection = "restaurants")
@NoArgsConstructor
public class RestaurantEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @NotNull
  private String restaurantId;

  @NotNull
  private String name;

  @NotNull
  private String city;

  @NotNull
  private String imageUrl;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  @NotNull
  private String opensAt;

  @NotNull
  private String closesAt;

  @NotNull
  private List<String> attributes = new ArrayList<>();

}

