// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.Expression;
import java.io.Serializable;

@Deprecated
public class VariableDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String type;
    protected String sourceVariableName;
    protected Expression sourceExpression;
    protected String destinationVariableName;
    protected Expression destinationExpression;
    protected String link;
    protected Expression linkExpression;
    
    public void initialize(final VariableScope innerScopeInstance, final VariableScope outerScopeInstance) {
        if (this.sourceVariableName != null) {
            if (!outerScopeInstance.hasVariable(this.sourceVariableName)) {
                throw new ProcessEngineException("Couldn't create variable '" + this.destinationVariableName + "', since the source variable '" + this.sourceVariableName + "does not exist");
            }
            final Object value = outerScopeInstance.getVariable(this.sourceVariableName);
            innerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.sourceExpression != null) {
            final Object value = this.sourceExpression.getValue(outerScopeInstance);
            innerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.link != null) {
            if (!outerScopeInstance.hasVariable(this.sourceVariableName)) {
                throw new ProcessEngineException("Couldn't create variable '" + this.destinationVariableName + "', since the source variable '" + this.sourceVariableName + "does not exist");
            }
            final Object value = outerScopeInstance.getVariable(this.sourceVariableName);
            innerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.linkExpression != null) {
            final Object value = this.sourceExpression.getValue(outerScopeInstance);
            innerScopeInstance.setVariable(this.destinationVariableName, value);
        }
    }
    
    public void destroy(final VariableScope innerScopeInstance, final VariableScope outerScopeInstance) {
        if (this.destinationVariableName != null) {
            if (!innerScopeInstance.hasVariable(this.sourceVariableName)) {
                throw new ProcessEngineException("Couldn't destroy variable " + this.sourceVariableName + ", since it does not exist");
            }
            final Object value = innerScopeInstance.getVariable(this.sourceVariableName);
            outerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.destinationExpression != null) {
            final Object value = this.destinationExpression.getValue(innerScopeInstance);
            outerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.link != null) {
            if (!innerScopeInstance.hasVariable(this.sourceVariableName)) {
                throw new ProcessEngineException("Couldn't destroy variable " + this.sourceVariableName + ", since it does not exist");
            }
            final Object value = innerScopeInstance.getVariable(this.sourceVariableName);
            outerScopeInstance.setVariable(this.destinationVariableName, value);
        }
        if (this.linkExpression != null) {
            final Object value = this.sourceExpression.getValue(innerScopeInstance);
            outerScopeInstance.setVariable(this.destinationVariableName, value);
        }
    }
    
    public VariableDeclaration(final String name, final String type) {
        this.name = name;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "VariableDeclaration[" + this.name + ":" + this.type + "]";
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getSourceVariableName() {
        return this.sourceVariableName;
    }
    
    public void setSourceVariableName(final String sourceVariableName) {
        this.sourceVariableName = sourceVariableName;
    }
    
    public Expression getSourceExpression() {
        return this.sourceExpression;
    }
    
    public void setSourceExpression(final Expression sourceExpression) {
        this.sourceExpression = sourceExpression;
    }
    
    public String getDestinationVariableName() {
        return this.destinationVariableName;
    }
    
    public void setDestinationVariableName(final String destinationVariableName) {
        this.destinationVariableName = destinationVariableName;
    }
    
    public Expression getDestinationExpression() {
        return this.destinationExpression;
    }
    
    public void setDestinationExpression(final Expression destinationExpression) {
        this.destinationExpression = destinationExpression;
    }
    
    public String getLink() {
        return this.link;
    }
    
    public void setLink(final String link) {
        this.link = link;
    }
    
    public Expression getLinkExpression() {
        return this.linkExpression;
    }
    
    public void setLinkExpression(final Expression linkExpression) {
        this.linkExpression = linkExpression;
    }
}
