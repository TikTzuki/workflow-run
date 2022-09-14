// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

public class DecisionTaskActivityBehavior extends CallingTaskActivityBehavior
{
    protected String resultVariable;
    
    @Override
    protected String getTypeName() {
        return "decision task";
    }
    
    public String getResultVariable() {
        return this.resultVariable;
    }
    
    public void setResultVariable(final String resultVariable) {
        this.resultVariable = resultVariable;
    }
}
