// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.io.Serializable;

public interface HistoryEventType extends Serializable
{
    String getEntityType();
    
    String getEventName();
}
