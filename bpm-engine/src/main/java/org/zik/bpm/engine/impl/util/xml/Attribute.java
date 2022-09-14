// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

public class Attribute
{
    protected String name;
    protected String value;
    protected String uri;
    
    public Attribute(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public Attribute(final String name, final String value, final String uri) {
        this(name, value);
        this.uri = uri;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public void setUri(final String uri) {
        this.uri = uri;
    }
}
