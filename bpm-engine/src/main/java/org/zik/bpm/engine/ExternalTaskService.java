// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.externaltask.UpdateExternalTaskRetriesSelectBuilder;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.externaltask.ExternalTaskQuery;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.externaltask.ExternalTaskQueryBuilder;

public interface ExternalTaskService
{
    ExternalTaskQueryBuilder fetchAndLock(final int p0, final String p1);
    
    ExternalTaskQueryBuilder fetchAndLock(final int p0, final String p1, final boolean p2);
    
    void lock(final String p0, final String p1, final long p2);
    
    void complete(final String p0, final String p1);
    
    void complete(final String p0, final String p1, final Map<String, Object> p2);
    
    void complete(final String p0, final String p1, final Map<String, Object> p2, final Map<String, Object> p3);
    
    void extendLock(final String p0, final String p1, final long p2);
    
    void handleFailure(final String p0, final String p1, final String p2, final int p3, final long p4);
    
    void handleFailure(final String p0, final String p1, final String p2, final String p3, final int p4, final long p5);
    
    void handleFailure(final String p0, final String p1, final String p2, final String p3, final int p4, final long p5, final Map<String, Object> p6, final Map<String, Object> p7);
    
    void handleBpmnError(final String p0, final String p1, final String p2);
    
    void handleBpmnError(final String p0, final String p1, final String p2, final String p3);
    
    void handleBpmnError(final String p0, final String p1, final String p2, final String p3, final Map<String, Object> p4);
    
    void unlock(final String p0);
    
    void setRetries(final String p0, final int p1);
    
    void setRetries(final List<String> p0, final int p1);
    
    Batch setRetriesAsync(final List<String> p0, final ExternalTaskQuery p1, final int p2);
    
    UpdateExternalTaskRetriesSelectBuilder updateRetries();
    
    void setPriority(final String p0, final long p1);
    
    ExternalTaskQuery createExternalTaskQuery();
    
    List<String> getTopicNames();
    
    List<String> getTopicNames(final boolean p0, final boolean p1, final boolean p2);
    
    String getExternalTaskErrorDetails(final String p0);
}
