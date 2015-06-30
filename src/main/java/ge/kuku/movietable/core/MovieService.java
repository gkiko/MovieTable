package ge.kuku.movietable.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.kuku.movietable.data.CloudRepository;
import ge.kuku.movietable.data.MovieItem;
import ge.kuku.movietable.data.Repository;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Path("movies")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MovieService {

    String movieParserApi = "http://movie-parser.herokuapp.com/webapi/parse/";

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

    @POST
    @Path("{id}")
    public List<MovieDo> retrieve(@PathParam("id") String id, @NotNull @Valid MovieDo movieDo) throws IOException {
        List<MovieDo> movieDos = new ArrayList<>();
        List<MovieItem> items = getRepo().retrieve(id);
        for (MovieItem fromDb : items) {
            movieDos.add(fromDb.toDo());
        }
        if (movieDos.isEmpty()) {
            MovieDo mDo = null;
            try {
                mDo = requestMovieSearch(id, movieDo.getName());
                getRepo().save(MovieItem.fromDo(mDo));
                movieDos.add(mDo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return movieDos;
    }

    private MovieDo requestMovieSearch(String id, String movieName) throws IOException {
        HttpClient c = new DefaultHttpClient();
        HttpPost p = new HttpPost(movieParserApi + id);

        p.setEntity(new StringEntity("{\"name\":\"" + movieName + "\"}", ContentType.create("application/json")));

        HttpResponse r = c.execute(p);

        BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
        String line = readFully(rd);

        ObjectMapper mapper = new ObjectMapper();
        MovieDo mDo = mapper.readValue(line, MovieDo.class);
        return mDo;
    }

    private String readFully(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String aux = "";

        while ((aux = reader.readLine()) != null) {
            builder.append(aux);
        }

        return builder.toString();
    }

}
