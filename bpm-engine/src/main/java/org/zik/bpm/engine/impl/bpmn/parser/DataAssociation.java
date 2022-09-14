// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.delegate.Expression;

public class DataAssociation
{
    protected String source;
    protected Expression sourceExpression;
    protected String target;
    protected String variables;
    protected Expression businessKeyExpression;
    
    protected DataAssociation(final String source, final String target) {
        this.source = source;
        this.target = target;
    }
    
    protected DataAssociation(final Expression sourceExpression, final String target) {
        this.sourceExpression = sourceExpression;
        this.target = target;
    }
    
    protected DataAssociation(final String variables) {
        this.variables = variables;
    }
    
    protected DataAssociation(final Expression businessKeyExpression) {
        this.businessKeyExpression = businessKeyExpression;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public Expression getSourceExpression() {
        return this.sourceExpression;
    }
    
    public String getVariables() {
        return this.variables;
    }
    
    public Expression getBusinessKeyExpression() {
        return this.businessKeyExpression;
    }
}
