package ge.kuku.movietable.core;

import com.mashape.unirest.http.Unirest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Unirest.setObjectMapper(new MovieDoMapper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
