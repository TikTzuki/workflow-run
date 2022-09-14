// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;

public class TreeBuilderException extends ELException
{
    private static final long serialVersionUID = 1L;
    private final String expression;
    private final int position;
    private final String encountered;
    private final String expected;
    
    public TreeBuilderException(final String expression, final int position, final String encountered, final String expected, final String message) {
        super(LocalMessages.get("error.build", expression, message));
        this.expression = expression;
        this.position = position;
        this.encountered = encountered;
        this.expected = expected;
    }
    
    public String getExpression() {
        return this.expression;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public String getEncountered() {
        return this.encountered;
    }
    
    public String getExpected() {
        return this.expected;
    }
}
