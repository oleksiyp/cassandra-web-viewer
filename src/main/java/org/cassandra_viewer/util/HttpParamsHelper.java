package org.cassandra_viewer.util;

import org.cassandra_viewer.util.Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 6:18 PM
 */
public class HttpParamsHelper {
    public static String getHiddensStr(Map params) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Object keyObj : params.keySet()) {
            String value = (String) params.get(keyObj);
            if (value == null) {
                continue;
            }
            String key = Encoder.encode((String) keyObj);
            String valueStr = Encoder.encode(value);
            builder.append("<input type='hidden' name='" + key + "' value='" + valueStr + "' />");
        }
        return builder.toString();
    }

    public static String toParamsStr(Map params) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object key : params.keySet()) {
            String value = (String) params.get(key);
            if (value == null) {
                continue;
            }
            if (!first) {
                builder.append("&");
            }
            first = false;
            builder.append(Encoder.encode((String) key));
            builder.append("=");
            builder.append(Encoder.encode(value));
        }
        return builder.toString();
    }

    public static Map change(Map input, String key, String value) {
        input = new TreeMap(input);
        input.put(key, value);
        return input;
    }

    public static void suckInParam(Map map, HttpServletRequest request, String name) {
        map.put(name, request.getParameter(name));
    }
}
