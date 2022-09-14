// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.IdentityLink;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetIdentityLinksForProcessDefinitionCmd implements Command<List<IdentityLink>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    
    public GetIdentityLinksForProcessDefinitionCmd(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public List<IdentityLink> execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(this.processDefinitionId);
        EnsureUtil.ensureNotNull("Cannot find process definition with id " + this.processDefinitionId, "processDefinition", processDefinition);
        final List<IdentityLink> identityLinks = (List<IdentityLink>)processDefinition.getIdentityLinks();
        return identityLinks;
    }
}
