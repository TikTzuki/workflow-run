// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class LocalMessages
{
    private static final String BUNDLE_NAME = "org.camunda.bpm.engine.impl.juel.misc.LocalStrings";
    private static final ResourceBundle RESOURCE_BUNDLE;
    
    public static String get(final String key, final Object... args) {
        String template = null;
        try {
            template = LocalMessages.RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e) {
            final StringBuilder b = new StringBuilder();
            try {
                b.append(LocalMessages.RESOURCE_BUNDLE.getString("message.unknown"));
                b.append(": ");
            }
            catch (MissingResourceException ex) {}
            b.append(key);
            if (args != null && args.length > 0) {
                b.append("(");
                b.append(args[0]);
                for (int i = 1; i < args.length; ++i) {
                    b.append(", ");
                    b.append(args[i]);
                }
                b.append(")");
            }
            return b.toString();
        }
        return MessageFormat.format(template, args);
    }
    
    static {
        RESOURCE_BUNDLE = ResourceBundle.getBundle("org.camunda.bpm.engine.impl.juel.misc.LocalStrings");
    }
}
