// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.List;
import java.util.Date;
import java.util.zip.ZipInputStream;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.io.InputStream;

public interface ProcessApplicationDeploymentBuilder extends DeploymentBuilder
{
    ProcessApplicationDeploymentBuilder resumePreviousVersions();
    
    ProcessApplicationDeploymentBuilder resumePreviousVersionsBy(final String p0);
    
    ProcessApplicationDeployment deploy();
    
    ProcessApplicationDeploymentBuilder addInputStream(final String p0, final InputStream p1);
    
    ProcessApplicationDeploymentBuilder addClasspathResource(final String p0);
    
    ProcessApplicationDeploymentBuilder addString(final String p0, final String p1);
    
    ProcessApplicationDeploymentBuilder addModelInstance(final String p0, final BpmnModelInstance p1);
    
    ProcessApplicationDeploymentBuilder addZipInputStream(final ZipInputStream p0);
    
    ProcessApplicationDeploymentBuilder name(final String p0);
    
    ProcessApplicationDeploymentBuilder nameFromDeployment(final String p0);
    
    ProcessApplicationDeploymentBuilder source(final String p0);
    
    @Deprecated
    ProcessApplicationDeploymentBuilder enableDuplicateFiltering();
    
    ProcessApplicationDeploymentBuilder enableDuplicateFiltering(final boolean p0);
    
    ProcessApplicationDeploymentBuilder activateProcessDefinitionsOn(final Date p0);
    
    ProcessApplicationDeploymentBuilder addDeploymentResources(final String p0);
    
    ProcessApplicationDeploymentBuilder addDeploymentResourceById(final String p0, final String p1);
    
    ProcessApplicationDeploymentBuilder addDeploymentResourcesById(final String p0, final List<String> p1);
    
    ProcessApplicationDeploymentBuilder addDeploymentResourceByName(final String p0, final String p1);
    
    ProcessApplicationDeploymentBuilder addDeploymentResourcesByName(final String p0, final List<String> p1);
}
