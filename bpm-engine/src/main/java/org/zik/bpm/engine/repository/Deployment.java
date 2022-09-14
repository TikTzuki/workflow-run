// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Date;

public interface Deployment
{
    String getId();
    
    String getName();
    
    Date getDeploymentTime();
    
    String getSource();
    
    String getTenantId();
}
