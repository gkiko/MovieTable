package ge.kuku.movietable.data;

import java.util.List;

public interface Repository {

    void save(MovieItem item);

    List<MovieItem> retrieve(String imdbId);
}
