// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTableNameCmd implements Command<String>, Serializable
{
    private static final long serialVersionUID = 1L;
    private Class<?> entityClass;
    
    public GetTableNameCmd(final Class<?> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("entityClass", this.entityClass);
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadTableName);
        return commandContext.getTableDataManager().getTableName(this.entityClass, true);
    }
}
