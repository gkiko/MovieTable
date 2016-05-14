package ge.kuku.movietable.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ge.kuku.movietable.data.CloudRepository;
import ge.kuku.movietable.data.MovieItem;
import ge.kuku.movietable.data.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MovieService {

    private String MOVIE_PARSER_API = "http://movie-parser.herokuapp.com/webapi/parse/";

    private Repository getRepo() {
        return new CloudRepository();
    }

    @GET
    @Path("{id}")
    public List<MovieDo> retrieve(@PathParam("id") String id,
                                  @QueryParam("movie_name") String movieName) {
        List<MovieDo> movieDos = new ArrayList<>();
        List<MovieItem> items = getRepo().retrieve(id);
        for (MovieItem fromDb : items) {
            movieDos.add(fromDb.toDo());
        }

        if (movieDos.isEmpty()) {
            for (MovieDo mDo : requestMovieSearch(id, movieName)) {
                mDo.setId(UUID.randomUUID().toString());
                getRepo().save(MovieItem.fromDo(mDo));
                movieDos.add(mDo);
            }
        }
        return movieDos;
    }

    private MovieDo[] requestMovieSearch(String id, String movieName) {
        HttpResponse<MovieDo[]> response = null;
        try {
            response = Unirest.post(String.format("%s%s", MOVIE_PARSER_API, id))
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(String.format("{name: %s}", movieName)))
                    .asObject(MovieDo[].class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

}
