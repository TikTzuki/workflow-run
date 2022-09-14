// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.cmmn.instance.Case;
import org.camunda.bpm.model.cmmn.Cmmn;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ResourceManager;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.persistence.deploy.DeploymentFailListener;
import org.zik.bpm.application.ProcessApplicationReference;
import java.util.HashSet;
import java.util.Collections;
import org.zik.bpm.engine.impl.repository.ProcessApplicationDeploymentBuilderImpl;
import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Iterator;
import org.zik.bpm.engine.repository.Resource;
import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.engine.repository.CandidateDeployment;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentManager;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.persistence.entity.ProcessApplicationDeploymentImpl;
import org.zik.bpm.engine.repository.ProcessApplicationDeploymentBuilder;
import org.zik.bpm.engine.impl.repository.CandidateDeploymentImpl;
import java.util.Collection;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.HashMap;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.repository.DeploymentHandler;
import org.zik.bpm.engine.impl.repository.DeploymentBuilderImpl;
import org.zik.bpm.engine.impl.cfg.TransactionLogger;
import java.io.Serializable;
import org.zik.bpm.engine.repository.DeploymentWithDefinitions;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeployCmd implements Command<DeploymentWithDefinitions>, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final CommandLogger LOG;
    private static final TransactionLogger TX_LOG;
    protected DeploymentBuilderImpl deploymentBuilder;
    protected DeploymentHandler deploymentHandler;
    
    public DeployCmd(final DeploymentBuilderImpl deploymentBuilder) {
        this.deploymentBuilder = deploymentBuilder;
    }
    
    @Override
    public DeploymentWithDefinitions execute(final CommandContext commandContext) {
        if (commandContext.getProcessEngineConfiguration().isDeploymentSynchronized()) {
            synchronized (ProcessEngine.class) {
                return this.doExecute(commandContext);
            }
        }
        return this.doExecute(commandContext);
    }
    
    protected DeploymentWithDefinitions doExecute(final CommandContext commandContext) {
        final DeploymentManager deploymentManager = commandContext.getDeploymentManager();
        final ProcessEngine processEngine = commandContext.getProcessEngineConfiguration().getProcessEngine();
        this.deploymentHandler = commandContext.getProcessEngineConfiguration().getDeploymentHandlerFactory().buildDeploymentHandler(processEngine);
        final Set<String> deploymentIds = this.getAllDeploymentIds(this.deploymentBuilder);
        if (!deploymentIds.isEmpty()) {
            final String[] deploymentIdArray = deploymentIds.toArray(new String[deploymentIds.size()]);
            final List<DeploymentEntity> deployments = deploymentManager.findDeploymentsByIds(deploymentIdArray);
            this.ensureDeploymentsWithIdsExists(deploymentIds, deployments);
        }
        this.checkCreateAndReadDeployments(commandContext, deploymentIds);
        final String nameFromDeployment = this.deploymentBuilder.getNameFromDeployment();
        this.setDeploymentName(nameFromDeployment, this.deploymentBuilder, commandContext);
        final List<ResourceEntity> resources = this.getResources(this.deploymentBuilder, commandContext);
        this.addResources(resources, this.deploymentBuilder);
        final Collection<String> resourceNames = this.deploymentBuilder.getResourceNames();
        if (resourceNames == null || resourceNames.isEmpty()) {
            throw new NotValidException("No deployment resources contained to deploy.");
        }
        DeploymentEntity deploymentToRegister;
        final Map<String, ResourceEntity> resourcesToDeploy;
        final Map<String, ResourceEntity> resourcesToIgnore;
        final CandidateDeployment candidateDeployment;
        String duplicateDeploymentId;
        ProcessApplicationRegistration registration;
        final DeploymentWithDefinitions deployment = commandContext.runWithoutAuthorization(() -> {
            this.acquireExclusiveLock(commandContext);
            deploymentToRegister = this.initDeployment();
            resourcesToDeploy = this.resolveResourcesToDeploy(commandContext, deploymentToRegister);
            resourcesToIgnore = new HashMap<String, ResourceEntity>(deploymentToRegister.getResources());
            resourcesToIgnore.keySet().removeAll(resourcesToDeploy.keySet());
            candidateDeployment = CandidateDeploymentImpl.fromDeploymentEntity(deploymentToRegister);
            if (!resourcesToDeploy.isEmpty()) {
                DeployCmd.LOG.debugCreatingNewDeployment();
                deploymentToRegister.setResources(resourcesToDeploy);
                this.deploy(commandContext, deploymentToRegister);
            }
            else {
                duplicateDeploymentId = this.deploymentHandler.determineDuplicateDeployment(candidateDeployment);
                deploymentToRegister = commandContext.getDeploymentManager().findDeploymentById(duplicateDeploymentId);
            }
            this.scheduleProcessDefinitionActivation(commandContext, deploymentToRegister);
            if (this.deploymentBuilder instanceof ProcessApplicationDeploymentBuilder) {
                registration = this.registerProcessApplication(commandContext, deploymentToRegister, candidateDeployment, resourcesToIgnore.values());
                return new ProcessApplicationDeploymentImpl(deploymentToRegister, registration);
            }
            else {
                this.registerWithJobExecutor(commandContext, deploymentToRegister);
                return deploymentToRegister;
            }
        });
        this.createUserOperationLog(this.deploymentBuilder, deployment, commandContext);
        return deployment;
    }
    
    protected void acquireExclusiveLock(final CommandContext commandContext) {
        if (commandContext.getProcessEngineConfiguration().isDeploymentLockUsed()) {
            commandContext.getPropertyManager().acquireExclusiveLock();
        }
        else {
            DeployCmd.LOG.warnDisabledDeploymentLock();
        }
    }
    
    protected Map<String, ResourceEntity> resolveResourcesToDeploy(final CommandContext commandContext, final DeploymentEntity candidateDeployment) {
        Map<String, ResourceEntity> resourcesToDeploy = new HashMap<String, ResourceEntity>();
        final Map<String, ResourceEntity> candidateResources = candidateDeployment.getResources();
        if (this.deploymentBuilder.isDuplicateFilterEnabled()) {
            String source = candidateDeployment.getSource();
            if (source == null || source.isEmpty()) {
                source = "process application";
            }
            final Map<String, ResourceEntity> existingResources = commandContext.getResourceManager().findLatestResourcesByDeploymentName(candidateDeployment.getName(), candidateResources.keySet(), source, candidateDeployment.getTenantId());
            for (final ResourceEntity deployedResource : candidateResources.values()) {
                final String resourceName = deployedResource.getName();
                final ResourceEntity existingResource = existingResources.get(resourceName);
                if (existingResource == null || existingResource.isGenerated() || this.deploymentHandler.shouldDeployResource(deployedResource, existingResource)) {
                    if (!this.deploymentBuilder.isDeployChangedOnly()) {
                        resourcesToDeploy = candidateResources;
                        break;
                    }
                    resourcesToDeploy.put(resourceName, deployedResource);
                }
            }
        }
        else {
            resourcesToDeploy = candidateResources;
        }
        return resourcesToDeploy;
    }
    
    protected void deploy(final CommandContext commandContext, final DeploymentEntity deployment) {
        deployment.setNew(true);
        commandContext.getDeploymentManager().insertDeployment(deployment);
    }
    
    protected void scheduleProcessDefinitionActivation(final CommandContext commandContext, final DeploymentWithDefinitions deployment) {
        if (this.deploymentBuilder.getProcessDefinitionsActivationDate() != null) {
            final RepositoryService repositoryService = commandContext.getProcessEngineConfiguration().getRepositoryService();
            for (final ProcessDefinition processDefinition : this.getDeployedProcesses(commandContext, deployment)) {
                repositoryService.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinition.getId()).suspend();
                repositoryService.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinition.getId()).executionDate(this.deploymentBuilder.getProcessDefinitionsActivationDate()).activate();
            }
        }
    }
    
    protected ProcessApplicationRegistration registerProcessApplication(final CommandContext commandContext, final DeploymentEntity deploymentToRegister, final CandidateDeployment candidateDeployment, final Collection ignoredResources) {
        final ProcessApplicationDeploymentBuilderImpl appDeploymentBuilder = (ProcessApplicationDeploymentBuilderImpl)this.deploymentBuilder;
        final ProcessApplicationReference appReference = appDeploymentBuilder.getProcessApplicationReference();
        final Set<String> deploymentsToRegister = new HashSet<String>(Collections.singleton(deploymentToRegister.getId()));
        if (appDeploymentBuilder.isResumePreviousVersions()) {
            final String resumePreviousVersionsBy;
            final String resumePreviousBy = resumePreviousVersionsBy = appDeploymentBuilder.getResumePreviousVersionsBy();
            switch (resumePreviousVersionsBy) {
                case "deployment-name": {
                    deploymentsToRegister.addAll(this.deploymentHandler.determineDeploymentsToResumeByDeploymentName(candidateDeployment));
                    break;
                }
                default: {
                    final String[] processDefinitionKeys = this.getProcessDefinitionsFromResources(commandContext, deploymentToRegister, ignoredResources);
                    if (processDefinitionKeys.length > 0) {
                        deploymentsToRegister.addAll(this.deploymentHandler.determineDeploymentsToResumeByProcessDefinitionKey(processDefinitionKeys));
                        break;
                    }
                    break;
                }
            }
        }
        return new RegisterProcessApplicationCmd(deploymentsToRegister, appReference).execute(commandContext);
    }
    
    protected void registerWithJobExecutor(final CommandContext commandContext, final Deployment deployment) {
        try {
            new RegisterDeploymentCmd(deployment.getId()).execute(commandContext);
        }
        finally {
            final DeploymentFailListener listener = new DeploymentFailListener(deployment.getId(), commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequiresNew());
            try {
                commandContext.getTransactionContext().addTransactionListener(TransactionState.ROLLED_BACK, listener);
            }
            catch (Exception e) {
                DeployCmd.TX_LOG.debugTransactionOperation("Could not register transaction synchronization. Probably the TX has already been rolled back by application code.");
                listener.execute(commandContext);
            }
        }
    }
    
    protected void createUserOperationLog(final DeploymentBuilderImpl deploymentBuilder, final Deployment deployment, final CommandContext commandContext) {
        final UserOperationLogManager logManager = commandContext.getOperationLogManager();
        final List<PropertyChange> properties = new ArrayList<PropertyChange>();
        final PropertyChange filterDuplicate = new PropertyChange("duplicateFilterEnabled", null, deploymentBuilder.isDuplicateFilterEnabled());
        properties.add(filterDuplicate);
        if (deploymentBuilder.isDuplicateFilterEnabled()) {
            final PropertyChange deployChangedOnly = new PropertyChange("deployChangedOnly", null, deploymentBuilder.isDeployChangedOnly());
            properties.add(deployChangedOnly);
        }
        logManager.logDeploymentOperation("Create", deployment.getId(), properties);
    }
    
    protected DeploymentEntity initDeployment() {
        final DeploymentEntity deployment = this.deploymentBuilder.getDeployment();
        deployment.setDeploymentTime(ClockUtil.getCurrentTime());
        return deployment;
    }
    
    protected void setDeploymentName(final String deploymentId, final DeploymentBuilderImpl deploymentBuilder, final CommandContext commandContext) {
        if (deploymentId != null && !deploymentId.isEmpty()) {
            final DeploymentManager deploymentManager = commandContext.getDeploymentManager();
            final DeploymentEntity deployment = deploymentManager.findDeploymentById(deploymentId);
            deploymentBuilder.getDeployment().setName(deployment.getName());
        }
    }
    
    protected void addResources(final List<ResourceEntity> resources, final DeploymentBuilderImpl deploymentBuilder) {
        final DeploymentEntity deployment = deploymentBuilder.getDeployment();
        final Map<String, ResourceEntity> existingResources = deployment.getResources();
        for (final ResourceEntity resource : resources) {
            final String resourceName = resource.getName();
            if (existingResources != null && existingResources.containsKey(resourceName)) {
                final String message = String.format("Cannot add resource with id '%s' and name '%s' from deployment with id '%s' to new deployment because the new deployment contains already a resource with same name.", resource.getId(), resourceName, resource.getDeploymentId());
                throw new NotValidException(message);
            }
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getBytes());
            deploymentBuilder.addInputStream(resourceName, inputStream);
        }
    }
    
    protected List<String> getMissingElements(final Set<String> expected, final Map<String, ?> actual) {
        final List<String> missingElements = new ArrayList<String>();
        for (final String value : expected) {
            if (!actual.containsKey(value)) {
                missingElements.add(value);
            }
        }
        return missingElements;
    }
    
    protected List<ResourceEntity> getResources(final DeploymentBuilderImpl deploymentBuilder, final CommandContext commandContext) {
        final List<ResourceEntity> resources = new ArrayList<ResourceEntity>();
        final Set<String> deploymentIds = deploymentBuilder.getDeployments();
        resources.addAll(this.getResourcesByDeploymentId(deploymentIds, commandContext));
        final Map<String, Set<String>> deploymentResourcesById = deploymentBuilder.getDeploymentResourcesById();
        resources.addAll(this.getResourcesById(deploymentResourcesById, commandContext));
        final Map<String, Set<String>> deploymentResourcesByName = deploymentBuilder.getDeploymentResourcesByName();
        resources.addAll(this.getResourcesByName(deploymentResourcesByName, commandContext));
        this.checkDuplicateResourceName(resources);
        return resources;
    }
    
    protected List<ResourceEntity> getResourcesByDeploymentId(final Set<String> deploymentIds, final CommandContext commandContext) {
        final List<ResourceEntity> result = new ArrayList<ResourceEntity>();
        if (!deploymentIds.isEmpty()) {
            final DeploymentManager deploymentManager = commandContext.getDeploymentManager();
            for (final String deploymentId : deploymentIds) {
                final DeploymentEntity deployment = deploymentManager.findDeploymentById(deploymentId);
                final Map<String, ResourceEntity> resources = deployment.getResources();
                final Collection<ResourceEntity> values = resources.values();
                result.addAll(values);
            }
        }
        return result;
    }
    
    protected List<ResourceEntity> getResourcesById(final Map<String, Set<String>> resourcesById, final CommandContext commandContext) {
        final List<ResourceEntity> result = new ArrayList<ResourceEntity>();
        final ResourceManager resourceManager = commandContext.getResourceManager();
        for (final String deploymentId : resourcesById.keySet()) {
            final Set<String> resourceIds = resourcesById.get(deploymentId);
            final String[] resourceIdArray = resourceIds.toArray(new String[resourceIds.size()]);
            final List<ResourceEntity> resources = resourceManager.findResourceByDeploymentIdAndResourceIds(deploymentId, resourceIdArray);
            this.ensureResourcesWithIdsExist(deploymentId, resourceIds, resources);
            result.addAll(resources);
        }
        return result;
    }
    
    protected List<ResourceEntity> getResourcesByName(final Map<String, Set<String>> resourcesByName, final CommandContext commandContext) {
        final List<ResourceEntity> result = new ArrayList<ResourceEntity>();
        final ResourceManager resourceManager = commandContext.getResourceManager();
        for (final String deploymentId : resourcesByName.keySet()) {
            final Set<String> resourceNames = resourcesByName.get(deploymentId);
            final String[] resourceNameArray = resourceNames.toArray(new String[resourceNames.size()]);
            final List<ResourceEntity> resources = resourceManager.findResourceByDeploymentIdAndResourceNames(deploymentId, resourceNameArray);
            this.ensureResourcesWithNamesExist(deploymentId, resourceNames, resources);
            result.addAll(resources);
        }
        return result;
    }
    
    protected List<? extends ProcessDefinition> getDeployedProcesses(final CommandContext commandContext, final DeploymentWithDefinitions deployment) {
        List<? extends ProcessDefinition> deployedProcessDefinitions = deployment.getDeployedProcessDefinitions();
        if (deployedProcessDefinitions == null) {
            final ProcessDefinitionManager manager = commandContext.getProcessDefinitionManager();
            deployedProcessDefinitions = manager.findProcessDefinitionsByDeploymentId(deployment.getId());
        }
        return deployedProcessDefinitions;
    }
    
    protected String[] getProcessDefinitionsFromResources(final CommandContext commandContext, final DeploymentEntity deploymentToRegister, final Collection ignoredResources) {
        final Set<String> processDefinitionKeys = new HashSet<String>();
        processDefinitionKeys.addAll(this.parseProcessDefinitionKeys(ignoredResources));
        for (final ProcessDefinition processDefinition : this.getDeployedProcesses(commandContext, deploymentToRegister)) {
            if (processDefinition.getVersion() > 1) {
                processDefinitionKeys.add(processDefinition.getKey());
            }
        }
        return processDefinitionKeys.toArray(new String[processDefinitionKeys.size()]);
    }
    
    protected Set<String> parseProcessDefinitionKeys(final Collection<Resource> resources) {
        final Set<String> processDefinitionKeys = new HashSet<String>(resources.size());
        for (final Resource resource : resources) {
            if (this.isBpmnResource(resource)) {
                final ByteArrayInputStream byteStream = new ByteArrayInputStream(resource.getBytes());
                final BpmnModelInstance model = Bpmn.readModelFromStream((InputStream)byteStream);
                for (final Process process : model.getDefinitions().getChildElementsByType((Class)Process.class)) {
                    processDefinitionKeys.add(process.getId());
                }
            }
            else {
                if (!this.isCmmnResource(resource)) {
                    continue;
                }
                final ByteArrayInputStream byteStream = new ByteArrayInputStream(resource.getBytes());
                final CmmnModelInstance model2 = Cmmn.readModelFromStream((InputStream)byteStream);
                for (final Case cmmnCase : model2.getDefinitions().getCases()) {
                    processDefinitionKeys.add(cmmnCase.getId());
                }
            }
        }
        return processDefinitionKeys;
    }
    
    protected Set<String> getAllDeploymentIds(final DeploymentBuilderImpl deploymentBuilder) {
        final Set<String> result = new HashSet<String>();
        final String nameFromDeployment = deploymentBuilder.getNameFromDeployment();
        if (nameFromDeployment != null && !nameFromDeployment.isEmpty()) {
            result.add(nameFromDeployment);
        }
        Set<String> deployments = deploymentBuilder.getDeployments();
        result.addAll(deployments);
        deployments = deploymentBuilder.getDeploymentResourcesById().keySet();
        result.addAll(deployments);
        deployments = deploymentBuilder.getDeploymentResourcesByName().keySet();
        result.addAll(deployments);
        return result;
    }
    
    protected void checkDuplicateResourceName(final List<ResourceEntity> resources) {
        final Map<String, ResourceEntity> resourceMap = new HashMap<String, ResourceEntity>();
        for (final ResourceEntity resource : resources) {
            final String name = resource.getName();
            final ResourceEntity duplicate = resourceMap.get(name);
            if (duplicate != null) {
                final String deploymentId = resource.getDeploymentId();
                if (!deploymentId.equals(duplicate.getDeploymentId())) {
                    final String message = String.format("The deployments with id '%s' and '%s' contain a resource with same name '%s'.", deploymentId, duplicate.getDeploymentId(), name);
                    throw new NotValidException(message);
                }
            }
            resourceMap.put(name, resource);
        }
    }
    
    protected void checkCreateAndReadDeployments(final CommandContext commandContext, final Set<String> deploymentIds) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateDeployment();
            for (final String deploymentId : deploymentIds) {
                checker.checkReadDeployment(deploymentId);
            }
        }
    }
    
    protected boolean isBpmnResource(final Resource resourceEntity) {
        return StringUtil.hasAnySuffix(resourceEntity.getName(), BpmnDeployer.BPMN_RESOURCE_SUFFIXES);
    }
    
    protected boolean isCmmnResource(final Resource resourceEntity) {
        return StringUtil.hasAnySuffix(resourceEntity.getName(), CmmnDeployer.CMMN_RESOURCE_SUFFIXES);
    }
    
    protected void ensureDeploymentsWithIdsExists(final Set<String> expected, final List<DeploymentEntity> actual) {
        final Map<String, DeploymentEntity> deploymentMap = new HashMap<String, DeploymentEntity>();
        for (final DeploymentEntity deployment : actual) {
            deploymentMap.put(deployment.getId(), deployment);
        }
        final List<String> missingDeployments = this.getMissingElements(expected, deploymentMap);
        if (!missingDeployments.isEmpty()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("The following deployments are not found by id: ");
            builder.append(StringUtil.join(missingDeployments.iterator()));
            throw new NotFoundException(builder.toString());
        }
    }
    
    protected void ensureResourcesWithIdsExist(final String deploymentId, final Set<String> expectedIds, final List<ResourceEntity> actual) {
        final Map<String, ResourceEntity> resources = new HashMap<String, ResourceEntity>();
        for (final ResourceEntity resource : actual) {
            resources.put(resource.getId(), resource);
        }
        this.ensureResourcesWithKeysExist(deploymentId, expectedIds, resources, "id");
    }
    
    protected void ensureResourcesWithNamesExist(final String deploymentId, final Set<String> expectedNames, final List<ResourceEntity> actual) {
        final Map<String, ResourceEntity> resources = new HashMap<String, ResourceEntity>();
        for (final ResourceEntity resource : actual) {
            resources.put(resource.getName(), resource);
        }
        this.ensureResourcesWithKeysExist(deploymentId, expectedNames, resources, "name");
    }
    
    protected void ensureResourcesWithKeysExist(final String deploymentId, final Set<String> expectedKeys, final Map<String, ResourceEntity> actual, final String valueProperty) {
        final List<String> missingResources = this.getMissingElements(expectedKeys, actual);
        if (!missingResources.isEmpty()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("The deployment with id '");
            builder.append(deploymentId);
            builder.append("' does not contain the following resources with ");
            builder.append(valueProperty);
            builder.append(": ");
            builder.append(StringUtil.join(missingResources.iterator()));
            throw new NotFoundException(builder.toString());
        }
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
        TX_LOG = ProcessEngineLogger.TX_LOGGER;
    }
}
