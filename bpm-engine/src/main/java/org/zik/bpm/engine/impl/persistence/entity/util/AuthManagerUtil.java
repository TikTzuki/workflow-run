// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity.util;

import org.zik.bpm.engine.authorization.HistoricTaskPermissions;
import org.zik.bpm.engine.authorization.ProcessDefinitionPermissions;
import org.zik.bpm.engine.authorization.Permission;

public class AuthManagerUtil
{
    public static VariablePermissions getVariablePermissions(final boolean ensureSpecificVariablePermission) {
        return new VariablePermissions(ensureSpecificVariablePermission);
    }
    
    public static class VariablePermissions
    {
        protected Permission processDefinitionPermission;
        protected Permission historicTaskPermission;
        
        public VariablePermissions(final boolean ensureSpecificVariablePermission) {
            if (ensureSpecificVariablePermission) {
                this.processDefinitionPermission = ProcessDefinitionPermissions.READ_HISTORY_VARIABLE;
                this.historicTaskPermission = HistoricTaskPermissions.READ_VARIABLE;
            }
            else {
                this.processDefinitionPermission = ProcessDefinitionPermissions.READ_HISTORY;
                this.historicTaskPermission = HistoricTaskPermissions.READ;
            }
        }
        
        public Permission getProcessDefinitionPermission() {
            return this.processDefinitionPermission;
        }
        
        public Permission getHistoricTaskPermission() {
            return this.historicTaskPermission;
        }
    }
}
