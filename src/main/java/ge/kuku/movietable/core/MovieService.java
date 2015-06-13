package ge.kuku.movietable.core;

import ge.kuku.movietable.data.CloudRepository;
import ge.kuku.movietable.data.MovieItem;
import ge.kuku.movietable.data.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MovieService {

    private Repository getRepo() {
        Repository repo = new CloudRepository();
        return repo;
    }

    @GET
    public List<MovieDo> read() {
        final ArrayList<MovieDo> result = new ArrayList<MovieDo>();
        MovieDo movieDo = new MovieDo();
        movieDo.setSource("http://example.com/movie/nasjdhiu13h12g");
        movieDo.setLanguage("JP");
        movieDo.setQuality("HD");
        result.add(movieDo);
        return result;
    }

    @GET
    @Path("{id}")
    public List<MovieDo> retrieve(@PathParam("id") String id) {
        List<MovieDo> movieDos = new ArrayList<>();
        List<MovieItem> items = getRepo().retrieve(id);
        for (MovieItem fromDb : items) {
            movieDos.add(fromDb.toDo());
        }
        return movieDos;
    }

}