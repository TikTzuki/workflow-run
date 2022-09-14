// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.io.InputStream;

public interface DeploymentBuilder
{
    DeploymentBuilder addInputStream(final String p0, final InputStream p1);
    
    DeploymentBuilder addClasspathResource(final String p0);
    
    DeploymentBuilder addString(final String p0, final String p1);
    
    DeploymentBuilder addModelInstance(final String p0, final BpmnModelInstance p1);
    
    DeploymentBuilder addModelInstance(final String p0, final DmnModelInstance p1);
    
    DeploymentBuilder addModelInstance(final String p0, final CmmnModelInstance p1);
    
    DeploymentBuilder addZipInputStream(final ZipInputStream p0);
    
    DeploymentBuilder addDeploymentResources(final String p0);
    
    DeploymentBuilder addDeploymentResourceById(final String p0, final String p1);
    
    DeploymentBuilder addDeploymentResourcesById(final String p0, final List<String> p1);
    
    DeploymentBuilder addDeploymentResourceByName(final String p0, final String p1);
    
    DeploymentBuilder addDeploymentResourcesByName(final String p0, final List<String> p1);
    
    DeploymentBuilder name(final String p0);
    
    DeploymentBuilder nameFromDeployment(final String p0);
    
    @Deprecated
    DeploymentBuilder enableDuplicateFiltering();
    
    DeploymentBuilder enableDuplicateFiltering(final boolean p0);
    
    DeploymentBuilder activateProcessDefinitionsOn(final Date p0);
    
    DeploymentBuilder source(final String p0);
    
    Deployment deploy();
    
    DeploymentWithDefinitions deployWithResult();
    
    Collection<String> getResourceNames();
    
    DeploymentBuilder tenantId(final String p0);
}
