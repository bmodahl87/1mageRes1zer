

package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(

        name = "index",
        urlPatterns = {"/index.html"}

)


/**
 * ShowUserDocs
 *
 * Servlet that serves the user-docs jsp
 * as the index of the application
 */
public class ShowUserDocs extends HttpServlet {


    /**
     * Serves the user-docs page as the index of the application
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/user-docs.jsp";

        RequestDispatcher dispatcher =
                getServletContext().getRequestDispatcher(url);

        dispatcher.forward(request, response);

    }


}
