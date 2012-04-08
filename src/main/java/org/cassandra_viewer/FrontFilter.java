package org.cassandra_viewer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 11:47 AM
 */
public class FrontFilter implements Filter {
    private FilterConfig config;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String servletPath = request.getServletPath();
        if (servletPath != null && servletPath.startsWith("/WEB-INF")) {
            chain.doFilter(req, resp);
        } else if (request.getMethod().equals("GET")) {
            new CassandraViewer(config, request, response).doGet();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

}
