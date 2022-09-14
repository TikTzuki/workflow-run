// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

public class FieldDeclaration
{
    protected String name;
    protected String type;
    protected Object value;
    
    public FieldDeclaration(final String name, final String type, final Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public FieldDeclaration() {
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
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
}
