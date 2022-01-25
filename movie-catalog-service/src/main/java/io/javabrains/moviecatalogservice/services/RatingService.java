package io.javabrains.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RatingService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(
            fallbackMethod = "getUserRatingFallback",
            threadPoolKey = "userRatingThreadPool", // this key is shareable with other methods
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"), // thread pool size
                    @HystrixProperty(name = "maxQueueSize", value = "10"), // how many requests waiting without consumming resources?
            })
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
    }

    @SuppressWarnings("unused")
    UserRating getUserRatingFallback(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Collections.emptyList());
        return userRating;
    }
}
