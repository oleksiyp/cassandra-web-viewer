package org.cassandra_viewer;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: captain-protect
 * Date: 4/8/12
 * Time: 11:41 AM
 */
public class Controller {
    protected final FilterConfig config;
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;

    public Controller(FilterConfig config, HttpServletRequest request, HttpServletResponse response) {
        this.config = config;
        this.request = request;
        this.response = response;
    }

    public Controller(Controller controller) {
        this.config = controller.config;
        this.request = controller.request;
        this.response = controller.response;
    }

    protected void forward(String path) throws ServletException, IOException {
        config.getServletContext().getRequestDispatcher(path).forward(request, response);
    }

}
