// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.spi;

import java.util.Collection;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;

public class DeploymentOperation
{
    private static final ContainerIntegrationLogger LOG;
    protected final String name;
    protected final PlatformServiceContainer serviceContainer;
    protected final List<DeploymentOperationStep> steps;
    protected final List<DeploymentOperationStep> successfulSteps;
    protected List<String> installedServices;
    protected Map<String, Object> attachments;
    protected boolean isRollbackOnFailure;
    protected DeploymentOperationStep currentStep;
    
    public DeploymentOperation(final String name, final PlatformServiceContainer container, final List<DeploymentOperationStep> steps) {
        this.successfulSteps = new ArrayList<DeploymentOperationStep>();
        this.installedServices = new ArrayList<String>();
        this.attachments = new HashMap<String, Object>();
        this.isRollbackOnFailure = true;
        this.name = name;
        this.serviceContainer = container;
        this.steps = steps;
    }
    
    public <S> S getAttachment(final String name) {
        return (S)this.attachments.get(name);
    }
    
    public void addAttachment(final String name, final Object value) {
        this.attachments.put(name, value);
    }
    
    public void addStep(final DeploymentOperationStep step) {
        if (this.currentStep != null) {
            this.steps.add(this.steps.indexOf(this.currentStep) + 1, step);
        }
        else {
            this.steps.add(step);
        }
    }
    
    public void serviceAdded(final String serviceName) {
        this.installedServices.add(serviceName);
    }
    
    public PlatformServiceContainer getServiceContainer() {
        return this.serviceContainer;
    }
    
    public void execute() {
        while (!this.steps.isEmpty()) {
            this.currentStep = this.steps.remove(0);
            try {
                DeploymentOperation.LOG.debugPerformOperationStep(this.currentStep.getName());
                this.currentStep.performOperationStep(this);
                this.successfulSteps.add(this.currentStep);
                DeploymentOperation.LOG.debugSuccessfullyPerformedOperationStep(this.currentStep.getName());
            }
            catch (Exception e3) {
                if (this.isRollbackOnFailure) {
                    try {
                        this.rollbackOperation();
                    }
                    catch (Exception e2) {
                        DeploymentOperation.LOG.exceptionWhileRollingBackOperation(e2);
                    }
                    throw DeploymentOperation.LOG.exceptionWhilePerformingOperationStep(this.name, this.currentStep.getName(), e3);
                }
                DeploymentOperation.LOG.exceptionWhilePerformingOperationStep(this.currentStep.getName(), e3);
            }
        }
    }
    
    protected void rollbackOperation() {
        for (final DeploymentOperationStep step : this.successfulSteps) {
            try {
                step.cancelOperationStep(this);
            }
            catch (Exception e) {
                DeploymentOperation.LOG.exceptionWhileRollingBackOperation(e);
            }
        }
        for (final String serviceName : this.installedServices) {
            try {
                this.serviceContainer.stopService(serviceName);
            }
            catch (Exception e) {
                DeploymentOperation.LOG.exceptionWhileStopping("service", serviceName, e);
            }
        }
    }
    
    public List<String> getInstalledServices() {
        return this.installedServices;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
    
    public static class DeploymentOperationBuilder
    {
        protected PlatformServiceContainer container;
        protected String name;
        protected boolean isUndeploymentOperation;
        protected List<DeploymentOperationStep> steps;
        protected Map<String, Object> initialAttachments;
        
        public DeploymentOperationBuilder(final PlatformServiceContainer container, final String name) {
            this.isUndeploymentOperation = false;
            this.steps = new ArrayList<DeploymentOperationStep>();
            this.initialAttachments = new HashMap<String, Object>();
            this.container = container;
            this.name = name;
        }
        
        public DeploymentOperationBuilder addStep(final DeploymentOperationStep step) {
            this.steps.add(step);
            return this;
        }
        
        public DeploymentOperationBuilder addSteps(final Collection<DeploymentOperationStep> steps) {
            for (final DeploymentOperationStep step : steps) {
                this.addStep(step);
            }
            return this;
        }
        
        public DeploymentOperationBuilder addAttachment(final String name, final Object value) {
            this.initialAttachments.put(name, value);
            return this;
        }
        
        public DeploymentOperationBuilder setUndeploymentOperation() {
            this.isUndeploymentOperation = true;
            return this;
        }
        
        public void execute() {
            final DeploymentOperation operation = new DeploymentOperation(this.name, this.container, this.steps);
            operation.isRollbackOnFailure = !this.isUndeploymentOperation;
            operation.attachments.putAll(this.initialAttachments);
            this.container.executeDeploymentOperation(operation);
        }
    }
}
