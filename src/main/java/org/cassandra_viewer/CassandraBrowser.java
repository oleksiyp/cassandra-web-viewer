package org.cassandra_viewer;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: captain-protect
 * Date: 4/2/12
 * Time: 5:18 PM
 */
public class CassandraBrowser {
    public static final ConsistencyLevel DEFAULT_CONSISTENCY_LEVEL = ConsistencyLevel.QUORUM;
    private final Cassandra.Client client;
    private String keyspace;
    private String columnFamily;

    public CassandraBrowser(Cassandra.Client client) {
        this.client = client;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public List<String> getKeys(String start, int count) throws TimedOutException, InvalidRequestException, UnavailableException, TException {
        if (keyspace == null) {
            throw new IllegalArgumentException("no keyspace specified");
        }
        if (columnFamily == null) {
            throw new IllegalArgumentException("no columnFamily specified");
        }

        SlicePredicate predicate = new SlicePredicate();
        predicate.setColumn_names(Collections.singletonList(new byte[]{'A','N','Y','_','C','O','L','U','M','N'}));

        ColumnParent parent = new ColumnParent(columnFamily);

        KeyRange keyRange = new KeyRange(count);
        keyRange.setStart_key(start == null ? "" : start);
        keyRange.setEnd_key("");

        List<KeySlice> slices = client.get_range_slices(
                keyspace,
                parent,
                predicate,
                keyRange,
                DEFAULT_CONSISTENCY_LEVEL);

        List<String> result = new ArrayList<String>();
        for (KeySlice keySlice : slices) {
            result.add(keySlice.getKey());
        }
        return result;
    }

    public Map<String, ValueTimestamp> getRecord(String key) throws TimedOutException, InvalidRequestException, UnavailableException, TException {

        SliceRange range = new SliceRange();
        range.setStart(new byte[0]);
        range.setFinish(new byte[0]);
        range.setReversed(false);
        range.setCount(Integer.MAX_VALUE);

        SlicePredicate predicate = new SlicePredicate();
        predicate.setSlice_range(range);

        ColumnParent columnParent = new ColumnParent(columnFamily);

        List<ColumnOrSuperColumn> slice = client.get_slice(keyspace,
                key,
                columnParent,
                predicate,
                DEFAULT_CONSISTENCY_LEVEL);

        Map<String, ValueTimestamp> result = new TreeMap<String, ValueTimestamp>();
        for (ColumnOrSuperColumn cosc : slice) {
            Column column = cosc.getColumn();
            SuperColumn superColumn = cosc.getSuper_column();
            if (column != null) {
                byte[] name = column.getName();
                byte[] value = column.getValue();

                result.put(new String(name),
                        new ValueTimestamp(value, column.getTimestamp())
                );
            } else if (superColumn != null) {
                for (Column subColumn : superColumn.getColumns())
                {
                    byte[] name = subColumn.getName();
                    byte[] value = subColumn.getValue();
                    String superName = new String(superColumn.getName()) + ":" + new String(name);
                    result.put(superName,
                        new ValueTimestamp(value, subColumn.getTimestamp())
                    );
                }
            }
        }

        return result;
    }

    public Set<String> getKeyspaces() throws TException {
        return client.describe_keyspaces();
    }

    public Set<String> getColumnFamilies() throws TException, NotFoundException {
        return client.describe_keyspace(keyspace).keySet();
    }

    public static class ValueTimestamp {
        private final Object value;
        private final long timestamp;

        private ValueTimestamp(Object value, long timestamp) {
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
}
