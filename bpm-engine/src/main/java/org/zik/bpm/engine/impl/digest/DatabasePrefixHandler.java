// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabasePrefixHandler
{
    protected Pattern pattern;
    
    public DatabasePrefixHandler() {
        this.pattern = Pattern.compile("^\\{(.*?)\\}");
    }
    
    public String generatePrefix(final String algorithmName) {
        return "{" + algorithmName + "}";
    }
    
    public String retrieveAlgorithmName(final String encryptedPasswordWithPrefix) {
        final Matcher matcher = this.pattern.matcher(encryptedPasswordWithPrefix);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    public String removePrefix(final String encryptedPasswordWithPrefix) {
        final int index = encryptedPasswordWithPrefix.indexOf("}");
        if (!encryptedPasswordWithPrefix.startsWith("{") || index < 0) {
            return null;
        }
        return encryptedPasswordWithPrefix.substring(index + 1);
    }
}
