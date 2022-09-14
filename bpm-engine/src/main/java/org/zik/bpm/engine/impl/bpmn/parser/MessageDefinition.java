// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.el.Expression;

public class MessageDefinition
{
    protected String id;
    protected Expression name;
    
    public MessageDefinition(final String id, final Expression name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Expression getExpression() {
        return this.name;
    }
}
