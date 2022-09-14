// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.cache;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;

public class DbEntityCache
{
    protected static final EnginePersistenceLogger LOG;
    protected Map<Class<?>, Map<String, CachedDbEntity>> cachedEntites;
    protected DbEntityCacheKeyMapping cacheKeyMapping;
    
    public DbEntityCache() {
        this.cachedEntites = new HashMap<Class<?>, Map<String, CachedDbEntity>>();
        this.cacheKeyMapping = DbEntityCacheKeyMapping.emptyMapping();
    }
    
    public DbEntityCache(final DbEntityCacheKeyMapping cacheKeyMapping) {
        this.cachedEntites = new HashMap<Class<?>, Map<String, CachedDbEntity>>();
        this.cacheKeyMapping = cacheKeyMapping;
    }
    
    public <T extends DbEntity> T get(final Class<T> type, final String id) {
        final Class<?> cacheKey = this.cacheKeyMapping.getEntityCacheKey(type);
        final CachedDbEntity cachedDbEntity = this.getCachedEntity(cacheKey, id);
        if (cachedDbEntity != null) {
            final DbEntity dbEntity = cachedDbEntity.getEntity();
            if (!type.isAssignableFrom(dbEntity.getClass())) {
                throw DbEntityCache.LOG.entityCacheLookupException(type, id, dbEntity.getClass(), null);
            }
            try {
                return (T)dbEntity;
            }
            catch (ClassCastException e) {
                throw DbEntityCache.LOG.entityCacheLookupException(type, id, dbEntity.getClass(), e);
            }
        }
        return null;
    }
    
    public <T extends DbEntity> List<T> getEntitiesByType(final Class<T> type) {
        final Class<?> cacheKey = this.cacheKeyMapping.getEntityCacheKey(type);
        final Map<String, CachedDbEntity> entities = this.cachedEntites.get(cacheKey);
        final List<T> result = new ArrayList<T>();
        if (entities == null) {
            return Collections.emptyList();
        }
        for (final CachedDbEntity cachedEntity : entities.values()) {
            if (type != cacheKey) {
                if (!type.isAssignableFrom(cachedEntity.getClass())) {
                    continue;
                }
                result.add((T)cachedEntity.getEntity());
            }
            else {
                result.add((T)cachedEntity.getEntity());
            }
        }
        return result;
    }
    
    public CachedDbEntity getCachedEntity(final Class<?> type, final String id) {
        final Class<?> cacheKey = this.cacheKeyMapping.getEntityCacheKey(type);
        final Map<String, CachedDbEntity> entitiesByType = this.cachedEntites.get(cacheKey);
        if (entitiesByType != null) {
            return entitiesByType.get(id);
        }
        return null;
    }
    
    public CachedDbEntity getCachedEntity(final DbEntity dbEntity) {
        return this.getCachedEntity(dbEntity.getClass(), dbEntity.getId());
    }
    
    public void putTransient(final DbEntity e) {
        final CachedDbEntity cachedDbEntity = new CachedDbEntity();
        cachedDbEntity.setEntity(e);
        cachedDbEntity.setEntityState(DbEntityState.TRANSIENT);
        this.putInternal(cachedDbEntity);
    }
    
    public void putPersistent(final DbEntity e) {
        final CachedDbEntity cachedDbEntity = new CachedDbEntity();
        cachedDbEntity.setEntity(e);
        cachedDbEntity.setEntityState(DbEntityState.PERSISTENT);
        cachedDbEntity.determineEntityReferences();
        cachedDbEntity.makeCopy();
        this.putInternal(cachedDbEntity);
    }
    
    public void putMerged(final DbEntity e) {
        final CachedDbEntity cachedDbEntity = new CachedDbEntity();
        cachedDbEntity.setEntity(e);
        cachedDbEntity.setEntityState(DbEntityState.MERGED);
        cachedDbEntity.determineEntityReferences();
        this.putInternal(cachedDbEntity);
    }
    
