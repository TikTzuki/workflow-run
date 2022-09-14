// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.MessageCorrelationBuilderImpl;
import java.util.Map;

public class CorrelationSet
{
    protected final String businessKey;
    protected final Map<String, Object> correlationKeys;
    protected final Map<String, Object> localCorrelationKeys;
    protected final String processInstanceId;
    protected final String processDefinitionId;
    protected final String tenantId;
    protected final boolean isTenantIdSet;
    protected final boolean isExecutionsOnly;
    
    public CorrelationSet(final MessageCorrelationBuilderImpl builder) {
        this.businessKey = builder.getBusinessKey();
        this.processInstanceId = builder.getProcessInstanceId();
        this.correlationKeys = builder.getCorrelationProcessInstanceVariables();
        this.localCorrelationKeys = builder.getCorrelationLocalVariables();
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.tenantId = builder.getTenantId();
        this.isTenantIdSet = builder.isTenantIdSet();
        this.isExecutionsOnly = builder.isExecutionsOnly();
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public Map<String, Object> getCorrelationKeys() {
        return this.correlationKeys;
    }
    
    public Map<String, Object> getLocalCorrelationKeys() {
        return this.localCorrelationKeys;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isExecutionsOnly() {
        return this.isExecutionsOnly;
    }
    
    @Override
    public String toString() {
        return "CorrelationSet [businessKey=" + this.businessKey + ", processInstanceId=" + this.processInstanceId + ", processDefinitionId=" + this.processDefinitionId + ", correlationKeys=" + this.correlationKeys + ", localCorrelationKeys=" + this.localCorrelationKeys + ", tenantId=" + this.tenantId + ", isTenantIdSet=" + this.isTenantIdSet + ", isExecutionsOnly=" + this.isExecutionsOnly + "]";
    }
}
