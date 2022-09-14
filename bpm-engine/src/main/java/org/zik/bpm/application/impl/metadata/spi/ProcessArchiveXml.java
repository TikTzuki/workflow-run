// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata.spi;

import java.util.Map;
import java.util.List;

public interface ProcessArchiveXml
{
    public static final String PROP_IS_DELETE_UPON_UNDEPLOY = "isDeleteUponUndeploy";
    public static final String PROP_IS_SCAN_FOR_PROCESS_DEFINITIONS = "isScanForProcessDefinitions";
    public static final String PROP_IS_RESUME_PREVIOUS_VERSIONS = "isResumePreviousVersions";
    public static final String PROP_RESUME_PREVIOUS_BY = "resumePreviousBy";
    public static final String PROP_IS_DEPLOY_CHANGED_ONLY = "isDeployChangedOnly";
    public static final String PROP_RESOURCE_ROOT_PATH = "resourceRootPath";
    public static final String PROP_ADDITIONAL_RESOURCE_SUFFIXES = "additionalResourceSuffixes";
    public static final String PROP_ADDITIONAL_RESOURCE_SUFFIXES_SEPARATOR = ",";
    
    String getName();
    
    String getTenantId();
    
    String getProcessEngineName();
    
    List<String> getProcessResourceNames();
    
    Map<String, String> getProperties();
}
