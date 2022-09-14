// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetPropertyCmd implements Command<Object>
{
    protected String name;
    protected String value;
    
    public SetPropertyCmd(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkSetProperty);
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        PropertyEntity property = propertyManager.findPropertyById(this.name);
        String operation = null;
        if (property != null) {
            property.setValue(this.value);
            operation = "Update";
        }
        else {
            property = new PropertyEntity(this.name, this.value);
            propertyManager.insert(property);
            operation = "Create";
        }
        commandContext.getOperationLogManager().logPropertyOperation(operation, Collections.singletonList(new PropertyChange("name", null, this.name)));
        return null;
    }
}
