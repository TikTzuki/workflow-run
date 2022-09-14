// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

import java.util.Date;
import java.util.List;

@Deprecated
public interface Event
{
    public static final String ACTION_ADD_USER_LINK = "AddUserLink";
    public static final String ACTION_DELETE_USER_LINK = "DeleteUserLink";
    public static final String ACTION_ADD_GROUP_LINK = "AddGroupLink";
    public static final String ACTION_DELETE_GROUP_LINK = "DeleteGroupLink";
    public static final String ACTION_ADD_COMMENT = "AddComment";
    public static final String ACTION_ADD_ATTACHMENT = "AddAttachment";
    public static final String ACTION_DELETE_ATTACHMENT = "DeleteAttachment";
    
    String getAction();
    
    List<String> getMessageParts();
    
    String getMessage();
    
    String getUserId();
    
    Date getTime();
    
    String getTaskId();
    
    String getProcessInstanceId();
}
