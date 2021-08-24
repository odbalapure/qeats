
package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest 
      getRestaurantsRequest,
      LocalTime currentTime) {
    List<Restaurant> restaurantList = new ArrayList<>();

    if (isPeakHour(currentTime)) {
      restaurantList = restaurantRepositoryService.findAllRestaurantsCloseBy(
          getRestaurantsRequest.getLatitude(),
          getRestaurantsRequest.getLongitude(), currentTime, peakHoursServingRadiusInKms);
    } else {
      restaurantList = restaurantRepositoryService.findAllRestaurantsCloseBy(
          getRestaurantsRequest.getLatitude(),
          getRestaurantsRequest.getLongitude(), currentTime, normalHoursServingRadiusInKms);
    }

    return new GetRestaurantsResponse(restaurantList);
  }

  @Override
  public GetRestaurantsResponse findRestaurantsBySearchQuery(GetRestaurantsRequest 
      getRestaurantsRequest,
      LocalTime currentTime) {
    Double servingRadiusInKms = isPeakHour(currentTime) ? peakHoursServingRadiusInKms 
        : normalHoursServingRadiusInKms;

    String searchFor = getRestaurantsRequest.getSearchFor();
    List<List<Restaurant>> listOfRestaurantLists = new ArrayList<>();

    if (!searchFor.isEmpty()) {
      listOfRestaurantLists.add(restaurantRepositoryService.findRestaurantsByName(
          getRestaurantsRequest.getLatitude(),
          getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

      listOfRestaurantLists
          .add(restaurantRepositoryService.findRestaurantsByAttributes(
              getRestaurantsRequest.getLatitude(),
              getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

      listOfRestaurantLists
          .add(restaurantRepositoryService.findRestaurantsByItemName(
              getRestaurantsRequest.getLatitude(),
              getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

      listOfRestaurantLists
          .add(restaurantRepositoryService.findRestaurantsByItemAttributes(
              getRestaurantsRequest.getLatitude(),
              getRestaurantsRequest.getLongitude(), searchFor, currentTime, servingRadiusInKms));

      if (servingRadiusInKms == 3.0) {
        int total = 0;
        for (List<Restaurant> sublist : listOfRestaurantLists) {
          total += sublist.size();
        }
        System.out.println("Length during peak hours: " + total);
      } else if (servingRadiusInKms == 5.0) {
        int total = 0;
        for (List<Restaurant> sublist : listOfRestaurantLists) {
          total += sublist.size();
        }
        System.out.println("Length during normal hours: " + total);
      }

      Set<String> restaurantSet = new HashSet<>();
      List<Restaurant> restaurantList = new ArrayList<>();
      for (List<Restaurant> restoList : listOfRestaurantLists) {
        for (Restaurant restaurant : restoList) {
          if (!restaurantSet.contains(restaurant.getRestaurantId())) {
            restaurantList.add(restaurant);
            restaurantSet.add(restaurant.getRestaurantId());
          }
        }
      }

      return new GetRestaurantsResponse(restaurantList);
    } else {
      return new GetRestaurantsResponse(new ArrayList<>());
    }

  }

  private boolean isTimeWithInRange(LocalTime timeNow, LocalTime startTime, LocalTime endTime) {
    return timeNow.isAfter(startTime) && timeNow.isBefore(endTime);
  }

  public boolean isPeakHour(LocalTime timeNow) {
    return isTimeWithInRange(timeNow, LocalTime.of(7, 59, 59), LocalTime.of(10, 00, 01))
        || isTimeWithInRange(timeNow, LocalTime.of(12, 59, 59), LocalTime.of(14, 00, 01))
        || isTimeWithInRange(timeNow, LocalTime.of(18, 59, 59), LocalTime.of(21, 00, 01));
  }


  @Override
  public GetRestaurantsResponse findRestaurantsBySearchQueryMt(GetRestaurantsRequest 
      getRestaurantsRequest,
      LocalTime currentTime) {
    Double servingRadiusInKms = isPeakHour(currentTime) ? peakHoursServingRadiusInKms : 
        normalHoursServingRadiusInKms;
    String searchFor = getRestaurantsRequest.getSearchFor();

    CompletableFuture<List<Restaurant>> result1 = restaurantRepositoryService
        .asyncFindRestaurantByName(
        getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), 
        searchFor, currentTime,
        servingRadiusInKms);

    CompletableFuture<List<Restaurant>> result2 = restaurantRepositoryService
        .asynFindRestaurantByAttribute(
        getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), 
        searchFor, currentTime,
        servingRadiusInKms);

    CompletableFuture<List<Restaurant>> result3 = restaurantRepositoryService
        .asynFindRestaurantByItemName(
        getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), 
        searchFor, currentTime,
        servingRadiusInKms);

    CompletableFuture<List<Restaurant>> result4 = restaurantRepositoryService
        .asynFindRestaurantByItemAttribute(
        getRestaurantsRequest.getLatitude(), getRestaurantsRequest.getLongitude(), searchFor, 
        currentTime,
        servingRadiusInKms);

    // wait until all of them are done...
    // CompletableFuture.allOf(result1, result2, result3, result4).join();

    List<List<Restaurant>> listOfRestaurantLists = new ArrayList<>();

    if (!searchFor.isEmpty()) {
      try {
        listOfRestaurantLists.add(result1.get());
        listOfRestaurantLists.add(result2.get());
        listOfRestaurantLists.add(result3.get());
        listOfRestaurantLists.add(result4.get());

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }

    Set<String> restaurantSet = new HashSet<>();
    List<Restaurant> restaurantList = new ArrayList<>();
    for (List<Restaurant> restoList : listOfRestaurantLists) {
      for (Restaurant restaurant : restoList) {
        if (!restaurantSet.contains(restaurant.getRestaurantId())) {
          restaurantList.add(restaurant);
          restaurantSet.add(restaurant.getRestaurantId());
        }
      }
    }
    
    return new GetRestaurantsResponse(restaurantList);
  }

  @Override
  public Restaurant findRestaurantById(String restaurantId) {
    Restaurant restaurant = restaurantRepositoryService.getRestaurantById(restaurantId);

    return restaurant;
  }

}

