// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ConstantValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.core.variable.scope.VariableScopeLocalAdapter;

import java.util.Map;

public class CallableElementParameter {
    protected ParameterValueProvider sourceValueProvider;
    protected String target;
    protected boolean allVariables;
    protected boolean readLocal;

    public CallableElementParameter() {
        this.readLocal = false;
    }

    public Object getSource(final VariableScope variableScope) {
        if (this.sourceValueProvider instanceof ConstantValueProvider) {
            final String variableName = (String) this.sourceValueProvider.getValue(variableScope);
            return variableScope.getVariableTyped(variableName);
        }
        return this.sourceValueProvider.getValue(variableScope);
    }

    public void applyTo(VariableScope variableScope, final VariableMap variables) {
        if (this.readLocal) {
            variableScope = new VariableScopeLocalAdapter(variableScope);
        }
        if (this.allVariables) {
            final Map<String, Object> allVariables = variableScope.getVariables();
            variables.putAll((Map) allVariables);
        } else {
            final Object value = this.getSource(variableScope);
            variables.put(this.target, value);
        }
    }

    public ParameterValueProvider getSourceValueProvider() {
        return this.sourceValueProvider;
    }

    public void setSourceValueProvider(final ParameterValueProvider source) {
        this.sourceValueProvider = source;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public boolean isAllVariables() {
        return this.allVariables;
    }

    public void setAllVariables(final boolean allVariables) {
        this.allVariables = allVariables;
    }

    public void setReadLocal(final boolean readLocal) {
        this.readLocal = readLocal;
    }

    public boolean isReadLocal() {
        return this.readLocal;
    }
}
