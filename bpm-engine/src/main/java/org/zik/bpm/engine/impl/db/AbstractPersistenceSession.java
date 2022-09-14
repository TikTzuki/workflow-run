// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Collections;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPersistenceSession implements PersistenceSession
{
    protected static final EnginePersistenceLogger LOG;
    protected List<EntityLoadListener> listeners;
    
    public AbstractPersistenceSession() {
        this.listeners = new ArrayList<EntityLoadListener>(1);
    }
    
    public void executeDbOperation(final DbOperation operation) {
        switch (operation.getOperationType()) {
            case INSERT: {
                this.insertEntity((DbEntityOperation)operation);
                break;
            }
            case DELETE: {
                this.deleteEntity((DbEntityOperation)operation);
                break;
            }
            case DELETE_BULK: {
                this.deleteBulk((DbBulkOperation)operation);
                break;
            }
            case UPDATE: {
                this.updateEntity((DbEntityOperation)operation);
                break;
            }
            case UPDATE_BULK: {
                this.updateBulk((DbBulkOperation)operation);
                break;
            }
        }
    }
    
    protected abstract void insertEntity(final DbEntityOperation p0);
    
    protected abstract void deleteEntity(final DbEntityOperation p0);
    
    protected abstract void deleteBulk(final DbBulkOperation p0);
    
    protected abstract void updateEntity(final DbEntityOperation p0);
    
    protected abstract void updateBulk(final DbBulkOperation p0);
    
    protected abstract String getDbVersion();
    
    @Override
    public void dbSchemaCreate() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final HistoryLevel configuredHistoryLevel = processEngineConfiguration.getHistoryLevel();
        if (!processEngineConfiguration.isDbHistoryUsed() && !configuredHistoryLevel.equals(HistoryLevel.HISTORY_LEVEL_NONE)) {
            throw AbstractPersistenceSession.LOG.databaseHistoryLevelException(configuredHistoryLevel.getName());
        }
        if (this.isEngineTablePresent()) {
            final String dbVersion = this.getDbVersion();
            if (!"fox".equals(dbVersion)) {
                throw AbstractPersistenceSession.LOG.wrongDbVersionException("fox", dbVersion);
            }
        }
        else {
            this.dbSchemaCreateEngine();
        }
        if (processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaCreateHistory();
        }
        if (processEngineConfiguration.isDbIdentityUsed()) {
            this.dbSchemaCreateIdentity();
        }
        if (processEngineConfiguration.isCmmnEnabled()) {
            this.dbSchemaCreateCmmn();
        }
        if (processEngineConfiguration.isCmmnEnabled() && processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaCreateCmmnHistory();
        }
        if (processEngineConfiguration.isDmnEnabled()) {
            this.dbSchemaCreateDmn();
            if (processEngineConfiguration.isDbHistoryUsed()) {
                this.dbSchemaCreateDmnHistory();
            }
        }
    }
    
    protected abstract void dbSchemaCreateIdentity();
    
    protected abstract void dbSchemaCreateHistory();
    
    protected abstract void dbSchemaCreateEngine();
    
    protected abstract void dbSchemaCreateCmmn();
    
    protected abstract void dbSchemaCreateCmmnHistory();
    
    protected abstract void dbSchemaCreateDmn();
    
    protected abstract void dbSchemaCreateDmnHistory();
    
    @Override
    public void dbSchemaDrop() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isDmnEnabled()) {
            this.dbSchemaDropDmn();
            if (processEngineConfiguration.isDbHistoryUsed()) {
                this.dbSchemaDropDmnHistory();
            }
        }
        if (processEngineConfiguration.isCmmnEnabled()) {
            this.dbSchemaDropCmmn();
        }
        this.dbSchemaDropEngine();
        if (processEngineConfiguration.isCmmnEnabled() && processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaDropCmmnHistory();
        }
        if (processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaDropHistory();
        }
        if (processEngineConfiguration.isDbIdentityUsed()) {
            this.dbSchemaDropIdentity();
        }
    }
    
    protected abstract void dbSchemaDropIdentity();
    
    protected abstract void dbSchemaDropHistory();
    
    protected abstract void dbSchemaDropEngine();
    
    protected abstract void dbSchemaDropCmmn();
    
    protected abstract void dbSchemaDropCmmnHistory();
    
    protected abstract void dbSchemaDropDmn();
    
    protected abstract void dbSchemaDropDmnHistory();
    
    @Override
    public void dbSchemaPrune() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (this.isHistoryTablePresent() && !processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaDropHistory();
        }
        if (this.isIdentityTablePresent() && !processEngineConfiguration.isDbIdentityUsed()) {
            this.dbSchemaDropIdentity();
        }
        if (this.isCmmnTablePresent() && !processEngineConfiguration.isCmmnEnabled()) {
            this.dbSchemaDropCmmn();
        }
        if (this.isCmmnHistoryTablePresent() && (!processEngineConfiguration.isCmmnEnabled() || !processEngineConfiguration.isDbHistoryUsed())) {
            this.dbSchemaDropCmmnHistory();
        }
        if (this.isDmnTablePresent() && !processEngineConfiguration.isDmnEnabled()) {
            this.dbSchemaDropDmn();
        }
        if (this.isDmnHistoryTablePresent() && (!processEngineConfiguration.isDmnEnabled() || !processEngineConfiguration.isDbHistoryUsed())) {
            this.dbSchemaDropDmnHistory();
        }
    }
    
    public abstract boolean isEngineTablePresent();
    
    public abstract boolean isHistoryTablePresent();
    
    public abstract boolean isIdentityTablePresent();
    
    public abstract boolean isCmmnTablePresent();
    
    public abstract boolean isCmmnHistoryTablePresent();
    
    public abstract boolean isDmnTablePresent();
    
    public abstract boolean isDmnHistoryTablePresent();
    
    @Override
    public void dbSchemaUpdate() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (!this.isEngineTablePresent()) {
            this.dbSchemaCreateEngine();
        }
        if (!this.isHistoryTablePresent() && processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaCreateHistory();
        }
        if (!this.isIdentityTablePresent() && processEngineConfiguration.isDbIdentityUsed()) {
            this.dbSchemaCreateIdentity();
        }
        if (!this.isCmmnTablePresent() && processEngineConfiguration.isCmmnEnabled()) {
            this.dbSchemaCreateCmmn();
        }
        if (!this.isCmmnHistoryTablePresent() && processEngineConfiguration.isCmmnEnabled() && processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaCreateCmmnHistory();
        }
        if (!this.isDmnTablePresent() && processEngineConfiguration.isDmnEnabled()) {
            this.dbSchemaCreateDmn();
        }
        if (!this.isDmnHistoryTablePresent() && processEngineConfiguration.isDmnEnabled() && processEngineConfiguration.isDbHistoryUsed()) {
            this.dbSchemaCreateDmnHistory();
        }
    }
    
    @Override
    public List<String> getTableNamesPresent() {
        return Collections.emptyList();
    }
    
    @Override
    public void addEntityLoadListener(final EntityLoadListener listener) {
        this.listeners.add(listener);
    }
    
    protected void fireEntityLoaded(final Object result) {
        if (result != null && result instanceof DbEntity) {
            final DbEntity entity = (DbEntity)result;
            for (final EntityLoadListener entityLoadListener : this.listeners) {
                entityLoadListener.onEntityLoaded(entity);
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
