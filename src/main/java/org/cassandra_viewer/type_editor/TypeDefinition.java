package org.cassandra_viewer.type_editor;

/**
 * User: captain-protect
 * Date: 4/8/12
 * Time: 12:01 PM
 */
public class TypeDefinition {
    enum Category {
        ANY,
        ANY_COLUMN,
        COLUMN
    }
    enum Type {
        DEFAULT,
        NULL,
        BOOLEAN1B,
        BOOLEAN4B,
        BYTE,
        CHAR,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        BYTES,
        DATE,
        STRING,
        UUID,
        OBJECT,
    }

    private final Category category;
    private final String columnFamily;
    private final String column;
    private final Type type;
    private final boolean isArray;
    private final int startOffset;
    private final int endOffset;

    public TypeDefinition(Category category,
                          String columnFamily,
                          String column,
                          Type type,
                          boolean array,
                          int startOffset,
                          int endOffset) {
        this.category = category;
        this.columnFamily = columnFamily;
        this.column = column;
        this.type = type;
        isArray = array;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public Category getCategory() {
        return category;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public String getColumn() {
        return column;
    }

    public Type getType() {
        return type;
    }

    public boolean isArray() {
        return isArray;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
}
