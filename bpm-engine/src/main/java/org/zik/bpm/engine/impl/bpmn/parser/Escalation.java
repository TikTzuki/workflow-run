// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

public class Escalation
{
    protected final String id;
    protected String name;
    protected String escalationCode;
    
    public Escalation(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getEscalationCode() {
        return this.escalationCode;
    }
    
    public void setEscalationCode(final String escalationCode) {
        this.escalationCode = escalationCode;
    }
    
    public String getId() {
        return this.id;
    }
}
