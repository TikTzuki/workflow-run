// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation.comparator;

import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.UserEntity;
import org.zik.bpm.engine.impl.persistence.entity.GroupEntity;
import org.zik.bpm.engine.impl.persistence.entity.TenantEntity;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartEntity;
import org.zik.bpm.engine.impl.persistence.entity.TenantMembershipEntity;
import org.zik.bpm.engine.impl.persistence.entity.MembershipEntity;
import org.zik.bpm.engine.impl.persistence.entity.EverLivingJobEntity;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.persistence.entity.IdentityLinkEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

public class EntityTypeComparatorForModifications implements Comparator<Class<?>>
{
    public static final Map<Class<?>, Integer> TYPE_ORDER;
    
    @Override
    public int compare(final Class<?> firstEntityType, final Class<?> secondEntityType) {
        if (firstEntityType == secondEntityType) {
            return 0;
        }
        Integer firstIndex = EntityTypeComparatorForModifications.TYPE_ORDER.get(firstEntityType);
        Integer secondIndex = EntityTypeComparatorForModifications.TYPE_ORDER.get(secondEntityType);
        if (firstIndex == null) {
            firstIndex = Integer.MAX_VALUE;
        }
        if (secondIndex == null) {
            secondIndex = Integer.MAX_VALUE;
        }
        final int result = firstIndex.compareTo(secondIndex);
        if (result == 0) {
            return firstEntityType.getName().compareTo(secondEntityType.getName());
        }
        return result;
    }
    
    static {
        (TYPE_ORDER = new HashMap<Class<?>, Integer>()).put(IncidentEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(VariableInstanceEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(IdentityLinkEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(EventSubscriptionEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(JobEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(MessageEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(TimerEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(EverLivingJobEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(MembershipEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(TenantMembershipEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(CaseSentryPartEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(ExternalTaskEntity.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(Batch.class, 1);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(TenantEntity.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(GroupEntity.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(UserEntity.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(ByteArrayEntity.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(TaskEntity.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(JobDefinition.class, 2);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(ExecutionEntity.class, 3);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(CaseExecutionEntity.class, 3);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(ProcessDefinitionEntity.class, 4);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(CaseDefinitionEntity.class, 4);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(DecisionDefinitionEntity.class, 4);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(DecisionRequirementsDefinitionEntity.class, 4);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(ResourceEntity.class, 4);
        EntityTypeComparatorForModifications.TYPE_ORDER.put(DeploymentEntity.class, 5);
    }
}
