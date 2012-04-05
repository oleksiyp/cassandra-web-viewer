package org.cassandra_viewer;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 11:47 AM
 */
public class CassandraViewerFilter implements Filter {
    private FilterConfig config;
    private static final int KEYS_PER_PAGE = 100;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String servletPath = request.getServletPath();
        if (servletPath != null && servletPath.startsWith("/WEB-INF")) {
            chain.doFilter(req, resp);
        } else if (request.getMethod().equals("GET")) {
            doGet(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }


    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String deserializer = request.getParameter("deserializer");
        if (deserializer == null) {
            deserializer = config.getInitParameter("DEFAULT_DESERIALZER");
            if (deserializer == null) {
                deserializer = "";
            }
        }

        String queryString = request.getServletPath();
        if (queryString.startsWith("/")) {
            queryString = queryString.substring(1);
        }
        if (queryString.equals("go")) {
            goRedirect(request, response);
            return;
        }
        String[] values = queryString.split("/");


        if (values.length < 1 || !queryString.endsWith("/")) {
            doStart(request, response);
            return;
        }

        String userHostPort = values[0];
        TProtocol protocol;
        try {
            protocol = doOpenProtocol(userHostPort);
        } catch (TTransportException e) {
            response.sendError(500, e.toString());
            return;
        }
        Cassandra.Client client = new Cassandra.Client(protocol);
        CassandraBrowser browser = new CassandraBrowser(client);

        try {
            if (values.length == 1) {
                Set<String> keyspaces = browseKeyspaces(browser);
                outputKeyspaces(response, keyspaces);
                response.getWriter().println(" <hr /> ");
            } else if (values.length == 2) {
                browser.setKeyspace(values[1]);
                Set<String> columnFamilies = browser.getColumnFamilies();
                outputColumnFamilies(response, columnFamilies);
                response.getWriter().println(" <hr /> ");
            } else if (values.length == 3) {
                browser.setKeyspace(values[1]);
                browser.setColumnFamily(values[2]);
                browser.setDeserialzer(new Deserializer(deserializer));
                browseKeys(request, response, browser);
            } else if (values.length == 4) {
                browser.setKeyspace(values[1]);
                browser.setColumnFamily(values[2]);
                String key = values[3];
                browser.setDeserialzer(new Deserializer(deserializer));
                browseRecord(request, response, browser, key);
            }
            response.getWriter().println(" <a href='..'>Back</a> ");
        } catch (TimedOutException e) {
            response.sendError(408, e.toString());
            return;
        } catch (InvalidRequestException e) {
            response.sendError(400, e.toString());
            return;
        } catch (UnavailableException e) {
            response.sendError(503, e.toString());
            return;
        } catch (TException e) {
            response.sendError(500, e.toString());
            return;
        } catch (NotFoundException e) {
            response.sendError(404, e.toString());
            return;
        }
    }

