// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.cache;

import org.zik.bpm.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.zik.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricFormPropertyEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDetailEventEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.persistence.entity.AcquirableJobEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.HashMap;
import java.util.Map;

public class DbEntityCacheKeyMapping
{
    protected Map<Class<?>, Class<?>> entityCacheKeys;
    
    public DbEntityCacheKeyMapping() {
        this.entityCacheKeys = new HashMap<Class<?>, Class<?>>();
    }
    
    public Class<?> getEntityCacheKey(final Class<?> entityType) {
        final Class<?> entityCacheKey = this.entityCacheKeys.get(entityType);
        if (entityCacheKey == null) {
            return entityType;
        }
        return entityCacheKey;
    }
    
    public void registerEntityCacheKey(final Class<?> entityType, final Class<?> cacheKey) {
        this.entityCacheKeys.put(entityType, cacheKey);
    }
    
    public static DbEntityCacheKeyMapping defaultEntityCacheKeyMapping() {
        final DbEntityCacheKeyMapping mapping = new DbEntityCacheKeyMapping();
        mapping.registerEntityCacheKey(JobEntity.class, AcquirableJobEntity.class);
        mapping.registerEntityCacheKey(MessageEntity.class, AcquirableJobEntity.class);
        mapping.registerEntityCacheKey(TimerEntity.class, AcquirableJobEntity.class);
        mapping.registerEntityCacheKey(HistoricFormPropertyEntity.class, HistoricDetailEventEntity.class);
        mapping.registerEntityCacheKey(HistoricFormPropertyEventEntity.class, HistoricDetailEventEntity.class);
        mapping.registerEntityCacheKey(HistoricVariableUpdateEventEntity.class, HistoricDetailEventEntity.class);
        mapping.registerEntityCacheKey(HistoricDetailVariableInstanceUpdateEntity.class, HistoricDetailEventEntity.class);
        return mapping;
    }
    
    public static DbEntityCacheKeyMapping emptyMapping() {
        return new DbEntityCacheKeyMapping();
    }
}
