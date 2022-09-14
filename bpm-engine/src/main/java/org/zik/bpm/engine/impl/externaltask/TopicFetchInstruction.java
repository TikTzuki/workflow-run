// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.externaltask;

import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import org.zik.bpm.engine.impl.QueryOperator;
import java.util.Map;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.QueryVariableValue;
import java.util.List;
import java.io.Serializable;

public class TopicFetchInstruction implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String topicName;
    protected String businessKey;
    protected String processDefinitionId;
    protected String[] processDefinitionIds;
    protected String processDefinitionKey;
    protected String[] processDefinitionKeys;
    protected String processDefinitionVersionTag;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected List<String> variablesToFetch;
    protected List<QueryVariableValue> filterVariables;
    protected long lockDuration;
    protected boolean deserializeVariables;
    protected boolean localVariables;
    protected boolean includeExtensionProperties;
    
    public TopicFetchInstruction(final String topicName, final long lockDuration) {
        this.isTenantIdSet = false;
        this.deserializeVariables = false;
        this.localVariables = false;
        this.includeExtensionProperties = false;
        this.topicName = topicName;
        this.lockDuration = lockDuration;
        this.filterVariables = new ArrayList<QueryVariableValue>();
    }
    
    public List<String> getVariablesToFetch() {
        return this.variablesToFetch;
    }
    
    public void setVariablesToFetch(final List<String> variablesToFetch) {
        this.variablesToFetch = variablesToFetch;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionIds(final String[] processDefinitionIds) {
        this.processDefinitionIds = processDefinitionIds;
    }
    
    public String[] getProcessDefinitionIds() {
        return this.processDefinitionIds;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKeys(final String[] processDefinitionKeys) {
        this.processDefinitionKeys = processDefinitionKeys;
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
    
    public void setProcessDefinitionVersionTag(final String processDefinitionVersionTag) {
        this.processDefinitionVersionTag = processDefinitionVersionTag;
    }
    
    public String getProcessDefinitionVersionTag() {
        return this.processDefinitionVersionTag;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public void setTenantIdSet(final boolean isTenantIdSet) {
        this.isTenantIdSet = isTenantIdSet;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    public void setTenantIds(final String[] tenantIds) {
        this.isTenantIdSet = true;
        this.tenantIds = tenantIds;
    }
    
    public List<QueryVariableValue> getFilterVariables() {
        return this.filterVariables;
    }
    
    public void setFilterVariables(final Map<String, Object> filterVariables) {
        for (final Map.Entry<String, Object> filter : filterVariables.entrySet()) {
            final QueryVariableValue variableValue = new QueryVariableValue(filter.getKey(), filter.getValue(), null, false);
            this.filterVariables.add(variableValue);
        }
    }
    
    public void addFilterVariable(final String name, final Object value) {
        final QueryVariableValue variableValue = new QueryVariableValue(name, value, QueryOperator.EQUALS, true);
        this.filterVariables.add(variableValue);
    }
    
    public Long getLockDuration() {
        return this.lockDuration;
    }
    
    public String getTopicName() {
        return this.topicName;
    }
    
    public boolean isDeserializeVariables() {
        return this.deserializeVariables;
    }
    
    public void setDeserializeVariables(final boolean deserializeVariables) {
        this.deserializeVariables = deserializeVariables;
    }
    
    public void ensureVariablesInitialized() {
        if (!this.filterVariables.isEmpty()) {
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
            final String dbType = processEngineConfiguration.getDatabaseType();
            for (final QueryVariableValue queryVariableValue : this.filterVariables) {
                queryVariableValue.initialize(variableSerializers, dbType);
            }
        }
    }
    
    public boolean isLocalVariables() {
        return this.localVariables;
    }
    
    public void setLocalVariables(final boolean localVariables) {
        this.localVariables = localVariables;
    }
    
    public boolean isIncludeExtensionProperties() {
        return this.includeExtensionProperties;
    }
    
    public void setIncludeExtensionProperties(final boolean includeExtensionProperties) {
        this.includeExtensionProperties = includeExtensionProperties;
    }
}
