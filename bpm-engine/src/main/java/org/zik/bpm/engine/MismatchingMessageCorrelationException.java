// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.Map;

public class MismatchingMessageCorrelationException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public MismatchingMessageCorrelationException(final String message) {
        super(message);
    }
    
    public MismatchingMessageCorrelationException(final String messageName, final String reason) {
        this("Cannot correlate message '" + messageName + "': " + reason);
    }
    
    public MismatchingMessageCorrelationException(final String messageName, final String businessKey, final Map<String, Object> correlationKeys) {
        this("Cannot correlate message '" + messageName + "' with process instance business key '" + businessKey + "' and correlation keys " + correlationKeys);
    }
    
    public MismatchingMessageCorrelationException(final String messageName, final String businessKey, final Map<String, Object> correlationKeys, final String reason) {
        this("Cannot correlate message '" + messageName + "' with process instance business key '" + businessKey + "' and correlation keys " + correlationKeys + ": " + reason);
    }
}
