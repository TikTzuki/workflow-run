// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

public interface PasswordEncryptor
{
    String encrypt(final String p0);
    
    boolean check(final String p0, final String p1);
    
    String hashAlgorithmName();
}
