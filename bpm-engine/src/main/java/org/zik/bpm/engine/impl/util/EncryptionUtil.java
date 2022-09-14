// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

public final class EncryptionUtil
{
    public static String saltPassword(final String password, String salt) {
        if (salt == null) {
            salt = "";
        }
        return password.concat(salt);
    }
}
