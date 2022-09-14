// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.migration.batch.MigrateProcessInstanceBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import java.util.Arrays;
import java.util.Collections;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.migration.MigrationPlanExecutionBuilder;

public class MigrationPlanExecutionBuilderImpl implements MigrationPlanExecutionBuilder
{
    protected CommandExecutor commandExecutor;
    protected MigrationPlan migrationPlan;
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    public MigrationPlanExecutionBuilderImpl(final CommandExecutor commandExecutor, final MigrationPlan migrationPlan) {
        this.commandExecutor = commandExecutor;
        this.migrationPlan = migrationPlan;
    }
    
    public MigrationPlan getMigrationPlan() {
        return this.migrationPlan;
    }
    
    @Override
    public MigrationPlanExecutionBuilder processInstanceIds(final List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public MigrationPlanExecutionBuilder processInstanceIds(final String... processInstanceIds) {
        if (processInstanceIds == null) {
            this.processInstanceIds = Collections.emptyList();
        }
        else {
            this.processInstanceIds = Arrays.asList(processInstanceIds);
        }
        return this;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    @Override
    public MigrationPlanExecutionBuilder processInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        this.processInstanceQuery = processInstanceQuery;
        return this;
    }
    
    public ProcessInstanceQuery getProcessInstanceQuery() {
        return this.processInstanceQuery;
    }
    
    @Override
    public MigrationPlanExecutionBuilder skipCustomListeners() {
        this.skipCustomListeners = true;
        return this;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    @Override
    public MigrationPlanExecutionBuilder skipIoMappings() {
        this.skipIoMappings = true;
        return this;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
    
    @Override
    public void execute() {
        this.commandExecutor.execute((Command<Object>)new MigrateProcessInstanceCmd(this, false));
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new MigrateProcessInstanceBatchCmd(this));
    }
}
