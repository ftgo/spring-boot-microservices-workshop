package io.javabrains.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getMovieFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"), // timout
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // look at the last n requests
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // if 50% of 5 fails, break circuit
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"), // interval to start breaking again
            })
    public Movie getMovie(Rating rating) {
        return restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
    }

    @SuppressWarnings("unused")
    Movie getMovieFallback(Rating rating) {
        return new Movie(rating.getMovieId(), "Movie name fallback", "Movie description fallback");
    }
}
