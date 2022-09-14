// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.batch;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class MigrationBatchConfiguration extends BatchConfiguration
{
    protected MigrationPlan migrationPlan;
    protected boolean isSkipCustomListeners;
    protected boolean isSkipIoMappings;
    
    public MigrationBatchConfiguration(final List<String> ids, final MigrationPlan migrationPlan, final boolean isSkipCustomListeners, final boolean isSkipIoMappings, final String batchId) {
        this(ids, null, migrationPlan, isSkipCustomListeners, isSkipIoMappings, batchId);
    }
    
    public MigrationBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final MigrationPlan migrationPlan, final boolean isSkipCustomListeners, final boolean isSkipIoMappings, final String batchId) {
        super(ids, mappings);
        this.migrationPlan = migrationPlan;
        this.isSkipCustomListeners = isSkipCustomListeners;
        this.isSkipIoMappings = isSkipIoMappings;
        this.batchId = batchId;
    }
    
    public MigrationBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final MigrationPlan migrationPlan, final boolean isSkipCustomListeners, final boolean isSkipIoMappings) {
        this(ids, mappings, migrationPlan, isSkipCustomListeners, isSkipIoMappings, null);
    }
    
    public MigrationPlan getMigrationPlan() {
        return this.migrationPlan;
    }
    
    public void setMigrationPlan(final MigrationPlan migrationPlan) {
        this.migrationPlan = migrationPlan;
    }
    
    public boolean isSkipCustomListeners() {
        return this.isSkipCustomListeners;
    }
    
    public void setSkipCustomListeners(final boolean isSkipCustomListeners) {
        this.isSkipCustomListeners = isSkipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.isSkipIoMappings;
    }
    
    public void setSkipIoMappings(final boolean isSkipIoMappings) {
        this.isSkipIoMappings = isSkipIoMappings;
    }
}
