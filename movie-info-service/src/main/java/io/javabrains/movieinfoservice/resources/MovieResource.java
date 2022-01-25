package io.javabrains.movieinfoservice.resources;

import io.javabrains.movieinfoservice.models.Movie;
import io.javabrains.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        MovieSummary movieSummary = getMovieSummary(movieId);

        return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
    }

    private MovieSummary getMovieSummary(String movieId) {
//      return restTemplate.getForObject(getMovieUri(movieId), MovieSummary.class);

        return this.webClientBuilder.build().get().uri(getMovieUri(movieId)).retrieve().bodyToMono(MovieSummary.class).block();
    }

    private String getMovieUri(String movieId) {
        return String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s", movieId, this.apiKey);
    }

}
