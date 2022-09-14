// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class BaseCallableElement
{
    protected ParameterValueProvider definitionKeyValueProvider;
    protected CallableElementBinding binding;
    protected ParameterValueProvider versionValueProvider;
    protected ParameterValueProvider versionTagValueProvider;
    protected ParameterValueProvider tenantIdProvider;
    protected String deploymentId;
    
    public String getDefinitionKey(final VariableScope variableScope) {
        final Object result = this.definitionKeyValueProvider.getValue(variableScope);
        if (result != null && !(result instanceof String)) {
            throw new ClassCastException("Cannot cast '" + result + "' to String");
        }
        return (String)result;
    }
    
    public ParameterValueProvider getDefinitionKeyValueProvider() {
        return this.definitionKeyValueProvider;
    }
    
    public void setDefinitionKeyValueProvider(final ParameterValueProvider definitionKey) {
        this.definitionKeyValueProvider = definitionKey;
    }
    
    public CallableElementBinding getBinding() {
        return this.binding;
    }
    
    public void setBinding(final CallableElementBinding binding) {
        this.binding = binding;
    }
    
    public boolean isLatestBinding() {
        final CallableElementBinding binding = this.getBinding();
        return binding == null || CallableElementBinding.LATEST.equals(binding);
    }
    
    public boolean isDeploymentBinding() {
        final CallableElementBinding binding = this.getBinding();
        return CallableElementBinding.DEPLOYMENT.equals(binding);
    }
    
    public boolean isVersionBinding() {
        final CallableElementBinding binding = this.getBinding();
        return CallableElementBinding.VERSION.equals(binding);
    }
    
    public boolean isVersionTagBinding() {
        final CallableElementBinding binding = this.getBinding();
        return CallableElementBinding.VERSION_TAG.equals(binding);
    }
    
    public Integer getVersion(final VariableScope variableScope) {
        final Object result = this.versionValueProvider.getValue(variableScope);
        if (result == null) {
            return null;
        }
        if (result instanceof String) {
            return Integer.valueOf((String)result);
        }
        if (result instanceof Integer) {
            return (Integer)result;
        }
        throw new ProcessEngineException("It is not possible to transform '" + result + "' into an integer.");
    }
    
    public ParameterValueProvider getVersionValueProvider() {
        return this.versionValueProvider;
    }
    
    public void setVersionValueProvider(final ParameterValueProvider version) {
        this.versionValueProvider = version;
    }
    
    public String getVersionTag(final VariableScope variableScope) {
        final Object result = this.versionTagValueProvider.getValue(variableScope);
        if (result == null) {
            return null;
        }
        if (result instanceof String) {
            return (String)result;
        }
        throw new ProcessEngineException("It is not possible to transform '" + result + "' into a string.");
    }
    
    public ParameterValueProvider getVersionTagValueProvider() {
        return this.versionTagValueProvider;
    }
    
    public void setVersionTagValueProvider(final ParameterValueProvider version) {
        this.versionTagValueProvider = version;
    }
    
    public void setTenantIdProvider(final ParameterValueProvider tenantIdProvider) {
        this.tenantIdProvider = tenantIdProvider;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public String getDefinitionTenantId(final VariableScope variableScope, final String defaultTenantId) {
        if (this.tenantIdProvider != null) {
            return (String)this.tenantIdProvider.getValue(variableScope);
        }
        return defaultTenantId;
    }
    
    public ParameterValueProvider getTenantIdProvider() {
        return this.tenantIdProvider;
    }
    
    public boolean hasDynamicReferences() {
        return (this.tenantIdProvider != null && this.tenantIdProvider.isDynamic()) || this.definitionKeyValueProvider.isDynamic() || this.versionValueProvider.isDynamic() || this.versionTagValueProvider.isDynamic();
    }
    
    public enum CallableElementBinding
    {
        LATEST("latest"), 
        DEPLOYMENT("deployment"), 
        VERSION("version"), 
        VERSION_TAG("versionTag");
        
        private String value;
        
        private CallableElementBinding(final String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
