// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import java.util.Collections;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.List;

public class FlushResult
{
    protected List<DbOperation> failedOperations;
    protected List<DbOperation> remainingOperations;
    
    public FlushResult(final List<DbOperation> failedOperations, final List<DbOperation> remainingOperations) {
        this.failedOperations = failedOperations;
        this.remainingOperations = remainingOperations;
    }
    
    public List<DbOperation> getFailedOperations() {
        return this.failedOperations;
    }
    
    public List<DbOperation> getRemainingOperations() {
        return this.remainingOperations;
    }
    
    public boolean hasFailures() {
        return !this.failedOperations.isEmpty();
    }
    
    public boolean hasRemainingOperations() {
        return !this.remainingOperations.isEmpty();
    }
    
    public static FlushResult allApplied() {
        return new FlushResult(Collections.emptyList(), Collections.emptyList());
    }
    
    public static FlushResult withFailures(final List<DbOperation> failedOperations) {
        return new FlushResult(failedOperations, Collections.emptyList());
    }
    
    public static FlushResult withFailuresAndRemaining(final List<DbOperation> failedOperations, final List<DbOperation> remainingOperations) {
        return new FlushResult(failedOperations, remainingOperations);
    }
}
