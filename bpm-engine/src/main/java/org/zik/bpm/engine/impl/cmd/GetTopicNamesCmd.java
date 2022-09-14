// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ExternalTaskQueryImpl;
import java.io.Serializable;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTopicNamesCmd implements Command<List<String>>, Serializable
{
    protected ExternalTaskQueryImpl externalTaskQuery;
    
    public GetTopicNamesCmd(final boolean withLockedTasks, final boolean withUnlockedTasks, final boolean withRetriesLeft) {
        this.externalTaskQuery = new ExternalTaskQueryImpl();
        if (withLockedTasks) {
            this.externalTaskQuery.locked();
        }
        if (withUnlockedTasks) {
            this.externalTaskQuery.notLocked();
        }
        if (withRetriesLeft) {
            this.externalTaskQuery.withRetriesLeft();
        }
    }
    
    @Override
    public List<String> execute(final CommandContext commandContext) {
        return commandContext.getExternalTaskManager().selectTopicNamesByQuery(this.externalTaskQuery);
    }
}
