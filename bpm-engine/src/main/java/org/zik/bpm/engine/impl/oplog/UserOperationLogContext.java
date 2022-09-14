// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.oplog;

import java.util.ArrayList;
import java.util.List;

public class UserOperationLogContext
{
    protected String operationId;
    protected String userId;
    protected List<UserOperationLogContextEntry> entries;
    
    public UserOperationLogContext() {
        this.entries = new ArrayList<UserOperationLogContextEntry>();
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public String getOperationId() {
        return this.operationId;
    }
    
    public void setOperationId(final String operationId) {
        this.operationId = operationId;
    }
    
    public void addEntry(final UserOperationLogContextEntry entry) {
        this.entries.add(entry);
    }
    
    public List<UserOperationLogContextEntry> getEntries() {
        return this.entries;
    }
}
