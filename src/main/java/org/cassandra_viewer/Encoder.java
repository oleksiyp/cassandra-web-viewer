package org.cassandra_viewer;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 3:06 PM
 */
public class Encoder {
    public static String encode(String str) {
        return str
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
