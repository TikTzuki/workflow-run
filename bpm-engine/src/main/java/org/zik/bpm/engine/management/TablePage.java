// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.Map;
import java.util.List;

public class TablePage
{
    protected String tableName;
    protected long total;
    protected long firstResult;
    protected List<Map<String, Object>> rowData;
    
    public TablePage() {
        this.total = -1L;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }
    
    public long getFirstResult() {
        return this.firstResult;
    }
    
    public void setFirstResult(final long firstResult) {
        this.firstResult = firstResult;
    }
    
    public void setRows(final List<Map<String, Object>> rowData) {
        this.rowData = rowData;
    }
    
    public List<Map<String, Object>> getRows() {
        return this.rowData;
    }
    
    public void setTotal(final long total) {
        this.total = total;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getSize() {
        return this.rowData.size();
    }
}
