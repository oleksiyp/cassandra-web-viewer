package org.cassandra_viewer.type_editor;

import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;
import org.cassandra_viewer.data.CassandraBrowser;
import org.cassandra_viewer.data.GeneralColumn;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * User: captain-protect
 * Date: 4/8/12
 * Time: 12:04 PM
 */
public class TypeSchema {
    private Set<String> columnFamilies;

    public static TypeSchema createDeafult(CassandraBrowser browser) throws TException, NotFoundException {
        return new TypeSchema(browser);
    }

    private TypeSchema(CassandraBrowser browser) throws TException, NotFoundException {
        columnFamilies = browser.getColumnFamilies();
        for (String cf : columnFamilies) {
            Collection<GeneralColumn> columns = browser.getColumns(cf);
        }
    }
}
