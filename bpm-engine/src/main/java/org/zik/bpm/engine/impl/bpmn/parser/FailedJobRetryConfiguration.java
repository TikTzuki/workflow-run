// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.el.Expression;
import java.util.List;

public class FailedJobRetryConfiguration
{
    protected int retries;
    protected List<String> retryIntervals;
    protected Expression expression;
    
    public FailedJobRetryConfiguration(final Expression expression) {
        this.expression = expression;
    }
    
    public FailedJobRetryConfiguration(final int retries, final List<String> retryIntervals) {
        this.retries = retries;
        this.retryIntervals = retryIntervals;
    }
    
    public int getRetries() {
        return this.retries;
    }
    
    public List<String> getRetryIntervals() {
        return this.retryIntervals;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
}
