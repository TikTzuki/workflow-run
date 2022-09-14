// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.repository.DeploymentBuilder;
import org.zik.bpm.engine.repository.Deployment;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.io.InputStream;
import java.util.Date;
import org.zik.bpm.engine.repository.ProcessApplicationDeployment;
import org.zik.bpm.engine.impl.RepositoryServiceImpl;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.repository.ProcessApplicationDeploymentBuilder;

public class ProcessApplicationDeploymentBuilderImpl extends DeploymentBuilderImpl implements ProcessApplicationDeploymentBuilder
{
    private static final long serialVersionUID = 1L;
    protected final ProcessApplicationReference processApplicationReference;
    protected boolean isResumePreviousVersions;
    protected String resumePreviousVersionsBy;
    
    public ProcessApplicationDeploymentBuilderImpl(final RepositoryServiceImpl repositoryService, final ProcessApplicationReference reference) {
        super(repositoryService);
        this.isResumePreviousVersions = false;
        this.resumePreviousVersionsBy = "process-definition-key";
        this.processApplicationReference = reference;
        this.source("process application");
    }
    
    @Override
    public ProcessApplicationDeploymentBuilder resumePreviousVersions() {
        this.isResumePreviousVersions = true;
        return this;
    }
    
    @Override
    public ProcessApplicationDeploymentBuilder resumePreviousVersionsBy(final String resumePreviousVersionsBy) {
        this.resumePreviousVersionsBy = resumePreviousVersionsBy;
        return this;
    }
    
    @Override
    public ProcessApplicationDeployment deploy() {
        return (ProcessApplicationDeployment)super.deploy();
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl activateProcessDefinitionsOn(final Date date) {
        return (ProcessApplicationDeploymentBuilderImpl)super.activateProcessDefinitionsOn(date);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addInputStream(final String resourceName, final InputStream inputStream) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addInputStream(resourceName, inputStream);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addClasspathResource(final String resource) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addClasspathResource(resource);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addString(final String resourceName, final String text) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addString(resourceName, text);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addModelInstance(final String resourceName, final BpmnModelInstance modelInstance) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addModelInstance(resourceName, modelInstance);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addZipInputStream(final ZipInputStream zipInputStream) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addZipInputStream(zipInputStream);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl name(final String name) {
        return (ProcessApplicationDeploymentBuilderImpl)super.name(name);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl tenantId(final String tenantId) {
        return (ProcessApplicationDeploymentBuilderImpl)super.tenantId(tenantId);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl nameFromDeployment(final String deploymentId) {
        return (ProcessApplicationDeploymentBuilderImpl)super.nameFromDeployment(deploymentId);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl source(final String source) {
        return (ProcessApplicationDeploymentBuilderImpl)super.source(source);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl enableDuplicateFiltering() {
        return (ProcessApplicationDeploymentBuilderImpl)super.enableDuplicateFiltering();
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl enableDuplicateFiltering(final boolean deployChangedOnly) {
        return (ProcessApplicationDeploymentBuilderImpl)super.enableDuplicateFiltering(deployChangedOnly);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addDeploymentResources(final String deploymentId) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addDeploymentResources(deploymentId);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addDeploymentResourceById(final String deploymentId, final String resourceId) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addDeploymentResourceById(deploymentId, resourceId);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addDeploymentResourcesById(final String deploymentId, final List<String> resourceIds) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addDeploymentResourcesById(deploymentId, resourceIds);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addDeploymentResourceByName(final String deploymentId, final String resourceName) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addDeploymentResourceByName(deploymentId, resourceName);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilderImpl addDeploymentResourcesByName(final String deploymentId, final List<String> resourceNames) {
        return (ProcessApplicationDeploymentBuilderImpl)super.addDeploymentResourcesByName(deploymentId, resourceNames);
    }
    
    public boolean isResumePreviousVersions() {
        return this.isResumePreviousVersions;
    }
    
    public ProcessApplicationReference getProcessApplicationReference() {
        return this.processApplicationReference;
    }
    
    public String getResumePreviousVersionsBy() {
        return this.resumePreviousVersionsBy;
    }
}
