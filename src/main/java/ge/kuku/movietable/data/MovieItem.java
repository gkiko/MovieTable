package ge.kuku.movietable.data;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import ge.kuku.movietable.core.MovieDo;

@DynamoDBTable(tableName = "MovieTable")
public class MovieItem {
    private String id;
    private String imdbId;
    private String language;
    private String quality;
    private String source;
    private String expireTime;
    private String name;

    private static final String EXPIRES_NAME = "expires";

    public static MovieItem fromDo(MovieDo movieDo) {
        MovieItem movieItem = new MovieItem();
        movieItem.setImdbId(movieDo.getImdbId());
        movieItem.setLanguage(movieDo.getLanguage());
        movieItem.setQuality(movieDo.getQuality());
        movieItem.setSource(movieDo.getSource());
        movieItem.setName(movieDo.getName());

        String timeout = trimTime(movieDo.getSource());
        timeout = padTime(timeout);
        movieItem.setExpireTime(timeout);
        return movieItem;
    }

    private static String trimTime(String source) {
        String exp = source.substring(source.indexOf(EXPIRES_NAME) + EXPIRES_NAME.length()+1);
        if (exp.contains("&"))
            exp = exp.substring(0, exp.indexOf("&"));
        return exp;
    }

    private static String padTime(String time) {
        if (time.length() == 13)
            return time;
        return String.format("%-13s", time).replace(' ', '0');
    }

    @Override
    public boolean equals(Object obj) {
        MovieItem neigh = (MovieItem)obj;

        return getImdbId().equals(neigh.getImdbId()) &&
                getLanguage().equals(neigh.getLanguage()) &&
                getQuality().equals(neigh.getQuality());
    }

    @DynamoDBRangeKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBHashKey(attributeName = "imdbId")
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @DynamoDBAttribute(attributeName = "Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @DynamoDBAttribute(attributeName = "Quality")
    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @DynamoDBAttribute(attributeName = "Source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Expire")
    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public MovieDo toDo() {
        MovieDo movieDo = new MovieDo();
        movieDo.setId(id);
        movieDo.setImdbId(imdbId);
        movieDo.setLanguage(language);
        movieDo.setQuality(quality);
        movieDo.setSource(source);
        movieDo.setName(name);
        return movieDo;
    }

}