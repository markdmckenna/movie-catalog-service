package io.javabrains.moviecatalogservice.resources;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.UserCatalog;
import io.javabrains.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

//    @Autowired
	private RestTemplate restTemplate;
	
	private WebClient.Builder webClientBuilder;

//	@Autowired
	public MovieCatalogResource(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
		super();
		this.restTemplate = restTemplate;
		this.webClientBuilder = webClientBuilder;
	}

	@GetMapping("/{userId}")
	public UserCatalog getCatalog(@PathVariable("userId") String userId) {
		UserCatalog userCatalog = new UserCatalog();
//		List<Rating> ratingsList = Arrays.asList(
//				new Rating("1234", 3), 
//				new Rating("5678", 4)
//		);
		
		
		UserRating userRating 
			= restTemplate.getForObject("http://localhost:8083/ratingsdata/user/" + userId, UserRating.class);
		
		if (null == userRating) return userCatalog;
		
		List<CatalogItem> catalogItems = userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie 
                    	= restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                	
//                	Movie movie = webClientBuilder.build()
//			                		.get()
//			                		.uri("http://localhost:8082/movies/" + rating.getMovieId())
//			                		.retrieve()
//			                		.bodyToMono(Movie.class)
//			                		.block();
                	
                    return new CatalogItem(movie.getName(), "Description", rating.getRating());
                })
				.toList();
		userCatalog.setUserId(userId);
		userCatalog.setCatalogItems(catalogItems);
		return userCatalog;

	}

}
