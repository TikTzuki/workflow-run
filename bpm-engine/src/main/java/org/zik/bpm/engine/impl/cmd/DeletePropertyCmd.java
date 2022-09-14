// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeletePropertyCmd implements Command<Object>
{
    protected String name;
    
    public DeletePropertyCmd(final String name) {
        this.name = name;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkDeleteProperty);
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        final PropertyEntity propertyEntity = propertyManager.findPropertyById(this.name);
        if (propertyEntity != null) {
            propertyManager.delete(propertyEntity);
            commandContext.getOperationLogManager().logPropertyOperation("Delete", Collections.singletonList(new PropertyChange("name", null, this.name)));
        }
        return null;
    }
}
