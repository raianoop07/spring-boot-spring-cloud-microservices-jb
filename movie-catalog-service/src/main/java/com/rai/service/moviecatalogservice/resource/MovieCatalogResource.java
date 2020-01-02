package com.rai.service.moviecatalogservice.resource;

import com.rai.service.moviecatalogservice.models.CatalogItem;
import com.rai.service.moviecatalogservice.models.UserRating;
import com.rai.service.moviecatalogservice.services.MovieInfo;
import com.rai.service.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating userRating = userRatingInfo.getUserRating(userId);

        /*List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 3)
        );*/

        return userRating.getRatings().stream()
                .map(rating -> {
                    return movieInfo.getCatalogItem(rating);
                    /*Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/"+rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();*/
                })
                .collect(Collectors.toList());
    }
}