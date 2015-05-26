package ge.kuku.movietable.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MovieDo {

    @XmlElement
    private String id;

    @XmlElement
    private String imdbId;

    @XmlElement
    private String url;

    @XmlElement
    private String quality;

    @XmlElement
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getSource() {
        return url;
    }

    public void setSource(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}