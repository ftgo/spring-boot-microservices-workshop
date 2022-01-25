package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import io.javabrains.moviecatalogservice.services.MovieService;
import io.javabrains.moviecatalogservice.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MovieService movieService;

    @RequestMapping("/{userId}")
//    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

//        ratings-data-service
        UserRating userRating = getUserRating(userId);

        return userRating.getRatings().stream()
                .map(rating -> {
//                    movie-info-service
                    Movie movie = getMovie(rating);
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());
    }


    private UserRating getUserRating(String userId) {
        return this.ratingService.getUserRating(userId);
    }

    private Movie getMovie(Rating rating) {
        return this.movieService.getMovie(rating);
    }

//    @HystrixCommand(fallbackMethod = "getMovieFallback")
//    private Movie getMovie(Rating rating) {
//        return restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
//    }
//
//    private Movie getMovieFallback(Rating rating) {
//        return new Movie(rating.getMovieId(), "Movie name fallback", "Movie description fallback");
//    }
//
//    @HystrixCommand(fallbackMethod = "getUserRatingFallback")
//    private UserRating getUserRating(String userId) {
//        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
//    }
//
//    private UserRating getUserRatingFallback(String userId) {
//        UserRating userRating = new UserRating();
//        userRating.setUserId(userId);
//        userRating.setRatings(Arrays.asList(new Rating("-1", -1)));
//        return userRating;
//    }


//    private List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
//        return Arrays.asList(new CatalogItem("No movie", "", 0));
//    }
}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/