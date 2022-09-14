// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import org.zik.bpm.engine.delegate.Expression;
import java.io.Serializable;

public class CmmnIfPartDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Expression condition;
    
    public Expression getCondition() {
        return this.condition;
    }
    
    public void setCondition(final Expression condition) {
        this.condition = condition;
    }
}
