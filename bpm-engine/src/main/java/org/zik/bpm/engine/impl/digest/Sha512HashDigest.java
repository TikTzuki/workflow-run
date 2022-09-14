// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

public class Sha512HashDigest extends Base64EncodedHashDigest implements PasswordEncryptor
{
    @Override
    public String hashAlgorithmName() {
        return "SHA-512";
    }
}
