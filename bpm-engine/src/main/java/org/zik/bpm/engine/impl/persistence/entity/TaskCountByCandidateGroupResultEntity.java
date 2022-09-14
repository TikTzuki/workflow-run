// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.task.TaskCountByCandidateGroupResult;

public class TaskCountByCandidateGroupResultEntity implements TaskCountByCandidateGroupResult
{
    protected int taskCount;
    protected String groupName;
    
    @Override
    public int getTaskCount() {
        return this.taskCount;
    }
    
    @Override
    public String getGroupName() {
        return this.groupName;
    }
    
    public void setTaskCount(final int taskCount) {
        this.taskCount = taskCount;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[taskCount=" + this.taskCount + ", groupName='" + this.groupName + ']';
    }
}
