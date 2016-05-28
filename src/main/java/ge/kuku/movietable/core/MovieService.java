package ge.kuku.movietable.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ge.kuku.movietable.data.CloudRepository;
import ge.kuku.movietable.data.MovieItem;
import ge.kuku.movietable.data.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MovieService {

    private String MOVIE_PARSER_API = "http://192.168.3.144:8080/webapi/movies/";//"http://movie-parser.herokuapp.com/webapi/movies/";

    private Repository getRepo() {
        return new CloudRepository();
    }

    @GET
    @Path("{id}")
    public List<MovieDo> retrieve(@PathParam("id") String id) {
        List<MovieDo> movieDos = new ArrayList<>();
        List<MovieItem> items = getRepo().retrieve(id);
        List<MovieItem> deleted = new ArrayList<>();
        for (MovieItem fromDb : items) {
            if (isAlive(fromDb))
                movieDos.add(fromDb.toDo());
            else deleted.add(fromDb);
        }

        if (movieDos.isEmpty()) {
            for (MovieDo mDo : requestMovieSearch(id)) {
                MovieItem curr = MovieItem.fromDo(mDo);
                int index = deleted.indexOf(curr);
                if (index != -1) {
                    MovieItem tmp = deleted.get(index);
                    mDo.setId(tmp.getId());
                    curr = MovieItem.fromExisting(tmp, mDo);
                    getRepo().save(curr);
                }
                else {
                    mDo.setId(UUID.randomUUID().toString());
                }
                if (!items.contains(curr)) {
                    getRepo().save(curr);
                }
                movieDos.add(mDo);
            }
        }
        return movieDos;
    }

    public boolean isAlive(MovieItem item) {
        long curr = System.currentTimeMillis();
        long old;
        try {
            old = Long.parseLong(item.getExpireTime());
        } catch (Exception e){
            return false;
        }
        return curr > old;
    }

    private MovieDo[] requestMovieSearch(String id) {
        HttpResponse<MovieDo[]> response = null;
        try {
            response = Unirest.get(String.format("%s%s", MOVIE_PARSER_API, id))
                    .asObject(MovieDo[].class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (response == null)
            return null;
        return response.getBody();
    }

}
