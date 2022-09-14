// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteIdentityLinkForProcessDefinitionCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected String userId;
    protected String groupId;
    
    public DeleteIdentityLinkForProcessDefinitionCmd(final String processDefinitionId, final String userId, final String groupId) {
        this.validateParams(userId, groupId, processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        this.userId = userId;
        this.groupId = groupId;
    }
    
    protected void validateParams(final String userId, final String groupId, final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        if (userId == null && groupId == null) {
            throw new ProcessEngineException("userId and groupId cannot both be null");
        }
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(this.processDefinitionId);
        EnsureUtil.ensureNotNull("Cannot find process definition with id " + this.processDefinitionId, "processDefinition", processDefinition);
        processDefinition.deleteIdentityLink(this.userId, this.groupId);
        return null;
    }
}
