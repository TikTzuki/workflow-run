// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

import java.util.Date;

public interface Attachment
{
    String getId();
    
    String getName();
    
    void setName(final String p0);
    
    String getDescription();
    
    void setDescription(final String p0);
    
    String getType();
    
    String getTaskId();
    
    String getProcessInstanceId();
    
    String getUrl();
    
    Date getCreateTime();
    
    String getRootProcessInstanceId();
    
    Date getRemovalTime();
}
