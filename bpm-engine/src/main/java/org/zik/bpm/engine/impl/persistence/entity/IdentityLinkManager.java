// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class IdentityLinkManager extends AbstractManager
{
    public List<IdentityLinkEntity> findIdentityLinksByTaskId(final String taskId) {
        return (List<IdentityLinkEntity>)this.getDbEntityManager().selectList("selectIdentityLinksByTask", taskId);
    }
    
    public List<IdentityLinkEntity> findIdentityLinksByProcessDefinitionId(final String processDefinitionId) {
        return (List<IdentityLinkEntity>)this.getDbEntityManager().selectList("selectIdentityLinksByProcessDefinition", processDefinitionId);
    }
    
    public List<IdentityLinkEntity> findIdentityLinkByTaskUserGroupAndType(final String taskId, final String userId, final String groupId, final String type) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("taskId", taskId);
        parameters.put("userId", userId);
        parameters.put("groupId", groupId);
        parameters.put("type", type);
        return (List<IdentityLinkEntity>)this.getDbEntityManager().selectList("selectIdentityLinkByTaskUserGroupAndType", parameters);
    }
    
    public List<IdentityLinkEntity> findIdentityLinkByProcessDefinitionUserAndGroup(final String processDefinitionId, final String userId, final String groupId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("userId", userId);
        parameters.put("groupId", groupId);
        return (List<IdentityLinkEntity>)this.getDbEntityManager().selectList("selectIdentityLinkByProcessDefinitionUserAndGroup", parameters);
    }
    
    public void deleteIdentityLinksByProcDef(final String processDefId) {
        this.getDbEntityManager().delete(IdentityLinkEntity.class, "deleteIdentityLinkByProcDef", processDefId);
    }
}
