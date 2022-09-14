// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.el.Expression;
import java.io.Serializable;

public class SignalDefinition implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected Expression name;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name.getExpressionText();
    }
    
    public Expression getExpression() {
        return this.name;
    }
    
    public void setExpression(final Expression name) {
        this.name = name;
    }
}
