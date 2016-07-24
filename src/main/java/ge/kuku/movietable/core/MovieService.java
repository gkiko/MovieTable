package ge.kuku.movietable.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ge.kuku.movietable.data.CloudRepository;
import ge.kuku.movietable.data.MovieItem;
import ge.kuku.movietable.data.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MovieService {

    private String MOVIE_PARSER_API = "http://movie-parser.herokuapp.com/webapi/movies/";

    private Repository getRepo() {
        return new CloudRepository();
    }

    @GET
    @Path("{id}")
    public List<MovieDo> retrieve(@PathParam("id") String id) {
        List<MovieItem> items = getRepo().retrieve(id);

        if (obsoleteOrEmpty(items)) {
            List<MovieDo> movieDos = requestMovieSearch(id);
            List<MovieItem> fetchedMovies;
            fetchedMovies = movieDos.stream()
                    .map(MovieItem::fromDo)
                    .collect(Collectors.toCollection(ArrayList::new));

            List<MovieItem> finalItems = items;
            fetchedMovies.forEach(i -> i.setId(tryGetOldId(i, finalItems)));
            fetchedMovies.forEach(i -> getRepo().save(i));

            items = fetchedMovies;
        }

        return items.stream()
                .map(MovieItem::toDo)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String tryGetOldId(MovieItem fetchedMovie, List<MovieItem> items) {
        List<MovieItem> ls = items.stream()
                .filter(i -> i.getImdbId().equals(fetchedMovie.getImdbId()) &&
                        i.getQuality().equals(fetchedMovie.getQuality()) &&
                        i.getLanguage().equals(fetchedMovie.getLanguage()))
                .collect(Collectors.toList());

        if (ls.isEmpty())
            return UUID.randomUUID().toString();

        return ls.get(0).getId();
    }

    private boolean obsoleteOrEmpty(List<MovieItem> items) {
        return items.isEmpty() || obsolete(items);
    }

    private boolean obsolete(List<MovieItem> items) {
        return !items.isEmpty() && expired(items.get(0));
    }

    private boolean expired(MovieItem item) {
        long curr = System.currentTimeMillis();
        long old = Long.parseLong(item.getExpireTime());
        return curr < old;
    }

    private List<MovieDo> requestMovieSearch(String id) {
        HttpResponse<MovieDo[]> response = null;
        try {
            response = Unirest.get(String.format("%s%s", MOVIE_PARSER_API, id))
                    .asObject(MovieDo[].class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (response == null)
            return Collections.emptyList();
        return Arrays.asList(response.getBody());
    }

}
