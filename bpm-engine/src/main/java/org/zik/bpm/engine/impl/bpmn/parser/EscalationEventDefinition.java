// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.pvm.PvmActivity;

public class EscalationEventDefinition
{
    protected final PvmActivity escalationHandler;
    protected final boolean cancelActivity;
    protected String escalationCode;
    protected String escalationCodeVariable;
    
    public EscalationEventDefinition(final PvmActivity escalationHandler, final boolean cancelActivity) {
        this.escalationHandler = escalationHandler;
        this.cancelActivity = cancelActivity;
    }
    
    public String getEscalationCode() {
        return this.escalationCode;
    }
    
    public PvmActivity getEscalationHandler() {
        return this.escalationHandler;
    }
    
    public boolean isCancelActivity() {
        return this.cancelActivity;
    }
    
    public void setEscalationCode(final String escalationCode) {
        this.escalationCode = escalationCode;
    }
    
    public String getEscalationCodeVariable() {
        return this.escalationCodeVariable;
    }
    
    public void setEscalationCodeVariable(final String escalationCodeVariable) {
        this.escalationCodeVariable = escalationCodeVariable;
    }
}
