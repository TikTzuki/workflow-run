// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.Date;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class IndentityLogger extends ProcessEngineLogger
{
    public void infoUserTemporarilyLocked(final String userId, final Date lockExpirationTime) {
        this.logInfo("001", String.format("The user with id '%s' is locked. The lock will expire at %s", userId, lockExpirationTime), new Object[0]);
    }
    
    public void infoUserPermanentlyLocked(final String userId) {
        this.logInfo("002", String.format("The user with id '%s' is permanently locked.", userId), new Object[0]);
    }
}
