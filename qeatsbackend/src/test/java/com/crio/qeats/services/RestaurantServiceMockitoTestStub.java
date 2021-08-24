
package com.crio.qeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crio.qeats.QEatsApplication;
import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import com.crio.qeats.utils.FixtureHelpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = { QEatsApplication.class })
@ActiveProfiles("test") 
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class RestaurantServiceMockitoTestStub {

  protected static final String FIXTURES = "fixtures/exchanges";

  protected ObjectMapper objectMapper = new ObjectMapper();

  protected Restaurant restaurant1;
  protected Restaurant restaurant2;
  protected Restaurant restaurant3;
  protected Restaurant restaurant4;
  protected Restaurant restaurant5;

  @InjectMocks
  protected RestaurantServiceImpl restaurantService;
  @MockBean
  protected RestaurantRepositoryService restaurantRepositoryServiceMock;

  public List<Restaurant> initializeRestaurantObjects() throws IOException {
    String fixture =
        FixtureHelpers.fixture(FIXTURES + "/mocking_list_of_restaurants.json");
    Restaurant[] restaurants = objectMapper.readValue(fixture, Restaurant[].class);
    // TODO CRIO_TASK_MODULE_MOCKITO
    //  What to do with this Restaurant[] ? Looks unused?
    //  Look for the "assert" statements in the tests
    //  following and find out what to do with the array.
    return Arrays.asList(restaurants);
  }

  @BeforeEach
  void setup() throws IOException {
    MockitoAnnotations.initMocks(this);
    objectMapper = new ObjectMapper();

    restaurant1 = initializeRestaurantObjects().get(0);
    restaurant2 = initializeRestaurantObjects().get(1); 
    restaurant3 = initializeRestaurantObjects().get(2); 
    restaurant4 = initializeRestaurantObjects().get(3); 
    restaurant5 = initializeRestaurantObjects().get(4);
  }



  @Test
  public void  testFindNearbyWithin5km() throws IOException {
    GetRestaurantsResponse allRestaurantsCloseBy = restaurantService
        .findAllRestaurantsCloseBy(new GetRestaurantsRequest(20.0, 30.0),
            LocalTime.of(3, 0));

    assertEquals(2, allRestaurantsCloseBy.getRestaurants().size());
    assertEquals("11", allRestaurantsCloseBy.getRestaurants().get(0).getRestaurantId());
    assertEquals("12", allRestaurantsCloseBy.getRestaurants().get(1).getRestaurantId());

    ArgumentCaptor<Double> servingRadiusInKms = ArgumentCaptor.forClass(Double.class);
    verify(restaurantRepositoryServiceMock, times(1))
        .findAllRestaurantsCloseBy(any(Double.class), any(Double.class), any(LocalTime.class),
            servingRadiusInKms.capture());

  }
 
  @Test
  public void  testFindNearbyWithin3km() throws IOException {

    List<Restaurant> restaurantList1 = loadRestaurantsDuringNormalHours();

    lenient().doReturn(restaurantList1)
        .when(restaurantRepositoryServiceMock)
        .findAllRestaurantsCloseBy(eq(20.0), eq(30.2), eq(LocalTime.of(3, 0)),
            eq(5.0));
    
    GetRestaurantsResponse allRestaurantsCloseByOffPeakHours = 
         restaurantService.findAllRestaurantsCloseBy(new GetRestaurantsRequest(20.0, 30.2), 
         LocalTime.of(3, 0));
    System.out.println("restaurantList1 length: " + restaurantList1.size());
 
    assertEquals(4, allRestaurantsCloseByOffPeakHours.getRestaurants().size());
    assertEquals("10", allRestaurantsCloseByOffPeakHours.getRestaurants().get(0).getRestaurantId());
    assertEquals("11", allRestaurantsCloseByOffPeakHours.getRestaurants().get(1)
        .getRestaurantId());        
  
    // TODO: CRIO_TASK_MODULE_MOCKITO
    //  Call restaurantService.findAllRestaurantsCloseBy with appropriate parameters such that
    //  Both of the mocks created above are called.
    //  Our assessment will verify whether these mocks are called as per the definition.
    //  Refer to the assertions below in order to understand the requirements better.
    
    List<Restaurant> restaurantList2 = loadRestaurantsDuringPeakHours();

    lenient().doReturn(restaurantList2)
        .when(restaurantRepositoryServiceMock)
        .findAllRestaurantsCloseBy(eq(21.0), eq(31.1), eq(LocalTime.of(19, 0)),
        eq(3.0));

    GetRestaurantsResponse allRestaurantsCloseByPeakHours = 
        restaurantService.findAllRestaurantsCloseBy(new GetRestaurantsRequest(21.0, 31.1), 
        LocalTime.of(19, 0));
    allRestaurantsCloseByPeakHours.setRestaurants(restaurantList2); 
 
    System.out.println("restaurantList2 length: " + restaurantList2.size());
     
    assertEquals(2, allRestaurantsCloseByPeakHours.getRestaurants().size());
    assertEquals("11", allRestaurantsCloseByPeakHours.getRestaurants().get(0).getRestaurantId());
    assertEquals("12", allRestaurantsCloseByPeakHours.getRestaurants().get(1).getRestaurantId());

  }

  private List<Restaurant> loadRestaurantsDuringNormalHours() throws IOException {
    String fixture =
        FixtureHelpers.fixture(FIXTURES + "/normal_hours_list_of_restaurants.json");

    return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
    });
  }

  private List<Restaurant> loadRestaurantsDuringPeakHours() throws IOException {
    String fixture = 
        FixtureHelpers.fixture(FIXTURES + "/peak_hours_list_of_restaurants.json");

    return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {
    });

    
  }

}

