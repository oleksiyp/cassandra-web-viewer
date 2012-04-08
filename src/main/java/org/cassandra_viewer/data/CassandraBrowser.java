package org.cassandra_viewer.data;

import org.apache.cassandra.thrift.AuthenticationException;
import org.apache.cassandra.thrift.AuthenticationRequest;
import org.apache.cassandra.thrift.AuthorizationException;
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
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: captain-protect
 * Date: 4/2/12
 * Time: 5:18 PM
 */
public class CassandraBrowser {
    public static final ConsistencyLevel DEFAULT_CONSISTENCY_LEVEL = ConsistencyLevel.QUORUM;
    public static final String RECORD_KEY = "";
    private final Cassandra.Client client;
    private String keyspace;
    private String columnFamily;
    private CassandraDeserializer deserialzer;

    public CassandraBrowser(Cassandra.Client client) {
        this.client = client;
    }

    public static TProtocol doOpenProtocol(String userHostPort) throws TTransportException {
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

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }



    public List<String> getKeys(String start, String end, int count) throws TimedOutException, InvalidRequestException, UnavailableException, TException {
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
        keyRange.setEnd_key(end == null ? "" : end);

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

    public Map<GeneralColumn, ValueTimestamp> getRecord(String key) throws TimedOutException, InvalidRequestException, UnavailableException, TException {

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

        Map<GeneralColumn, ValueTimestamp> result = new TreeMap<GeneralColumn, ValueTimestamp>();

        for (ColumnOrSuperColumn cosc : slice) {
            Column column = cosc.getColumn();
            SuperColumn superColumn = cosc.getSuper_column();
            if (column != null) {
                byte[] name = column.getName();
                byte[] value = column.getValue();

                GeneralColumn genColumn = new GeneralColumn((Comparable) deserialize(name));
                ValueTimestamp valueTs = new ValueTimestamp(deserialize(value), column.getTimestamp());
                result.put(genColumn, valueTs);
            } else if (superColumn != null) {
                for (Column subColumn : superColumn.getColumns())
                {
                    byte[] name = subColumn.getName();
                    byte[] value = subColumn.getValue();
                    GeneralColumn genColumn = new GeneralColumn(
                            (Comparable) deserialize(name),
                            (Comparable) deserialize(superColumn.getName())
                    );
                    ValueTimestamp valueTs = new ValueTimestamp(deserialize(value), subColumn.getTimestamp());

                    result.put(genColumn, valueTs);
                }
            }
        }

        return result;
    }

    public Object deserialize(byte[] bytes) {
        if (deserialzer != null) {
            return deserialzer.deserialize(bytes);
        }
        return bytes;
    }

    public static String stringify(Object object) {
        if (object == null) {
            return "null";
        }
        Class<? extends Object> clazz = object.getClass();
        if (clazz.equals(byte[].class)) {
            return Arrays.toString((byte[]) object);
        } else {
            return object.toString();
        }
    }

    public String stringify(byte []bytes) {
        return stringify(deserialize(bytes));
    }

    public Set<String> getKeyspaces() throws TException {
        return client.describe_keyspaces();
    }

    public Set<String> getColumnFamilies() throws TException, NotFoundException {
        return client.describe_keyspace(keyspace).keySet();
    }

    public void setDeserialzer(CassandraDeserializer deserialzer) {
        this.deserialzer = deserialzer;
    }


    public boolean login(String username, String password) throws AuthorizationException, TException, AuthenticationException {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        Map credentials = new HashMap();
        if (username != null) {
            credentials.put("username", username);
        }
        if (password != null) {
            credentials.put("password", password);
        }
        authRequest.setCredentials(credentials);
        client.login(keyspace, authRequest);
        return true;
    }

    public Collection<GeneralColumn> getColumns(String cf) throws TException, NotFoundException {
        Map<String, Map<String, String>> stringMapMap = client.describe_keyspace(keyspace);
        return null;
    }
}
