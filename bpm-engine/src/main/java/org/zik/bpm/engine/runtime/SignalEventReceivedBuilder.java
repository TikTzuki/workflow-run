// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Map;

public interface SignalEventReceivedBuilder
{
    SignalEventReceivedBuilder setVariables(final Map<String, Object> p0);
    
    SignalEventReceivedBuilder executionId(final String p0);
    
    SignalEventReceivedBuilder tenantId(final String p0);
    
    SignalEventReceivedBuilder withoutTenantId();
    
    void send();
}
