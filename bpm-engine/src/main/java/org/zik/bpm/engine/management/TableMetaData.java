// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.ArrayList;
import java.util.List;

public class TableMetaData
{
    protected String tableName;
    protected List<String> columnNames;
    protected List<String> columnTypes;
    
    public TableMetaData() {
        this.columnNames = new ArrayList<String>();
        this.columnTypes = new ArrayList<String>();
    }
    
    public TableMetaData(final String tableName) {
        this.columnNames = new ArrayList<String>();
        this.columnTypes = new ArrayList<String>();
        this.tableName = tableName;
    }
    
    public void addColumnMetaData(final String columnName, final String columnType) {
        this.columnNames.add(columnName);
        this.columnTypes.add(columnType);
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }
    
    public List<String> getColumnNames() {
        return this.columnNames;
    }
    
    public void setColumnNames(final List<String> columnNames) {
        this.columnNames = columnNames;
    }
    
    public List<String> getColumnTypes() {
        return this.columnTypes;
    }
    
    public void setColumnTypes(final List<String> columnTypes) {
        this.columnTypes = columnTypes;
    }
}
