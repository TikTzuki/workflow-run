// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.management.TableMetaData;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTableMetaDataCmd implements Command<TableMetaData>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String tableName;
    
    public GetTableMetaDataCmd(final String tableName) {
        this.tableName = tableName;
    }
    
    @Override
    public TableMetaData execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("tableName", (Object)this.tableName);
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadTableMetaData);
        return commandContext.getTableDataManager().getTableMetaData(this.tableName);
    }
}
