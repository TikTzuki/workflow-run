// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class DeleteProcessDefinitionsByKeyCmd extends AbstractDeleteProcessDefinitionCmd
{
    private static final long serialVersionUID = 1L;
    private final String processDefinitionKey;
    private final String tenantId;
    private final boolean isTenantIdSet;
    
    public DeleteProcessDefinitionsByKeyCmd(final String processDefinitionKey, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings, final String tenantId, final boolean isTenantIdSet) {
        this.processDefinitionKey = processDefinitionKey;
        this.cascade = cascade;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
        this.tenantId = tenantId;
        this.isTenantIdSet = isTenantIdSet;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)this.processDefinitionKey);
        final List<ProcessDefinition> processDefinitions = commandContext.getProcessDefinitionManager().findDefinitionsByKeyAndTenantId(this.processDefinitionKey, this.tenantId, this.isTenantIdSet);
        EnsureUtil.ensureNotEmpty(NotFoundException.class, "No process definition found with key '" + this.processDefinitionKey + "'", "processDefinitions", processDefinitions);
        for (final ProcessDefinition processDefinition : processDefinitions) {
            final String processDefinitionId = processDefinition.getId();
            this.deleteProcessDefinitionCmd(commandContext, processDefinitionId, this.cascade, this.skipCustomListeners, this.skipIoMappings);
        }
        return null;
    }
}
