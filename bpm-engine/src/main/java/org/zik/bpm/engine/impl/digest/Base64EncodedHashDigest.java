// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import org.zik.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import org.zik.bpm.engine.ProcessEngineException;

public abstract class Base64EncodedHashDigest
{
    public String encrypt(final String password) {
        final byte[] hash = this.createByteHash(password);
        return this.encodeHash(hash);
    }
    
    public boolean check(final String password, final String encrypted) {
        return this.encrypt(password).equals(encrypted);
    }
    
    protected byte[] createByteHash(final String password) {
        final MessageDigest digest = this.createDigestInstance();
        try {
            digest.update(password.getBytes("UTF-8"));
            return digest.digest();
        }
        catch (UnsupportedEncodingException e) {
            throw new ProcessEngineException("UnsupportedEncodingException while calculating password digest");
        }
    }
    
    protected MessageDigest createDigestInstance() {
        try {
            return MessageDigest.getInstance(this.hashAlgorithmName());
        }
        catch (NoSuchAlgorithmException e) {
            throw new ProcessEngineException("Cannot lookup " + this.hashAlgorithmName() + " algorithm");
        }
    }
    
    protected String encodeHash(final byte[] hash) {
        return new String(Base64.encodeBase64(hash));
    }
    
    protected abstract String hashAlgorithmName();
}
