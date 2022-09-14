// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

import java.util.Date;

public interface Comment
{
    String getId();
    
    String getUserId();
    
    Date getTime();
    
    String getTaskId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getFullMessage();
    
    Date getRemovalTime();
}
