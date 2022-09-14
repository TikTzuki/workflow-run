// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import java.util.Map;
import java.util.List;

public interface ProcessApplicationInfo
{
    public static final String PROP_SERVLET_CONTEXT_PATH = "servletContextPath";
    
    String getName();
    
    List<ProcessApplicationDeploymentInfo> getDeploymentInfo();
    
    Map<String, String> getProperties();
}
