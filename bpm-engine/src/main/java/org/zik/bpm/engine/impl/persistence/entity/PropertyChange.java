// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Date;

public class PropertyChange
{
    public static final PropertyChange EMPTY_CHANGE;
    protected String propertyName;
    protected Object orgValue;
    protected Object newValue;
    
    public PropertyChange(final String propertyName, final Object orgValue, final Object newValue) {
        this.propertyName = propertyName;
        this.orgValue = orgValue;
        this.newValue = newValue;
    }
    
    public String getPropertyName() {
        return this.propertyName;
    }
    
    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }
    
    public Object getOrgValue() {
        return this.orgValue;
    }
    
    public void setOrgValue(final Object orgValue) {
        this.orgValue = orgValue;
    }
    
    public Object getNewValue() {
        return this.newValue;
    }
    
    public void setNewValue(final Object newValue) {
        this.newValue = newValue;
    }
    
    public String getNewValueString() {
        return this.valueAsString(this.newValue);
    }
    
    public String getOrgValueString() {
        return this.valueAsString(this.orgValue);
    }
    
    protected String valueAsString(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return String.valueOf(((Date)value).getTime());
        }
        return value.toString();
    }
    
    static {
        EMPTY_CHANGE = new PropertyChange(null, null, null);
    }
}
