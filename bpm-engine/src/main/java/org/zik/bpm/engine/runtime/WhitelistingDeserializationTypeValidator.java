// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface WhitelistingDeserializationTypeValidator extends DeserializationTypeValidator
{
    void setAllowedClasses(final String p0);
    
    void setAllowedPackages(final String p0);
}
