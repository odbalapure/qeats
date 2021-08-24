package com.crio.qeats.repositoryservices;

import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// @Service
public class RestaurantAsyncService {

  private String url = "localhost:8081/qeats/v1/restaurants?latitude=20.700382&longitude=78.629861";

  @Autowired
  private RestTemplate restTemplate;

  @Bean
  public RestTemplate restTemplate() {
    return restTemplate;
  }    

  @Async("asyncExecutor")
  public CompletableFuture<GetRestaurantsResponse> getRestaurants() throws InterruptedException {
    GetRestaurantsResponse getRestaurantsResponse = restTemplate
        .getForObject(url, GetRestaurantsResponse.class);

    System.out.println("RestaurantAsyncService::getRestaurants " + getRestaurantsResponse);
    Thread.sleep(1000L);
    return CompletableFuture.completedFuture(getRestaurantsResponse);
  }

  @Async("asyncExecutor")
  public CompletableFuture<GetRestaurantsResponse> getRestaurantSearchResult() 
      throws InterruptedException {
    GetRestaurantsResponse getRestaurantsResponse = restTemplate
        .getForObject(url + "?%s", GetRestaurantsResponse.class);

    Thread.sleep(1000L);
    return CompletableFuture.completedFuture(getRestaurantsResponse);
  }

}