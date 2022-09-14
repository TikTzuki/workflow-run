// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteTaskMetricsCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Date timestamp;
    
    public DeleteTaskMetricsCmd(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkDeleteTaskMetrics);
        this.writeUserOperationLog(commandContext);
        commandContext.getMeterLogManager().deleteTaskMetricsByTimestamp(this.timestamp);
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        if (this.timestamp != null) {
            propertyChanges.add(new PropertyChange("timestamp", null, this.timestamp));
        }
        if (propertyChanges.isEmpty()) {
            propertyChanges.add(PropertyChange.EMPTY_CHANGE);
        }
        commandContext.getOperationLogManager().logTaskMetricsOperation("Delete", propertyChanges);
    }
}
