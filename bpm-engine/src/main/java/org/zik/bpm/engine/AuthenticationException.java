// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.Date;

public class AuthenticationException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public AuthenticationException(final String userId) {
        super("The user with id '" + userId + "' is permanently locked. Please contact your admin to unlock the account.");
    }
    
    public AuthenticationException(final String userId, final Date lockExpirationDate) {
        super("The user with id '" + userId + "' is locked. The lock will expire at " + lockExpirationDate);
    }
    
    public AuthenticationException(final String userId, final String message) {
        super("The user with id '" + userId + "' tries to login without success. " + message);
    }
}
