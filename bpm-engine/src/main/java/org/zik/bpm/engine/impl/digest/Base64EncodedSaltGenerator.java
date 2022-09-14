// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import org.zik.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import java.security.SecureRandom;
import java.util.Random;

public abstract class Base64EncodedSaltGenerator implements SaltGenerator
{
    protected Random secureRandom;
    
    public Base64EncodedSaltGenerator() {
        this.secureRandom = new SecureRandom();
    }
    
    @Override
    public String generateSalt() {
        final byte[] byteSalt = this.generateByteSalt();
        return this.encodeSalt(byteSalt);
    }
    
    protected byte[] generateByteSalt() {
        final byte[] salt = new byte[(int)this.getSaltLengthInByte()];
        this.secureRandom.nextBytes(salt);
        return salt;
    }
    
    protected String encodeSalt(final byte[] salt) {
        return new String(Base64.encodeBase64(salt));
    }
    
    protected abstract Integer getSaltLengthInByte();
}
