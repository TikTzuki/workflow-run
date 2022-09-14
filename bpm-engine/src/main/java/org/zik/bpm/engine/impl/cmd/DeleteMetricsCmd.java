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

public class DeleteMetricsCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Date timestamp;
    protected String reporter;
    
    public DeleteMetricsCmd(final Date timestamp, final String reporter) {
        this.timestamp = timestamp;
        this.reporter = reporter;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkDeleteMetrics);
        this.writeUserOperationLog(commandContext);
        if (this.timestamp == null && this.reporter == null) {
            commandContext.getMeterLogManager().deleteAll();
        }
        else {
            commandContext.getMeterLogManager().deleteByTimestampAndReporter(this.timestamp, this.reporter);
        }
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        if (this.timestamp != null) {
            propertyChanges.add(new PropertyChange("timestamp", null, this.timestamp));
        }
        if (this.reporter != null) {
            propertyChanges.add(new PropertyChange("reporter", null, this.reporter));
        }
        if (propertyChanges.isEmpty()) {
            propertyChanges.add(PropertyChange.EMPTY_CHANGE);
        }
        commandContext.getOperationLogManager().logMetricsOperation("Delete", propertyChanges);
    }
}