    protected void putInternal(final CachedDbEntity entityToAdd) {
        final Class<? extends DbEntity> type = entityToAdd.getEntity().getClass();
        final Class<?> cacheKey = this.cacheKeyMapping.getEntityCacheKey(type);
        Map<String, CachedDbEntity> map = this.cachedEntites.get(cacheKey);
        if (map == null) {
            map = new HashMap<String, CachedDbEntity>();
            this.cachedEntites.put(cacheKey, map);
        }
        final CachedDbEntity existingCachedEntity = map.get(entityToAdd.getEntity().getId());
        if (existingCachedEntity == null) {
            map.put(entityToAdd.getEntity().getId(), entityToAdd);
        }
        else {
            switch (entityToAdd.getEntityState()) {
                case TRANSIENT: {
                    if (existingCachedEntity.getEntityState() == DbEntityState.TRANSIENT) {
                        throw DbEntityCache.LOG.entityCacheDuplicateEntryException("TRANSIENT", entityToAdd.getEntity().getId(), entityToAdd.getEntity().getClass(), existingCachedEntity.getEntityState());
                    }
                    throw DbEntityCache.LOG.alreadyMarkedEntityInEntityCacheException(entityToAdd.getEntity().getId(), entityToAdd.getEntity().getClass(), existingCachedEntity.getEntityState());
                }
                case PERSISTENT: {
                    if (existingCachedEntity.getEntityState() == DbEntityState.PERSISTENT) {
                        map.put(entityToAdd.getEntity().getId(), entityToAdd);
                        break;
                    }
                    if (existingCachedEntity.getEntityState() == DbEntityState.DELETED_PERSISTENT) {
                        break;
                    }
                    if (existingCachedEntity.getEntityState() == DbEntityState.DELETED_MERGED) {
                        break;
                    }
                    throw DbEntityCache.LOG.entityCacheDuplicateEntryException("PERSISTENT", entityToAdd.getEntity().getId(), entityToAdd.getEntity().getClass(), existingCachedEntity.getEntityState());
                }
                case MERGED: {
                    if (existingCachedEntity.getEntityState() == DbEntityState.PERSISTENT || existingCachedEntity.getEntityState() == DbEntityState.MERGED) {
                        map.put(entityToAdd.getEntity().getId(), entityToAdd);
                        break;
                    }
                    if (existingCachedEntity.getEntityState() == DbEntityState.DELETED_PERSISTENT) {
                        break;
                    }
                    if (existingCachedEntity.getEntityState() == DbEntityState.DELETED_MERGED) {
                        break;
                    }
                    throw DbEntityCache.LOG.entityCacheDuplicateEntryException("MERGED", entityToAdd.getEntity().getId(), entityToAdd.getEntity().getClass(), existingCachedEntity.getEntityState());
                }
                default: {
                    map.put(entityToAdd.getEntity().getId(), entityToAdd);
                    break;
                }
            }
        }
    }
    
    public boolean remove(final DbEntity e) {
        final Class<?> cacheKey = this.cacheKeyMapping.getEntityCacheKey(e.getClass());
        final Map<String, CachedDbEntity> typeMap = this.cachedEntites.get(cacheKey);
        return typeMap != null && typeMap.remove(e.getId()) != null;
    }
    
    public void remove(final CachedDbEntity cachedDbEntity) {
        this.remove(cachedDbEntity.getEntity());
    }
    
    public boolean contains(final DbEntity dbEntity) {
        return this.getCachedEntity(dbEntity) != null;
    }
    
    public boolean isPersistent(final DbEntity dbEntity) {
        final CachedDbEntity cachedDbEntity = this.getCachedEntity(dbEntity);
        return cachedDbEntity != null && cachedDbEntity.getEntityState() == DbEntityState.PERSISTENT;
    }
    
    public boolean isDeleted(final DbEntity dbEntity) {
        final CachedDbEntity cachedDbEntity = this.getCachedEntity(dbEntity);
        return cachedDbEntity != null && (cachedDbEntity.getEntityState() == DbEntityState.DELETED_MERGED || cachedDbEntity.getEntityState() == DbEntityState.DELETED_PERSISTENT || cachedDbEntity.getEntityState() == DbEntityState.DELETED_TRANSIENT);
    }
    
    public boolean isTransient(final DbEntity dbEntity) {
        final CachedDbEntity cachedDbEntity = this.getCachedEntity(dbEntity);
        return cachedDbEntity != null && cachedDbEntity.getEntityState() == DbEntityState.TRANSIENT;
    }
    
    public List<CachedDbEntity> getCachedEntities() {
        final List<CachedDbEntity> result = new ArrayList<CachedDbEntity>();
        for (final Map<String, CachedDbEntity> typeCache : this.cachedEntites.values()) {
            result.addAll(typeCache.values());
        }
        return result;
    }
    
    public void setDeleted(final DbEntity dbEntity) {
        final CachedDbEntity cachedEntity = this.getCachedEntity(dbEntity);
        if (cachedEntity != null) {
            if (cachedEntity.getEntityState() == DbEntityState.TRANSIENT) {
                cachedEntity.setEntityState(DbEntityState.DELETED_TRANSIENT);
            }
            else if (cachedEntity.getEntityState() == DbEntityState.PERSISTENT) {
                cachedEntity.setEntityState(DbEntityState.DELETED_PERSISTENT);
            }
            else if (cachedEntity.getEntityState() == DbEntityState.MERGED) {
                cachedEntity.setEntityState(DbEntityState.DELETED_MERGED);
            }
        }
        else {
            final CachedDbEntity cachedDbEntity = new CachedDbEntity();
            cachedDbEntity.setEntity(dbEntity);
            cachedDbEntity.setEntityState(DbEntityState.DELETED_MERGED);
            this.putInternal(cachedDbEntity);
        }
    }
    
    public void undoDelete(final DbEntity dbEntity) {
        final CachedDbEntity cachedEntity = this.getCachedEntity(dbEntity);
        if (cachedEntity.getEntityState() == DbEntityState.DELETED_TRANSIENT) {
            cachedEntity.setEntityState(DbEntityState.TRANSIENT);
        }
        else {
            cachedEntity.setEntityState(DbEntityState.MERGED);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
