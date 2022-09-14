// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import org.zik.bpm.engine.ProcessEngineException;
import java.util.Collection;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class SecurityLogger extends ProcessEngineLogger
{
    public ProcessEngineException cannotResolveAlgorithmPrefixFromGivenPasswordException(final String resolvedHashAlgorithmName, final Collection<String> availableHashAlgorithmNames) {
        return new PasswordEncryptionException(this.exceptionMessage("001", "Could not resolve hash algorithm name of a hashed password. Resolved hash algorithm name {}. Available hash algorithms {}", new Object[] { resolvedHashAlgorithmName, availableHashAlgorithmNames }));
    }
    
    public ProcessEngineException hashAlgorithmForPasswordEncryptionAlreadyAvailableException(final String hashAlgorithmName) {
        return new PasswordEncryptionException(this.exceptionMessage("002", "Hash algorithm with the name '{}' was already added. The algorithm cannot be added twice!", new Object[] { hashAlgorithmName }));
    }
}
