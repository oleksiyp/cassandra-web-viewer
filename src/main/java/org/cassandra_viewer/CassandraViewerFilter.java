package org.cassandra_viewer;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;
import org.cassandra_viewer.util.HttpParamsHelper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
            new RequestProcessor(request, response).doGet();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    private class RequestProcessor {
        private final HttpServletRequest request;
        private final HttpServletResponse response;
        private String hostPort;
        private CassandraBrowser browser;

        private RequestProcessor(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

        public void doGet() throws IOException, ServletException {
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
                actionRedirect();
                return;
            } else if (queryString.equals("navigate")) {
                actionNavigate();
                return;
            }
            String[] values = queryString.split("/");


            if (values.length < 1 || !queryString.endsWith("/")) {
                forward("START", "/WEB-INF/start.jsp");
                return;
            }

            String userHostPort = values[0];
            TProtocol protocol;
            try {
                protocol = CassandraBrowser.doOpenProtocol(userHostPort);
            } catch (TTransportException e) {
                response.sendError(500, e.toString());
                return;
            }
            hostPort = userHostPort;
            Cassandra.Client client = new Cassandra.Client(protocol);
            browser = new CassandraBrowser(client);

            try {
                if (values.length == 1) {
                    browseKeyspaces();
                } else if (values.length == 2) {
                    browser.setKeyspace(values[1]);
                    browseColumnFamilies();
                } else if (values.length == 3) {
                    browser.setKeyspace(values[1]);
                    browser.setColumnFamily(values[2]);
                    browser.setDeserialzer(new CassandraDeserializer(deserializer));
                    browseKeys(browser);
                } else if (values.length == 4) {
                    browser.setKeyspace(values[1]);
                    browser.setColumnFamily(values[2]);
                    String key = values[3];
                    browser.setDeserialzer(new CassandraDeserializer(deserializer));
                    browseRecord(key);
                }
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

        private void actionNavigate() throws IOException {
            Map map = new TreeMap();
            HttpParamsHelper.suckInParam(map, request, "start");
            HttpParamsHelper.suckInParam(map, request, "count");
            HttpParamsHelper.suckInParam(map, request, "showInTable");
            HttpParamsHelper.suckInParam(map, request, "next");
            HttpParamsHelper.suckInParam(map, request, "toKey");
            HttpParamsHelper.suckInParam(map, request, "previous");
            HttpParamsHelper.suckInParam(map, request, "fromKey");
            HttpParamsHelper.suckInParam(map, request, "jump");
            HttpParamsHelper.suckInParam(map, request, "jumpPos");
            HttpParamsHelper.suckInParam(map, request, "limit");
            HttpParamsHelper.suckInParam(map, request, "limitCount");

            if (request.getParameter("next") != null) {
                map.put("start", map.get("toKey"));
                map.remove("end");
                removeTechnicalNavigableParams(map);
                goReferer(map);
            } else if (request.getParameter("previous") != null) {
                map.put("end", map.get("fromKey"));
                map.remove("start");
                removeTechnicalNavigableParams(map);
                goReferer(map);
            } else if (request.getParameter("jump") != null) {
                map.put("start", map.get("jumpPos"));
                removeTechnicalNavigableParams(map);
                goReferer(map);
            } else if (request.getParameter("limit") != null) {
                map.put("count", map.get("limitCount"));
                removeTechnicalNavigableParams(map);
                goReferer(map);
            }
        }

        private void removeTechnicalNavigableParams(Map map) {
            map.remove("firstKey");
            map.remove("lastKey");
            map.remove("next");
            map.remove("toKey");
            map.remove("previous");
            map.remove("fromKey");
            map.remove("jump");
            map.remove("limit");
            map.remove("jumpPos");
            map.remove("limitCount");
        }

        private void goReferer(Map params) throws IOException {
            String referer = request.getHeader("Referer");
            if (referer == null){
                response.sendError(500, "No referer to action jumplet");
                return;
            }

            int paramsPoint = referer.indexOf('?');
            if (paramsPoint != -1) {
                referer = referer.substring(0, paramsPoint);
            }
            referer += "?" + HttpParamsHelper.toParamsStr(params);
            response.sendRedirect(referer);
        }

        private void actionRedirect() throws IOException {
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


        private void browseKeyspaces() throws TException, IOException, ServletException {
            outputKeyspaces(browser.getKeyspaces());
        }

        private void browseColumnFamilies() throws TException, NotFoundException, IOException, ServletException {
            outputColumnFamilies(browser.getColumnFamilies());
        }

        private void browseRecord(String key) throws IOException, TimedOutException, InvalidRequestException, UnavailableException, TException, ServletException {
            Map<GeneralColumn, ValueTimestamp> record = browser.getRecord(key);
            outputRecord(key, record);
        }

        private void browseKeys(CassandraBrowser browser) throws IOException, TException, TimedOutException, InvalidRequestException, UnavailableException, ServletException {
            String start = request.getParameter("start");
            String end = request.getParameter("end");
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
            List<String> keys = browser.getKeys(start, end, count + 1);
            boolean hasNext = true, hasPrevoius = true;
            if (start != null) {
                hasNext = keys.size() == count + 1;
                if (hasNext) {
                    keys = keys.subList(0, keys.size() - 1);
                }
            }
            if (end != null)
            {
                hasPrevoius = keys.size() == count + 1;
                if (hasPrevoius) {
                    keys = keys.subList(1, keys.size());
                }
            }

            boolean showTable = (showTableStr != null && showTableStr.trim().equals("true"));
            if (showTable) {
                browseInTable(keys);
            } else {
                outputKeys(keys, hasNext, hasPrevoius);
            }
//            outputNext(keys, count, showTable);
//            outputShowInTable(start, showTable);
        }

        private void browseInTable(List<String> keys) throws IOException, TException, TimedOutException, InvalidRequestException, UnavailableException, ServletException {
            List<Map<GeneralColumn, ValueTimestamp>> records = new ArrayList<Map<GeneralColumn, ValueTimestamp>>();
            List<GeneralColumn> columns = new ArrayList<GeneralColumn>();
            for (String key : keys) {
                Map<GeneralColumn, ValueTimestamp> record = browser.getRecord(key);
                for (GeneralColumn column : record.keySet()) {
                    columns.add(column);
                }
                records.add(record);
            }
            outputTable(keys, columns, records);
        }


        private void outputColumnFamilies(Set<String> columnFamilies) throws IOException, ServletException {
            request.setAttribute("keyspace", browser.getKeyspace());
            request.setAttribute("families", columnFamilies);
            forward("COLUMN_FAMILIES", "/WEB-INF/columnfamilies.jsp");
        }

        private void outputKeyspaces(Set<String> keyspaces) throws IOException, ServletException {
            request.setAttribute("hostPort", hostPort);
            request.setAttribute("keyspaces", keyspaces);
            forward("KEYSPACES", "/WEB-INF/keyspaces.jsp");
        }

        private void outputRecord(String key, Map<GeneralColumn, ValueTimestamp> record) throws IOException, ServletException {
            request.setAttribute("key", key);
            request.setAttribute("columns", record.keySet());
            request.setAttribute("record", record);
            forward("RECORD", "/WEB-INF/record.jsp");
        }

        private void outputTable(List<String> keys, List<GeneralColumn> columns, List<Map<GeneralColumn, ValueTimestamp>> records) throws IOException, ServletException {
            request.setAttribute("keys", keys);
            request.setAttribute("columns", columns);
            request.setAttribute("records", records);
            request.setAttribute("fromKey", keys.get(0));
            request.setAttribute("toKey", keys.get(keys.size() - 1));
            forward("TABLE", "/WEB-INF/recordtable.jsp");
        }

        private void outputKeys(List<String> keys, boolean hasNext, boolean hasPrevious) throws IOException, ServletException {
            request.setAttribute("keys", keys);
            request.setAttribute("hasRange", !keys.isEmpty());
            request.setAttribute("fromKey", keys.get(0));
            request.setAttribute("toKey", keys.get(keys.size() - 1));
            request.setAttribute("hasNext", hasNext);
            request.setAttribute("hasPrevious", hasPrevious);
            forward("KEYS", "/WEB-INF/keys.jsp");
        }

        private void forward(String pageType, String path) throws ServletException, IOException {
            request.setAttribute("pageType", pageType);
            config.getServletContext().getRequestDispatcher(path).forward(request, response);
        }

    }
}
