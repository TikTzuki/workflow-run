// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import java.util.Iterator;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Collection;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class CallableElement extends BaseCallableElement
{
    protected ParameterValueProvider businessKeyValueProvider;
    protected List<CallableElementParameter> inputs;
    protected List<CallableElementParameter> outputs;
    protected List<CallableElementParameter> outputsLocal;
    
    public CallableElement() {
        this.inputs = new ArrayList<CallableElementParameter>();
        this.outputs = new ArrayList<CallableElementParameter>();
        this.outputsLocal = new ArrayList<CallableElementParameter>();
    }
    
    public String getBusinessKey(final VariableScope variableScope) {
        if (this.businessKeyValueProvider == null) {
            return null;
        }
        final Object result = this.businessKeyValueProvider.getValue(variableScope);
        if (result != null && !(result instanceof String)) {
            throw new ClassCastException("Cannot cast '" + result + "' to String");
        }
        return (String)result;
    }
    
    public ParameterValueProvider getBusinessKeyValueProvider() {
        return this.businessKeyValueProvider;
    }
    
    public void setBusinessKeyValueProvider(final ParameterValueProvider businessKeyValueProvider) {
        this.businessKeyValueProvider = businessKeyValueProvider;
    }
    
    public List<CallableElementParameter> getInputs() {
        return this.inputs;
    }
    
    public void addInput(final CallableElementParameter input) {
        this.inputs.add(input);
    }
    
    public void addInputs(final List<CallableElementParameter> inputs) {
        this.inputs.addAll(inputs);
    }
    
    public VariableMap getInputVariables(final VariableScope variableScope) {
        final List<CallableElementParameter> inputs = this.getInputs();
        return this.getVariables(inputs, variableScope);
    }
    
    public List<CallableElementParameter> getOutputs() {
        return this.outputs;
    }
    
    public List<CallableElementParameter> getOutputsLocal() {
        return this.outputsLocal;
    }
    
    public void addOutput(final CallableElementParameter output) {
        this.outputs.add(output);
    }
    
    public void addOutputLocal(final CallableElementParameter output) {
        this.outputsLocal.add(output);
    }
    
    public void addOutputs(final List<CallableElementParameter> outputs) {
        this.outputs.addAll(outputs);
    }
    
    public VariableMap getOutputVariables(final VariableScope calledElementScope) {
        final List<CallableElementParameter> outputs = this.getOutputs();
        return this.getVariables(outputs, calledElementScope);
    }
    
    public VariableMap getOutputVariablesLocal(final VariableScope calledElementScope) {
        final List<CallableElementParameter> outputs = this.getOutputsLocal();
        return this.getVariables(outputs, calledElementScope);
    }
    
    protected VariableMap getVariables(final List<CallableElementParameter> params, final VariableScope variableScope) {
        final VariableMap result = Variables.createVariables();
        for (final CallableElementParameter param : params) {
            param.applyTo(variableScope, result);
        }
        return result;
    }
}
