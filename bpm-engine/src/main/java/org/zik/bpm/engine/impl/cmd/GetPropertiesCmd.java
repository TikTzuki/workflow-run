// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetPropertiesCmd implements Command<Map<String, String>>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public Map<String, String> execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadProperties);
        final List<PropertyEntity> propertyEntities = (List<PropertyEntity>)commandContext.getDbEntityManager().selectList("selectProperties");
        final Map<String, String> properties = new HashMap<String, String>();
        for (final PropertyEntity propertyEntity : propertyEntities) {
            properties.put(propertyEntity.getName(), propertyEntity.getValue());
        }
        return properties;
    }
}
