// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

public final class EventType
{
    public static final EventType MESSAGE;
    public static final EventType SIGNAL;
    public static final EventType COMPENSATE;
    public static final EventType CONDITONAL;
    private final String name;
    
    private EventType(final String name) {
        this.name = name;
    }
    
    public String name() {
        return this.name;
    }
    
    static {
        MESSAGE = new EventType("message");
        SIGNAL = new EventType("signal");
        COMPENSATE = new EventType("compensate");
        CONDITONAL = new EventType("conditional");
    }
}
