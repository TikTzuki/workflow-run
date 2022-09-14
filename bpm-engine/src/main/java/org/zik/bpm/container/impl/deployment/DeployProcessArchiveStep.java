// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.deployment.scanning.ProcessApplicationScanningUtil;
import java.util.Collection;
import org.zik.bpm.engine.repository.ProcessApplicationDeploymentBuilder;
import org.zik.bpm.engine.RepositoryService;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.deployment.util.DeployedProcessArchive;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Map;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.container.impl.metadata.PropertyHelper;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.util.HashMap;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.engine.repository.ProcessApplicationDeployment;
import java.net.URL;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class DeployProcessArchiveStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    protected final ProcessArchiveXml processArchive;
    protected URL metaFileUrl;
    protected ProcessApplicationDeployment deployment;
    
    public DeployProcessArchiveStep(final ProcessArchiveXml parsedProcessArchive, final URL url) {
        this.processArchive = parsedProcessArchive;
        this.metaFileUrl = url;
    }
    
    @Override
    public String getName() {
        return "Deployment of process archive '" + this.processArchive.getName();
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final ClassLoader processApplicationClassloader = processApplication.getProcessApplicationClassloader();
        final ProcessEngine processEngine = this.getProcessEngine(serviceContainer, processApplication.getDefaultDeployToEngineName());
        final Map<String, byte[]> deploymentMap = new HashMap<String, byte[]>();
        final List<String> listedProcessResources = this.processArchive.getProcessResourceNames();
        for (final String processResource : listedProcessResources) {
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = processApplicationClassloader.getResourceAsStream(processResource);
                final byte[] bytes = IoUtil.readInputStream(resourceAsStream, processResource);
                deploymentMap.put(processResource, bytes);
            }
            finally {
                IoUtil.closeSilently(resourceAsStream);
            }
        }
        if (PropertyHelper.getBooleanProperty(this.processArchive.getProperties(), "isScanForProcessDefinitions", true)) {
            final String paResourceRoot = this.processArchive.getProperties().get("resourceRootPath");
            final String[] additionalResourceSuffixes = StringUtil.split(this.processArchive.getProperties().get("additionalResourceSuffixes"), ",");
            deploymentMap.putAll(this.findResources(processApplicationClassloader, paResourceRoot, additionalResourceSuffixes));
        }
        final RepositoryService repositoryService = processEngine.getRepositoryService();
        final ProcessApplicationDeploymentBuilder deploymentBuilder = repositoryService.createDeployment(processApplication.getReference());
        String deploymentName = this.processArchive.getName();
        if (deploymentName == null || deploymentName.isEmpty()) {
            deploymentName = processApplication.getName();
        }
        deploymentBuilder.name(deploymentName);
        final String tenantId = this.processArchive.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            deploymentBuilder.tenantId(tenantId);
        }
        deploymentBuilder.enableDuplicateFiltering(PropertyHelper.getBooleanProperty(this.processArchive.getProperties(), "isDeployChangedOnly", false));
        if (PropertyHelper.getBooleanProperty(this.processArchive.getProperties(), "isResumePreviousVersions", true)) {
            this.enableResumingOfPreviousVersions(deploymentBuilder);
        }
        for (final Map.Entry<String, byte[]> deploymentResource : deploymentMap.entrySet()) {
            deploymentBuilder.addInputStream((String)deploymentResource.getKey(), (InputStream)new ByteArrayInputStream(deploymentResource.getValue()));
        }
        processApplication.createDeployment(this.processArchive.getName(), deploymentBuilder);
        final Collection<String> deploymentResourceNames = deploymentBuilder.getResourceNames();
        if (!deploymentResourceNames.isEmpty()) {
            DeployProcessArchiveStep.LOG.deploymentSummary(deploymentResourceNames, deploymentName);
            this.deployment = deploymentBuilder.deploy();
            Map<String, DeployedProcessArchive> processArchiveDeploymentMap = operationContext.getAttachment("processArchiveDeploymentMap");
            if (processArchiveDeploymentMap == null) {
                processArchiveDeploymentMap = new HashMap<String, DeployedProcessArchive>();
                operationContext.addAttachment("processArchiveDeploymentMap", processArchiveDeploymentMap);
            }
            processArchiveDeploymentMap.put(this.processArchive.getName(), new DeployedProcessArchive(this.deployment));
        }
        else {
            DeployProcessArchiveStep.LOG.notCreatingPaDeployment(processApplication.getName());
        }
    }
    
    protected void enableResumingOfPreviousVersions(final ProcessApplicationDeploymentBuilder deploymentBuilder) throws IllegalArgumentException {
        deploymentBuilder.resumePreviousVersions();
        final String resumePreviousBy = this.processArchive.getProperties().get("resumePreviousBy");
        if (resumePreviousBy == null) {
            deploymentBuilder.resumePreviousVersionsBy("process-definition-key");
        }
        else {
            if (!this.isValidValueForResumePreviousBy(resumePreviousBy)) {
                final StringBuilder b = new StringBuilder();
                b.append("Illegal value passed for property ").append("resumePreviousBy");
                b.append(". Value was ").append(resumePreviousBy);
                b.append(" expected ").append("deployment-name");
                b.append(" or ").append("process-definition-key").append(".");
                throw DeployProcessArchiveStep.LOG.illegalValueForResumePreviousByProperty(b.toString());
            }
            deploymentBuilder.resumePreviousVersionsBy(resumePreviousBy);
        }
    }
    
    protected boolean isValidValueForResumePreviousBy(final String resumePreviousBy) {
        return resumePreviousBy.equals("deployment-name") || resumePreviousBy.equals("process-definition-key");
    }
    
    protected Map<String, byte[]> findResources(final ClassLoader processApplicationClassloader, final String paResourceRoot, final String[] additionalResourceSuffixes) {
        return ProcessApplicationScanningUtil.findResources(processApplicationClassloader, paResourceRoot, this.metaFileUrl, additionalResourceSuffixes);
    }
    
    @Override
    public void cancelOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final ProcessEngine processEngine = this.getProcessEngine(serviceContainer, processApplication.getDefaultDeployToEngineName());
        if (this.deployment != null && this.deployment.getProcessApplicationRegistration() != null) {
            processEngine.getManagementService().unregisterProcessApplication(this.deployment.getProcessApplicationRegistration().getDeploymentIds(), true);
        }
        if (this.deployment != null && PropertyHelper.getBooleanProperty(this.processArchive.getProperties(), "isDeleteUponUndeploy", false) && processEngine != null) {
            processEngine.getRepositoryService().deleteDeployment(this.deployment.getId(), true);
        }
    }
    
    protected ProcessEngine getProcessEngine(final PlatformServiceContainer serviceContainer) {
        return this.getProcessEngine(serviceContainer, "default");
    }
    
    protected ProcessEngine getProcessEngine(final PlatformServiceContainer serviceContainer, final String defaultDeployToProcessEngineName) {
        final String processEngineName = this.processArchive.getProcessEngineName();
        if (processEngineName != null) {
            final ProcessEngine processEngine = serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, processEngineName);
            EnsureUtil.ensureNotNull("Cannot deploy process archive '" + this.processArchive.getName() + "' to process engine '" + processEngineName + "' no such process engine exists", "processEngine", processEngine);
            return processEngine;
        }
        final ProcessEngine processEngine = serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, defaultDeployToProcessEngineName);
        EnsureUtil.ensureNotNull("Cannot deploy process archive '" + this.processArchive.getName() + "' to default process: no such process engine exists", "processEngine", processEngine);
        return processEngine;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
