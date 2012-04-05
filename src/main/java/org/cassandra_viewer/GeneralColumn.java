package org.cassandra_viewer;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 4:26 PM
 */
public class GeneralColumn implements Comparable<GeneralColumn> {
    private final Comparable superColumn;
    private final Comparable column;

    public GeneralColumn(Comparable superColumn, Comparable column) {
        this.superColumn = superColumn;
        this.column = column;
    }

    public GeneralColumn(Comparable column) {
        this.superColumn = null;
        this.column = column;
    }

    public String getSuperColumn() {
        if (superColumn == null) {
            return null;
        }
        return CassandraBrowser.stringify(superColumn);
    }

    public String getColumn() {
        if (column == null) {
            return null;
        }
        return CassandraBrowser.stringify(column);
    }

    @Override
    public int compareTo(GeneralColumn o) {
        int cmp = column.compareTo(o.column);
        if (cmp != 0) return cmp;

        if (superColumn == null && o.superColumn != null) {
            return -1;
        } else if (superColumn != null && o.superColumn == null) {
            return 1;
        } else if (superColumn != null && o.superColumn != null) {
            return superColumn.compareTo(o.superColumn);
        }
        return 0;
    }
}
