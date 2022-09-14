// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import org.zik.bpm.engine.impl.telemetry.reporter.TelemetryReporter;
import org.zik.bpm.engine.impl.telemetry.dto.TelemetryDataImpl;
import org.zik.bpm.engine.impl.telemetry.dto.LicenseKeyDataImpl;
import java.util.UUID;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.TelemetryUtil;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingResult;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.persistence.entity.EverLivingJobEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.OptimisticLockingListener;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.ProcessEngineBootstrapCommand;

public class BootstrapEngineCommand implements ProcessEngineBootstrapCommand
{
    private static final EnginePersistenceLogger LOG;
    protected static final String TELEMETRY_PROPERTY_NAME = "camunda.telemetry.enabled";
    protected static final String INSTALLATION_PROPERTY_NAME = "camunda.installation.id";
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.initializeInstallationId(commandContext);
        this.checkDeploymentLockExists(commandContext);
        if (this.isHistoryCleanupEnabled(commandContext)) {
            this.checkHistoryCleanupLockExists(commandContext);
            this.createHistoryCleanupJob(commandContext);
        }
        this.initializeTelemetryProperty(commandContext);
        this.updateTelemetryData(commandContext);
        this.startTelemetryReporter(commandContext);
        return null;
    }
    
    protected void createHistoryCleanupJob(final CommandContext commandContext) {
        if (Context.getProcessEngineConfiguration().getManagementService().getTableMetaData("ACT_RU_JOB") != null) {
            commandContext.getDbEntityManager().registerOptimisticLockingListener(new OptimisticLockingListener() {
                @Override
                public Class<? extends DbEntity> getEntityType() {
                    return EverLivingJobEntity.class;
                }
                
                @Override
                public OptimisticLockingResult failedOperation(final DbOperation operation) {
                    return OptimisticLockingResult.IGNORE;
                }
            });
            Context.getProcessEngineConfiguration().getHistoryService().cleanUpHistoryAsync();
        }
    }
    
    public void checkDeploymentLockExists(final CommandContext commandContext) {
        final PropertyEntity deploymentLockProperty = commandContext.getPropertyManager().findPropertyById("deployment.lock");
        if (deploymentLockProperty == null) {
            BootstrapEngineCommand.LOG.noDeploymentLockPropertyFound();
        }
    }
    
    public void checkHistoryCleanupLockExists(final CommandContext commandContext) {
        final PropertyEntity historyCleanupLockProperty = commandContext.getPropertyManager().findPropertyById("history.cleanup.job.lock");
        if (historyCleanupLockProperty == null) {
            BootstrapEngineCommand.LOG.noHistoryCleanupLockPropertyFound();
        }
    }
    
    protected boolean isHistoryCleanupEnabled(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().isHistoryCleanupEnabled();
    }
    
    public void initializeTelemetryProperty(final CommandContext commandContext) {
        try {
            this.checkTelemetryLockExists(commandContext);
            this.acquireExclusiveTelemetryLock(commandContext);
            final PropertyEntity databaseTelemetryProperty = this.databaseTelemetryConfiguration(commandContext);
            final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
            if (databaseTelemetryProperty == null) {
                BootstrapEngineCommand.LOG.noTelemetryPropertyFound();
                this.createTelemetryProperty(commandContext);
            }
            if ((databaseTelemetryProperty == null && processEngineConfiguration.isInitializeTelemetry()) || Boolean.valueOf(databaseTelemetryProperty.getValue())) {
                TelemetryUtil.toggleLocalTelemetry(true, processEngineConfiguration.getTelemetryRegistry(), processEngineConfiguration.getMetricsRegistry());
            }
        }
        catch (Exception e) {
            BootstrapEngineCommand.LOG.errorConfiguringTelemetryProperty(e);
        }
    }
    
    protected void checkTelemetryLockExists(final CommandContext commandContext) {
        final PropertyEntity telemetryLockProperty = commandContext.getPropertyManager().findPropertyById("telemetry.lock");
        if (telemetryLockProperty == null) {
            BootstrapEngineCommand.LOG.noTelemetryLockPropertyFound();
        }
    }
    
    protected PropertyEntity databaseTelemetryConfiguration(final CommandContext commandContext) {
        try {
            return commandContext.getPropertyManager().findPropertyById("camunda.telemetry.enabled");
        }
        catch (Exception e) {
            BootstrapEngineCommand.LOG.errorFetchingTelemetryPropertyInDatabase(e);
            return null;
        }
    }
    
    protected void createTelemetryProperty(final CommandContext commandContext) {
        final Boolean telemetryEnabled = commandContext.getProcessEngineConfiguration().isInitializeTelemetry();
        PropertyEntity property = null;
        if (telemetryEnabled != null) {
            property = new PropertyEntity("camunda.telemetry.enabled", Boolean.toString(telemetryEnabled));
        }
        else {
            property = new PropertyEntity("camunda.telemetry.enabled", "null");
        }
        commandContext.getPropertyManager().insert(property);
        BootstrapEngineCommand.LOG.creatingTelemetryPropertyInDatabase(telemetryEnabled);
    }
    
    public void initializeInstallationId(final CommandContext commandContext) {
        this.checkInstallationIdLockExists(commandContext);
        String databaseInstallationId = this.databaseInstallationId(commandContext);
        if (databaseInstallationId == null || databaseInstallationId.isEmpty()) {
            this.acquireExclusiveInstallationIdLock(commandContext);
            databaseInstallationId = this.databaseInstallationId(commandContext);
            if (databaseInstallationId == null || databaseInstallationId.isEmpty()) {
                BootstrapEngineCommand.LOG.noInstallationIdPropertyFound();
                this.createInstallationProperty(commandContext);
            }
        }
        else {
            BootstrapEngineCommand.LOG.installationIdPropertyFound(databaseInstallationId);
            commandContext.getProcessEngineConfiguration().setInstallationId(databaseInstallationId);
        }
    }
    
    protected void createInstallationProperty(final CommandContext commandContext) {
        final String installationId = UUID.randomUUID().toString();
        final PropertyEntity property = new PropertyEntity("camunda.installation.id", installationId);
        commandContext.getPropertyManager().insert(property);
        BootstrapEngineCommand.LOG.creatingInstallationPropertyInDatabase(property.getValue());
        commandContext.getProcessEngineConfiguration().setInstallationId(installationId);
    }
    
    protected String databaseInstallationId(final CommandContext commandContext) {
        try {
            final PropertyEntity installationIdProperty = commandContext.getPropertyManager().findPropertyById("camunda.installation.id");
            return (installationIdProperty != null) ? installationIdProperty.getValue() : null;
        }
        catch (Exception e) {
            BootstrapEngineCommand.LOG.couldNotSelectInstallationId(e.getMessage());
            return null;
        }
    }
    
    protected void checkInstallationIdLockExists(final CommandContext commandContext) {
        final PropertyEntity installationIdProperty = commandContext.getPropertyManager().findPropertyById("installationId.lock");
        if (installationIdProperty == null) {
            BootstrapEngineCommand.LOG.noInstallationIdLockPropertyFound();
        }
    }
    
    protected void updateTelemetryData(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        final String installationId = processEngineConfiguration.getInstallationId();
        final TelemetryDataImpl telemetryData = processEngineConfiguration.getTelemetryData();
        telemetryData.setInstallation(installationId);
        final ManagementServiceImpl managementService = (ManagementServiceImpl)processEngineConfiguration.getManagementService();
        final String licenseKey = managementService.getLicenseKey();
        if (licenseKey != null) {
            final LicenseKeyDataImpl licenseKeyData = LicenseKeyDataImpl.fromRawString(licenseKey);
            managementService.setLicenseKeyForTelemetry(licenseKeyData);
            telemetryData.getProduct().getInternals().setLicenseKey(licenseKeyData);
        }
    }
    
    protected void startTelemetryReporter(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        final TelemetryReporter telemetryReporter = processEngineConfiguration.getTelemetryReporter();
        final boolean telemetryReporterActivate = processEngineConfiguration.isTelemetryReporterActivate();
        if (telemetryReporter != null && telemetryReporterActivate) {
            try {
                telemetryReporter.start();
            }
            catch (Exception e) {
                ProcessEngineLogger.TELEMETRY_LOGGER.schedulingTaskFailsOnEngineStart(e);
            }
        }
    }
    
    protected void acquireExclusiveTelemetryLock(final CommandContext commandContext) {
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        propertyManager.acquireExclusiveLockForTelemetry();
    }
    
    protected void acquireExclusiveInstallationIdLock(final CommandContext commandContext) {
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        propertyManager.acquireExclusiveLockForInstallationId();
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
