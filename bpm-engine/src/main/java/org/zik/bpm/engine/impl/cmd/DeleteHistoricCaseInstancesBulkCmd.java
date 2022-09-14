// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.HistoricCaseInstanceQueryImpl;
import java.util.concurrent.Callable;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricCaseInstancesBulkCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final List<String> caseInstanceIds;
    
    public DeleteHistoricCaseInstancesBulkCmd(final List<String> caseInstanceIds) {
        this.caseInstanceIds = caseInstanceIds;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "caseInstanceIds", this.caseInstanceIds);
        commandContext.runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                EnsureUtil.ensureEquals(BadUserRequestException.class, "ClosedCaseInstanceIds", new HistoricCaseInstanceQueryImpl().closed().caseInstanceIds(new HashSet<String>(DeleteHistoricCaseInstancesBulkCmd.this.caseInstanceIds)).count(), DeleteHistoricCaseInstancesBulkCmd.this.caseInstanceIds.size());
                return null;
            }
        });
        commandContext.getOperationLogManager().logCaseInstanceOperation("DeleteHistory", null, Collections.singletonList(new PropertyChange("nrOfInstances", null, this.caseInstanceIds.size())));
        commandContext.getHistoricCaseInstanceManager().deleteHistoricCaseInstancesByIds(this.caseInstanceIds);
        return null;
    }
}
