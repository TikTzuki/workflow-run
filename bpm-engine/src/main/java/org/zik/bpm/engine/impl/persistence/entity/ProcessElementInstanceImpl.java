// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.runtime.ProcessElementInstance;

public class ProcessElementInstanceImpl implements ProcessElementInstance
{
    protected static final String[] NO_IDS;
    protected String id;
    protected String parentActivityInstanceId;
    protected String processInstanceId;
    protected String processDefinitionId;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getParentActivityInstanceId() {
        return this.parentActivityInstanceId;
    }
    
    public void setParentActivityInstanceId(final String parentActivityInstanceId) {
        this.parentActivityInstanceId = parentActivityInstanceId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", parentActivityInstanceId=" + this.parentActivityInstanceId + ", processInstanceId=" + this.processInstanceId + ", processDefinitionId=" + this.processDefinitionId + "]";
    }
    
    static {
        NO_IDS = new String[0];
    }
}
