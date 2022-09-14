// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.telemetry;

import java.util.Map;

public interface LicenseKeyData
{
    String getCustomer();
    
    String getType();
    
    String getValidUntil();
    
    Boolean isUnlimited();
    
    Map<String, String> getFeatures();
    
    String getRaw();
}