    private void outputColumnFamilies(HttpServletResponse response, Set<String> columnFamilies) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html><body><h2>Column families</h2>");
        for (String columnFamily : columnFamilies) {
            writer.println("<a href='" + encode(columnFamily) + "/'>" + encode(columnFamily) + "</a><br />");
        }
    }

    private Set<String> browseKeyspaces(CassandraBrowser browser) throws TException {
        return browser.getKeyspaces();
    }

    private void outputKeyspaces(HttpServletResponse response, Set<String> keyspaces) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html><body><h2>Keyspaces</h2>");
        for (String keyspace : keyspaces) {
            writer.println("<a href='" + encode(keyspace) + "/'>" + encode(keyspace) + "</a><br />");
        }
    }

    private TProtocol doOpenProtocol(String userHostPort) throws TTransportException {
        TProtocol protocol;
        String[]hostPortArr = userHostPort.split(":");
        String host = hostPortArr[0];
        int port = 9160;
        if (hostPortArr.length > 1) {
            try {
                port = Integer.parseInt(hostPortArr[1]);
            } catch (NumberFormatException nfe) {
                //skip;
            }
        }

        TSocket transport = new TSocket(host, port, 1000000);
        protocol = new TBinaryProtocol(transport);
        transport.open();
        return protocol;
    }

    private void doStart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        config.getServletContext().getRequestDispatcher("/WEB-INF/start.jsp").forward(request, response);
    }

    private void goRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hostname = request.getParameter("hostname");
        String port = request.getParameter("port");
        String username = request.getParameter("username");

        String path = config.getServletContext().getContextPath() + "/";

        path += (username == null || username.trim().isEmpty()) ? "" : (username.trim() + "@");
        path += (hostname == null || hostname.trim().isEmpty()) ? "" : hostname.trim();

        if (port != null && !port.trim().equals("9160")) {
            path += ":" + port.trim();
        }
        path += "/";
        response.sendRedirect(path);
    }

    private void browseRecord(HttpServletRequest request, HttpServletResponse response, CassandraBrowser browser, String key) throws IOException, TimedOutException, InvalidRequestException, UnavailableException, TException {
        Map<String, CassandraBrowser.ValueTimestamp> record = browser.getRecord(key);
        outputRecord(response, key, record);
    }

    private void outputRecord(HttpServletResponse response, String key, Map<String,CassandraBrowser.ValueTimestamp> record) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html><body><h2>Record </h2><h3>" + encode(key) + "</h3>\n");
        writer.println("<table border='1' cellpadding='0' cellspacing='0'>\n");
        writer.println("<tr><th>COLUMN</th><th>VALUE</th><th>TIMESTAMP</th></tr>\n");
        for (String columnName : record.keySet()) {
            CassandraBrowser.ValueTimestamp column = record.get(columnName);
            Object value = column == null ? null : column.getValue();
            if (value == null) {
                value = "";
            } else if (value.toString().trim().equals("")) {
                value = "&nbsp;";
            } else {
                value = encode(CassandraBrowser.stringify(value));
            }
            writer.println("<tr><td>" + encode(columnName)
                    + "</td><td>" + value
                    + "</td><td>" + encode(new Date(column.getTimestamp()).toString())
                    + "</td></tr>\n");
        }
        writer.println("</table>\n");
    }

    private void browseKeys(HttpServletRequest request, HttpServletResponse response, CassandraBrowser browser) throws IOException, TException, TimedOutException, InvalidRequestException, UnavailableException {
        String start = request.getParameter("start");
        String countStr = request.getParameter("count");
        int count = KEYS_PER_PAGE;
        if (countStr != null) {
            try {
                count = Integer.parseInt(countStr.trim());
            } catch (NumberFormatException nfe) {
                // skip
            }
        }
        String showTableStr = request.getParameter("showTable");
        List<String> keys = browser.getKeys(start, count);
        boolean showTable = (showTableStr != null && showTableStr.trim().equals("true"));
        if (showTable) {
            browseInTable(response, browser, keys);
        } else {
            outputKeys(response, keys);
        }
        response.getWriter().println("<hr></hr>");
        outputNext(response, keys, count, showTable);
        outputShowInTable(response, start, showTable);
    }

    private void browseInTable(HttpServletResponse response, CassandraBrowser browser, List<String> keys) throws IOException, TException, TimedOutException, InvalidRequestException, UnavailableException {
        List<Map<String, CassandraBrowser.ValueTimestamp>> records = new ArrayList<Map<String, CassandraBrowser.ValueTimestamp>>();
        List<String> columns = new ArrayList<String>();
        for (String key : keys) {
            Map<String, CassandraBrowser.ValueTimestamp> record = browser.getRecord(key);
            for (String column : record.keySet()) {
                columns.add(column);
            }
            records.add(record);
        }
        outputTable(response, keys, columns, records);
    }

    private void outputTable(HttpServletResponse response, List<String> keys, List<String> columns, List<Map<String,CassandraBrowser.ValueTimestamp>> records) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html><body><h2>Records in table</h2>\n");
        writer.println("<table border='1' cellpadding='0' cellspacing='0'>\n");

        writer.println("<tr>");
        writer.println("<th>KEY</th>");
        for (String column : columns) {
            writer.println("<th>" + column + "</th>");
        }
        writer.println("</tr>\n");

        for (int i = 0; i < keys.size(); i++) {
            writer.println("<tr>");
            String key = keys.get(i);
            writer.println("<td><a href='" + encode(key) + "/'>" + encode(key) + "</a></td>");
            for (String columnName : columns) {
                CassandraBrowser.ValueTimestamp column = records.get(i).get(columnName);
                Object value = column == null ? null : column.getValue();
                if (value == null) {
                    value = "";
                } else if (value.toString().trim().equals("")) {
                    value = "&nbsp;";
                } else {
                    value = encode(CassandraBrowser.stringify(value));
                }
                writer.println("<td>" + value + "</td>");
            }
            writer.println("</tr>\n");
        }

        writer.println("</table>\n");
    }

    private void help(HttpServletResponse response) throws IOException {
        response.getWriter().write("<html><body>Usage: http://localhost:8080/cassandra-web-viewer/cassandraHost[:Port]/keyspace/columnfamily/. Don't forget last slash!");
    }

    private void outputKeys(HttpServletResponse response, List<String> keys) throws IOException {
        PrintWriter writer = response.getWriter();

        String range = keys.isEmpty() ? "" : encode(keys.get(0)) + " <span style='font-size: 70%'>to</span> " + encode(keys.get(keys.size() - 1));
        writer.println("<html><body><h2>Keys</h2><h3>" + range + "</h3>\n");
        for (String key : keys) {
            writer.println("<a href='" + encode(key) + "/'>" + encode(key) + "</a>\n");
        }
    }
    private void outputNext(HttpServletResponse response, List<String> keys, int count, boolean showTable) throws IOException {
        PrintWriter writer = response.getWriter();
        if (keys.size() == count) {
            String lastKey = keys.get(keys.size() - 1);
            String otherParams = "";
            if (count != KEYS_PER_PAGE) {
                otherParams += "&count=" + count;
            }
            if (showTable) {
                otherParams += "&showTable=true";
            }
            writer.println("<a href='?start=" + encode(lastKey) + otherParams + "'>Next page</a>");
        }
    }

    private void outputShowInTable(HttpServletResponse response, String start, boolean showTable) throws IOException {
        showTable = !showTable;
        PrintWriter writer = response.getWriter();
        String params = start != null ? ("?start=" + encode(start) + "&") : "?";
        params += "count=5&showTable=" + (showTable ? "true" : "false");
        writer.println(" <a href='" + params + "'>" + (showTable ? "Show as table" : "Show as list") + "</a>");
    }


    private String encode(String str) {
        return str
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
