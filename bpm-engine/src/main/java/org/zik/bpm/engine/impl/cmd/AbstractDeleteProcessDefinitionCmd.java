// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractDeleteProcessDefinitionCmd implements Command<Void>, Serializable
{
    protected boolean cascade;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    protected void deleteProcessDefinitionCmd(final CommandContext commandContext, final String processDefinitionId, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        final ProcessDefinition processDefinition = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No process definition found with id '" + processDefinitionId + "'", "processDefinition", processDefinition);
        final List<CommandChecker> commandCheckers = commandContext.getProcessEngineConfiguration().getCommandCheckers();
        for (final CommandChecker checker : commandCheckers) {
            checker.checkDeleteProcessDefinitionById(processDefinitionId);
        }
        final UserOperationLogManager userOperationLogManager = commandContext.getOperationLogManager();
        userOperationLogManager.logProcessDefinitionOperation("Delete", processDefinitionId, processDefinition.getKey(), new PropertyChange("cascade", false, cascade));
        final ProcessDefinitionManager definitionManager = commandContext.getProcessDefinitionManager();
        definitionManager.deleteProcessDefinition(processDefinition, processDefinitionId, cascade, cascade, skipCustomListeners, skipIoMappings);
    }
}
