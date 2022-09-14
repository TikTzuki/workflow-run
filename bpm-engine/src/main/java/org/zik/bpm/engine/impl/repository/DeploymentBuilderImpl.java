// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Collections;
import org.zik.bpm.engine.repository.DeploymentWithDefinitions;
import org.zik.bpm.engine.repository.Deployment;
import java.util.Collection;
import java.util.List;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.zip.ZipEntry;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.zip.ZipInputStream;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.camunda.bpm.model.dmn.Dmn;
import org.zik.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.io.OutputStream;
import org.camunda.bpm.model.cmmn.Cmmn;
import java.io.ByteArrayOutputStream;
import org.zik.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.impl.util.IoUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.RepositoryServiceImpl;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import java.io.Serializable;
import org.zik.bpm.engine.repository.DeploymentBuilder;

public class DeploymentBuilderImpl implements DeploymentBuilder, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final CommandLogger LOG;
    protected transient RepositoryServiceImpl repositoryService;
    protected DeploymentEntity deployment;
    protected boolean isDuplicateFilterEnabled;
    protected boolean deployChangedOnly;
    protected Date processDefinitionsActivationDate;
    protected String nameFromDeployment;
    protected Set<String> deployments;
    protected Map<String, Set<String>> deploymentResourcesById;
    protected Map<String, Set<String>> deploymentResourcesByName;
    
    public DeploymentBuilderImpl(final RepositoryServiceImpl repositoryService) {
        this.deployment = new DeploymentEntity();
        this.isDuplicateFilterEnabled = false;
        this.deployChangedOnly = false;
        this.deployments = new HashSet<String>();
        this.deploymentResourcesById = new HashMap<String, Set<String>>();
        this.deploymentResourcesByName = new HashMap<String, Set<String>>();
        this.repositoryService = repositoryService;
    }
    
    @Override
    public DeploymentBuilder addInputStream(final String resourceName, final InputStream inputStream) {
        EnsureUtil.ensureNotNull("inputStream for resource '" + resourceName + "' is null", "inputStream", inputStream);
        final byte[] bytes = IoUtil.readInputStream(inputStream, resourceName);
        return this.addBytes(resourceName, bytes);
    }
    
    @Override
    public DeploymentBuilder addClasspathResource(final String resource) {
        final InputStream inputStream = ReflectUtil.getResourceAsStream(resource);
        EnsureUtil.ensureNotNull("resource '" + resource + "' not found", "inputStream", inputStream);
        return this.addInputStream(resource, inputStream);
    }
    
    @Override
    public DeploymentBuilder addString(final String resourceName, final String text) {
        EnsureUtil.ensureNotNull("text", (Object)text);
        final byte[] bytes = (this.repositoryService != null && this.repositoryService.getDeploymentCharset() != null) ? text.getBytes(this.repositoryService.getDeploymentCharset()) : text.getBytes();
        return this.addBytes(resourceName, bytes);
    }
    
    @Override
    public DeploymentBuilder addModelInstance(final String resourceName, final CmmnModelInstance modelInstance) {
        EnsureUtil.ensureNotNull("modelInstance", modelInstance);
        this.validateResouceName(resourceName, CmmnDeployer.CMMN_RESOURCE_SUFFIXES);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Cmmn.writeModelToStream((OutputStream)outputStream, modelInstance);
        return this.addBytes(resourceName, outputStream.toByteArray());
    }
    
    @Override
    public DeploymentBuilder addModelInstance(final String resourceName, final BpmnModelInstance modelInstance) {
        EnsureUtil.ensureNotNull("modelInstance", modelInstance);
        this.validateResouceName(resourceName, BpmnDeployer.BPMN_RESOURCE_SUFFIXES);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bpmn.writeModelToStream((OutputStream)outputStream, modelInstance);
        return this.addBytes(resourceName, outputStream.toByteArray());
    }
    
    @Override
    public DeploymentBuilder addModelInstance(final String resourceName, final DmnModelInstance modelInstance) {
        EnsureUtil.ensureNotNull("modelInstance", modelInstance);
        this.validateResouceName(resourceName, DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Dmn.writeModelToStream((OutputStream)outputStream, modelInstance);
        return this.addBytes(resourceName, outputStream.toByteArray());
    }
    
    private void validateResouceName(final String resourceName, final String[] resourceSuffixes) {
        if (!StringUtil.hasAnySuffix(resourceName, resourceSuffixes)) {
            DeploymentBuilderImpl.LOG.warnDeploymentResourceHasWrongName(resourceName, resourceSuffixes);
        }
    }
    
    protected DeploymentBuilder addBytes(final String resourceName, final byte[] bytes) {
        final ResourceEntity resource = new ResourceEntity();
        resource.setBytes(bytes);
        resource.setName(resourceName);
        this.deployment.addResource(resource);
        return this;
    }
    
    @Override
    public DeploymentBuilder addZipInputStream(final ZipInputStream zipInputStream) {
        try {
            for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
                if (!entry.isDirectory()) {
                    final String entryName = entry.getName();
                    this.addInputStream(entryName, zipInputStream);
                }
            }
        }
        catch (Exception e) {
            throw new ProcessEngineException("problem reading zip input stream", e);
        }
        return this;
    }
    
    @Override
    public DeploymentBuilder addDeploymentResources(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deployments.add(deploymentId);
        return this;
    }
    
    @Override
    public DeploymentBuilder addDeploymentResourceById(final String deploymentId, final String resourceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceId", (Object)resourceId);
        CollectionUtil.addToMapOfSets(this.deploymentResourcesById, deploymentId, resourceId);
        return this;
    }
    
    @Override
    public DeploymentBuilder addDeploymentResourcesById(final String deploymentId, final List<String> resourceIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceIds", resourceIds);
        EnsureUtil.ensureNotEmpty(NotValidException.class, "resourceIds", resourceIds);
        EnsureUtil.ensureNotContainsNull(NotValidException.class, "resourceIds", resourceIds);
        CollectionUtil.addCollectionToMapOfSets(this.deploymentResourcesById, deploymentId, resourceIds);
        return this;
    }
    
    @Override
    public DeploymentBuilder addDeploymentResourceByName(final String deploymentId, final String resourceName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceName", (Object)resourceName);
        CollectionUtil.addToMapOfSets(this.deploymentResourcesByName, deploymentId, resourceName);
        return this;
    }
    
    @Override
    public DeploymentBuilder addDeploymentResourcesByName(final String deploymentId, final List<String> resourceNames) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        EnsureUtil.ensureNotNull(NotValidException.class, "resourceNames", resourceNames);
        EnsureUtil.ensureNotEmpty(NotValidException.class, "resourceNames", resourceNames);
        EnsureUtil.ensureNotContainsNull(NotValidException.class, "resourceNames", resourceNames);
        CollectionUtil.addCollectionToMapOfSets(this.deploymentResourcesByName, deploymentId, resourceNames);
        return this;
    }
    
    @Override
    public DeploymentBuilder name(final String name) {
        if (this.nameFromDeployment != null && !this.nameFromDeployment.isEmpty()) {
            final String message = String.format("Cannot set the deployment name to '%s', because the property 'nameForDeployment' has been already set to '%s'.", name, this.nameFromDeployment);
            throw new NotValidException(message);
        }
        this.deployment.setName(name);
        return this;
    }
    
    @Override
    public DeploymentBuilder nameFromDeployment(final String deploymentId) {
        final String name = this.deployment.getName();
        if (name != null && !name.isEmpty()) {
            final String message = String.format("Cannot set the given deployment id '%s' to get the name from it, because the deployment name has been already set to '%s'.", deploymentId, name);
            throw new NotValidException(message);
        }
        this.nameFromDeployment = deploymentId;
        return this;
    }
    
    @Override
    public DeploymentBuilder enableDuplicateFiltering() {
        return this.enableDuplicateFiltering(false);
    }
    
    @Override
    public DeploymentBuilder enableDuplicateFiltering(final boolean deployChangedOnly) {
        this.isDuplicateFilterEnabled = true;
        this.deployChangedOnly = deployChangedOnly;
        return this;
    }
    
    @Override
    public DeploymentBuilder activateProcessDefinitionsOn(final Date date) {
        this.processDefinitionsActivationDate = date;
        return this;
    }
    
    @Override
    public DeploymentBuilder source(final String source) {
        this.deployment.setSource(source);
        return this;
    }
    
    @Override
    public DeploymentBuilder tenantId(final String tenantId) {
        this.deployment.setTenantId(tenantId);
        return this;
    }
    
    @Override
    public Deployment deploy() {
        return this.deployWithResult();
    }
    
    @Override
    public DeploymentWithDefinitions deployWithResult() {
        return this.repositoryService.deployWithResult(this);
    }
    
    @Override
    public Collection<String> getResourceNames() {
        if (this.deployment.getResources() == null) {
            return (Collection<String>)Collections.emptySet();
        }
        return this.deployment.getResources().keySet();
    }
    
    public DeploymentEntity getDeployment() {
        return this.deployment;
    }
    
    public boolean isDuplicateFilterEnabled() {
        return this.isDuplicateFilterEnabled;
    }
    
    public boolean isDeployChangedOnly() {
        return this.deployChangedOnly;
    }
    
    public Date getProcessDefinitionsActivationDate() {
        return this.processDefinitionsActivationDate;
    }
    
    public String getNameFromDeployment() {
        return this.nameFromDeployment;
    }
    
    public Set<String> getDeployments() {
        return this.deployments;
    }
    
    public Map<String, Set<String>> getDeploymentResourcesById() {
        return this.deploymentResourcesById;
    }
    
    public Map<String, Set<String>> getDeploymentResourcesByName() {
        return this.deploymentResourcesByName;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
