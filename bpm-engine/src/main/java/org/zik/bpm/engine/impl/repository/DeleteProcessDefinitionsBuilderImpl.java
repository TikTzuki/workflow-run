// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.DeleteProcessDefinitionsByIdsCmd;
import org.zik.bpm.engine.impl.cmd.DeleteProcessDefinitionsByKeyCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsTenantBuilder;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsSelectBuilder;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsBuilder;

public class DeleteProcessDefinitionsBuilderImpl implements DeleteProcessDefinitionsBuilder, DeleteProcessDefinitionsSelectBuilder, DeleteProcessDefinitionsTenantBuilder
{
    private final CommandExecutor commandExecutor;
    private String processDefinitionKey;
    private List<String> processDefinitionIds;
    private boolean cascade;
    private String tenantId;
    private boolean isTenantIdSet;
    private boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    public DeleteProcessDefinitionsBuilderImpl(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl byIds(final String... processDefinitionId) {
        if (processDefinitionId != null) {
            (this.processDefinitionIds = new ArrayList<String>()).addAll(Arrays.asList(processDefinitionId));
        }
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl byKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl withoutTenantId() {
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl withTenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.isTenantIdSet = true;
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl cascade() {
        this.cascade = true;
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl skipCustomListeners() {
        this.skipCustomListeners = true;
        return this;
    }
    
    @Override
    public DeleteProcessDefinitionsBuilderImpl skipIoMappings() {
        this.skipIoMappings = true;
        return this;
    }
    
    @Override
    public void delete() {
        EnsureUtil.ensureOnlyOneNotNull(NullValueException.class, "'processDefinitionKey' or 'processDefinitionIds' cannot be null", this.processDefinitionKey, this.processDefinitionIds);
        Command<Void> command;
        if (this.processDefinitionKey != null) {
            command = new DeleteProcessDefinitionsByKeyCmd(this.processDefinitionKey, this.cascade, this.skipCustomListeners, this.skipIoMappings, this.tenantId, this.isTenantIdSet);
        }
        else {
            if (this.processDefinitionIds == null || this.processDefinitionIds.isEmpty()) {
                return;
            }
            command = new DeleteProcessDefinitionsByIdsCmd(this.processDefinitionIds, this.cascade, this.skipCustomListeners, this.skipIoMappings);
        }
        this.commandExecutor.execute(command);
    }
}
