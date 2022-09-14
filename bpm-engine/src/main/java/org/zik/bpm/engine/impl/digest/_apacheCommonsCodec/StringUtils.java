// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest._apacheCommonsCodec;

import java.io.UnsupportedEncodingException;

public class StringUtils
{
    public static final String UTF_8 = "UTF-8";
    
    public static String newString(final byte[] bytes, final String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        }
        catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }
    
    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, "UTF-8");
    }
    
    public static byte[] getBytesUtf8(final String string) {
        return getBytesUnchecked(string, "UTF-8");
    }
    
    public static byte[] getBytesUnchecked(final String string, final String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        }
        catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }
    
    private static IllegalStateException newIllegalStateException(final String charsetName, final UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }
}
