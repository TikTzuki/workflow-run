// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTableCountCmd implements Command<Map<String, Long>>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public Map<String, Long> execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadTableCount);
        return commandContext.getTableDataManager().getTableCount();
    }
}
