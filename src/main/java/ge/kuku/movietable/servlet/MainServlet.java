package ge.kuku.movietable.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        int statusCode;

        String imdbId = request.getParameter("imdb");
        if(imdbId == null || imdbId.trim().isEmpty()){
            writer.write("invalid");
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        } else {


            writer.write(imdbId);
            statusCode = HttpServletResponse.SC_OK;
        }

        response.setStatus(statusCode);
        writer.flush();
    }
}
