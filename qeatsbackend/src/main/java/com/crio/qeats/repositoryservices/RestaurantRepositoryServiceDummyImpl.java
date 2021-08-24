// package com.crio.qeats.repositoryservices;

// import com.crio.qeats.dto.Restaurant;
// import com.crio.qeats.utils.FixtureHelpers;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.io.IOException;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.concurrent.Future;
// import java.util.concurrent.ThreadLocalRandom;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
// import org.springframework.stereotype.Repository;
// import org.springframework.stereotype.Service;

// public class RestaurantRepositoryServiceDummyImpl implements RestaurantRepositoryService {
//   private static final String FIXTURES = "fixtures/exchanges";
//   private ObjectMapper objectMapper = new ObjectMapper();

//   private List<Restaurant> loadRestaurantsDuringNormalHours() throws IOException {
//     String fixture =
//         FixtureHelpers.fixture(FIXTURES + "/normal_hours_list_of_restaurants.json");

//     return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
//     });
//   }

//   @Override
//   public List<Restaurant> findAllRestaurantsCloseBy(Double latitude, Double longitude,
//       LocalTime currentTime, Double servingRadiusInKms) {
//     List<Restaurant> restaurantList = new ArrayList<>();
//     try {
//       restaurantList = loadRestaurantsDuringNormalHours();
//     } catch (IOException e) {
//       e.printStackTrace();
//     }
//     for (Restaurant restaurant : restaurantList) {
//       restaurant.setLatitude(latitude + ThreadLocalRandom.current().nextDouble(0.000001, 0.2));
//       restaurant.setLongitude(longitude + ThreadLocalRandom.current().nextDouble(0.000001, 0.2));
//     }
//     return restaurantList;
//   }

//   public List<Restaurant> findRestaurantsByName(Double latitude, Double longitude,
//       String searchString, LocalTime currentTime, Double servingRadiusInKms) {
//     return null;
//   }

//   public List<Restaurant> findRestaurantsByAttributes(
//       Double latitude, Double longitude, String searchString,
//       LocalTime currentTime, Double servingRadiusInKms) {
//     return null;
//   }

//   public List<Restaurant> findRestaurantsByItemName(Double latitude, Double longitude,
//       String searchString, LocalTime currentTime, Double servingRadiusInKms) {
//     return null;
//   }

//   public List<Restaurant> findRestaurantsByItemAttributes(Double latitude, Double longitude,
//       String searchString, LocalTime currentTime, Double servingRadiusInKms) {
//     return null;
//   }

// }
