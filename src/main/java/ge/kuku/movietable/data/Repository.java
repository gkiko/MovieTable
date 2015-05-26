package ge.kuku.movietable.data;

import java.util.List;

public interface Repository {

    public void save(MovieItem item);

    public List<MovieItem> retrieve(String imdbId);
}
