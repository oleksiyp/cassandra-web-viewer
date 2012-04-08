package org.cassandra_viewer.data;

/**
* User: captain-protect
* Date: 4/5/12
* Time: 4:26 PM
*/
public class ValueTimestamp {
    private final Object value;
    private final long timestamp;

    ValueTimestamp(Object value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Object getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
