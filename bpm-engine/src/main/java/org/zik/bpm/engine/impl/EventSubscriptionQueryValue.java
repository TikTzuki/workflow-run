// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.io.Serializable;

public class EventSubscriptionQueryValue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String eventType;
    protected String eventName;
    
    public EventSubscriptionQueryValue(final String eventName, final String eventType) {
        this.eventName = eventName;
        this.eventType = eventType;
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    public String getEventName() {
        return this.eventName;
    }
    
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }
}
