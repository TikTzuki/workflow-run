// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingVariableInstance;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;

public class VariableConflictActivityInstanceValidator implements MigratingActivityInstanceValidator
{
    @Override
    public void validate(final MigratingActivityInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl instanceReport) {
        final ScopeImpl sourceScope = migratingInstance.getSourceScope();
        final ScopeImpl targetScope = migratingInstance.getTargetScope();
        if (migratingInstance.migrates()) {
            final boolean becomesNonScope = sourceScope.isScope() && !targetScope.isScope();
            if (becomesNonScope) {
                final Map<String, List<MigratingVariableInstance>> dependentVariablesByName = this.getMigratingVariableInstancesByName(migratingInstance);
                for (final String variableName : dependentVariablesByName.keySet()) {
                    if (dependentVariablesByName.get(variableName).size() > 1) {
                        instanceReport.addFailure("The variable '" + variableName + "' exists in both, this scope and concurrent local in the parent scope. Migrating to a non-scope activity would overwrite one of them.");
                    }
                }
            }
        }
    }
    
    protected Map<String, List<MigratingVariableInstance>> getMigratingVariableInstancesByName(final MigratingActivityInstance activityInstance) {
        final Map<String, List<MigratingVariableInstance>> result = new HashMap<String, List<MigratingVariableInstance>>();
        for (final MigratingInstance migratingInstance : activityInstance.getMigratingDependentInstances()) {
            if (migratingInstance instanceof MigratingVariableInstance) {
                final MigratingVariableInstance migratingVariableInstance = (MigratingVariableInstance)migratingInstance;
                CollectionUtil.addToMapOfLists(result, migratingVariableInstance.getVariableName(), migratingVariableInstance);
            }
        }
        return result;
    }
}
