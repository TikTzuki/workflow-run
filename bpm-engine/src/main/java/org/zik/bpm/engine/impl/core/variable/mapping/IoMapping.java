// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import java.util.List;

public class IoMapping
{
    protected List<InputParameter> inputParameters;
    protected List<OutputParameter> outputParameters;
    
    public void executeInputParameters(final AbstractVariableScope variableScope) {
        for (final InputParameter inputParameter : this.getInputParameters()) {
            inputParameter.execute(variableScope);
        }
    }
    
    public void executeOutputParameters(final AbstractVariableScope variableScope) {
        for (final OutputParameter outputParameter : this.getOutputParameters()) {
            outputParameter.execute(variableScope);
        }
    }
    
    public void addInputParameter(final InputParameter param) {
        if (this.inputParameters == null) {
            this.inputParameters = new ArrayList<InputParameter>();
        }
        this.inputParameters.add(param);
    }
    
    public void addOutputParameter(final OutputParameter param) {
        if (this.outputParameters == null) {
            this.outputParameters = new ArrayList<OutputParameter>();
        }
        this.outputParameters.add(param);
    }
    
    public List<InputParameter> getInputParameters() {
        if (this.inputParameters == null) {
            return Collections.emptyList();
        }
        return this.inputParameters;
    }
    
    public void setInputParameters(final List<InputParameter> inputParameters) {
        this.inputParameters = inputParameters;
    }
    
    public List<OutputParameter> getOutputParameters() {
        if (this.outputParameters == null) {
            return Collections.emptyList();
        }
        return this.outputParameters;
    }
    
    public void setOuputParameters(final List<OutputParameter> outputParameters) {
        this.outputParameters = outputParameters;
    }
}
