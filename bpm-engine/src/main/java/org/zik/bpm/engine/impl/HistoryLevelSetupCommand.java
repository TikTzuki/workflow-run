// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.cmd.DetermineHistoryLevelCmd;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public final class HistoryLevelSetupCommand implements Command<Void>
{
    private static final EnginePersistenceLogger LOG;
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        this.checkStartupLockExists(commandContext);
        HistoryLevel databaseHistoryLevel = new DetermineHistoryLevelCmd(processEngineConfiguration.getHistoryLevels()).execute(commandContext);
        this.determineAutoHistoryLevel(processEngineConfiguration, databaseHistoryLevel);
        final HistoryLevel configuredHistoryLevel = processEngineConfiguration.getHistoryLevel();
        if (databaseHistoryLevel == null) {
            this.acquireExclusiveLock(commandContext);
            databaseHistoryLevel = new DetermineHistoryLevelCmd(processEngineConfiguration.getHistoryLevels()).execute(commandContext);
            if (databaseHistoryLevel == null) {
                HistoryLevelSetupCommand.LOG.noHistoryLevelPropertyFound();
                dbCreateHistoryLevel(commandContext);
            }
        }
        else if (configuredHistoryLevel.getId() != databaseHistoryLevel.getId()) {
            throw new ProcessEngineException("historyLevel mismatch: configuration says " + configuredHistoryLevel + " and database says " + databaseHistoryLevel);
        }
        return null;
    }
    
    public static void dbCreateHistoryLevel(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final HistoryLevel configuredHistoryLevel = processEngineConfiguration.getHistoryLevel();
        final PropertyEntity property = new PropertyEntity("historyLevel", Integer.toString(configuredHistoryLevel.getId()));
        commandContext.getSession(DbEntityManager.class).insert(property);
        HistoryLevelSetupCommand.LOG.creatingHistoryLevelPropertyInDatabase(configuredHistoryLevel);
    }
    
    public static Integer databaseHistoryLevel(final CommandContext commandContext) {
        try {
            final PropertyEntity historyLevelProperty = commandContext.getPropertyManager().findPropertyById("historyLevel");
            return (historyLevelProperty != null) ? new Integer(historyLevelProperty.getValue()) : null;
        }
        catch (Exception e) {
            HistoryLevelSetupCommand.LOG.couldNotSelectHistoryLevel(e.getMessage());
            return null;
        }
    }
    
    protected void determineAutoHistoryLevel(final ProcessEngineConfigurationImpl engineConfiguration, final HistoryLevel databaseHistoryLevel) {
        final HistoryLevel configuredHistoryLevel = engineConfiguration.getHistoryLevel();
        if (configuredHistoryLevel == null && "auto".equals(engineConfiguration.getHistory())) {
            if (databaseHistoryLevel != null) {
                engineConfiguration.setHistoryLevel(databaseHistoryLevel);
            }
            else {
                engineConfiguration.setHistoryLevel(engineConfiguration.getDefaultHistoryLevel());
            }
        }
    }
    
    protected void checkStartupLockExists(final CommandContext commandContext) {
        final PropertyEntity historyStartupProperty = commandContext.getPropertyManager().findPropertyById("startup.lock");
        if (historyStartupProperty == null) {
            HistoryLevelSetupCommand.LOG.noStartupLockPropertyFound();
        }
    }
    
    protected void acquireExclusiveLock(final CommandContext commandContext) {
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        propertyManager.acquireExclusiveLockForStartup();
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
