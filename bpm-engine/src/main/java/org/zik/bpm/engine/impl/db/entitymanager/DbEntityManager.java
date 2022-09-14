// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.identity.db.DbGroupQueryImpl;
import org.zik.bpm.engine.impl.GroupQueryImpl;
import org.zik.bpm.engine.impl.identity.db.DbUserQueryImpl;
import org.zik.bpm.engine.impl.UserQueryImpl;
import org.zik.bpm.engine.impl.HistoricJobLogQueryImpl;
import org.zik.bpm.engine.impl.HistoricVariableInstanceQueryImpl;
import org.zik.bpm.engine.impl.HistoricDetailQueryImpl;
import org.zik.bpm.engine.impl.HistoricTaskInstanceQueryImpl;
import org.zik.bpm.engine.impl.HistoricActivityInstanceQueryImpl;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.JobQueryImpl;
import org.zik.bpm.engine.impl.TaskQueryImpl;
import org.zik.bpm.engine.impl.ExecutionQueryImpl;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionQueryImpl;
import org.zik.bpm.engine.impl.ProcessDefinitionQueryImpl;
import org.zik.bpm.engine.impl.DeploymentQueryImpl;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperationType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.db.HistoricEntity;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.db.FlushResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Collection;
import org.zik.bpm.engine.impl.db.entitymanager.cache.CachedDbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.cache.DbEntityState;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperationManager;
import org.zik.bpm.engine.impl.db.entitymanager.cache.DbEntityCache;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import java.util.List;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.EntityLoadListener;
import org.zik.bpm.engine.impl.interceptor.Session;

public class DbEntityManager implements Session, EntityLoadListener
{
    protected static final EnginePersistenceLogger LOG;
    protected static final String TOGGLE_FOREIGN_KEY_STMT = "toggleForeignKey";
    public static final int BATCH_SIZE = 50;
    protected List<OptimisticLockingListener> optimisticLockingListeners;
    protected IdGenerator idGenerator;
    protected DbEntityCache dbEntityCache;
    protected DbOperationManager dbOperationManager;
    protected PersistenceSession persistenceSession;
    protected boolean isIgnoreForeignKeysForNextFlush;
    
    public DbEntityManager(final IdGenerator idGenerator, final PersistenceSession persistenceSession) {
        this.idGenerator = idGenerator;
        this.persistenceSession = persistenceSession;
        if (persistenceSession != null) {
            this.persistenceSession.addEntityLoadListener(this);
        }
        this.initializeEntityCache();
        this.initializeOperationManager();
    }
    
    protected void initializeOperationManager() {
        this.dbOperationManager = new DbOperationManager();
    }
    
    protected void initializeEntityCache() {
        final JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null && processEngineConfiguration.isDbEntityCacheReuseEnabled() && jobExecutorContext != null) {
            this.dbEntityCache = jobExecutorContext.getEntityCache();
            if (this.dbEntityCache == null) {
                jobExecutorContext.setEntityCache(this.dbEntityCache = new DbEntityCache(processEngineConfiguration.getDbEntityCacheKeyMapping()));
            }
        }
        else if (processEngineConfiguration != null) {
            this.dbEntityCache = new DbEntityCache(processEngineConfiguration.getDbEntityCacheKeyMapping());
        }
        else {
            this.dbEntityCache = new DbEntityCache();
        }
    }
    
    public List selectList(final String statement) {
        return this.selectList(statement, null, 0, Integer.MAX_VALUE);
    }
    
    public List selectList(final String statement, final Object parameter) {
        return this.selectList(statement, parameter, 0, Integer.MAX_VALUE);
    }
    
    public List selectList(final String statement, final Object parameter, final Page page) {
        if (page != null) {
            return this.selectList(statement, parameter, page.getFirstResult(), page.getMaxResults());
        }
        return this.selectList(statement, parameter, 0, Integer.MAX_VALUE);
    }
    
    public List selectList(final String statement, final ListQueryParameterObject parameter, final Page page) {
        return this.selectList(statement, parameter);
    }
    
    public List selectList(final String statement, final Object parameter, final int firstResult, final int maxResults) {
        return this.selectList(statement, new ListQueryParameterObject(parameter, firstResult, maxResults));
    }
    
    public List selectList(final String statement, final ListQueryParameterObject parameter) {
        return this.selectListWithRawParameter(statement, parameter, parameter.getFirstResult(), parameter.getMaxResults());
    }
    
    public List selectListWithRawParameter(final String statement, final Object parameter, final int firstResult, final int maxResults) {
        if (firstResult == -1 || maxResults == -1) {
            return Collections.EMPTY_LIST;
        }
        final List loadedObjects = this.persistenceSession.selectList(statement, parameter);
        return this.filterLoadedObjects(loadedObjects);
    }
    
    public Object selectOne(final String statement, final Object parameter) {
        Object result = this.persistenceSession.selectOne(statement, parameter);
        if (result instanceof DbEntity) {
            final DbEntity loadedObject = (DbEntity)result;
            result = this.cacheFilter(loadedObject);
        }
        return result;
    }
    
    public boolean selectBoolean(final String statement, final Object parameter) {
        final List<String> result = (List<String>)this.persistenceSession.selectList(statement, parameter);
        return result != null && result.contains(1);
    }
    
    public <T extends DbEntity> T selectById(final Class<T> entityClass, final String id) {
        T persistentObject = this.dbEntityCache.get(entityClass, id);
        if (persistentObject != null) {
            return persistentObject;
        }
        persistentObject = this.persistenceSession.selectById(entityClass, id);
        if (persistentObject == null) {
            return null;
        }
        return persistentObject;
    }
    
    public <T extends DbEntity> T getCachedEntity(final Class<T> type, final String id) {
        return this.dbEntityCache.get(type, id);
    }
    
    public <T extends DbEntity> List<T> getCachedEntitiesByType(final Class<T> type) {
        return this.dbEntityCache.getEntitiesByType(type);
    }
    
    protected List filterLoadedObjects(final List<Object> loadedObjects) {
        if (loadedObjects.isEmpty() || loadedObjects.get(0) == null) {
            return loadedObjects;
        }
        if (!DbEntity.class.isAssignableFrom(loadedObjects.get(0).getClass())) {
            return loadedObjects;
        }
        final List<DbEntity> filteredObjects = new ArrayList<DbEntity>(loadedObjects.size());
        for (final Object loadedObject : loadedObjects) {
            final DbEntity cachedPersistentObject = this.cacheFilter((DbEntity)loadedObject);
            filteredObjects.add(cachedPersistentObject);
        }
        return filteredObjects;
    }
    
    protected DbEntity cacheFilter(final DbEntity persistentObject) {
        final DbEntity cachedPersistentObject = this.dbEntityCache.get(persistentObject.getClass(), persistentObject.getId());
        if (cachedPersistentObject != null) {
            return cachedPersistentObject;
        }
        return persistentObject;
    }
    
    @Override
    public void onEntityLoaded(final DbEntity entity) {
        final DbEntity cachedPersistentObject = this.dbEntityCache.get(entity.getClass(), entity.getId());
        if (cachedPersistentObject == null) {
            this.dbEntityCache.putPersistent(entity);
            if (entity instanceof DbEntityLifecycleAware) {
                final DbEntityLifecycleAware lifecycleAware = (DbEntityLifecycleAware)entity;
                lifecycleAware.postLoad();
            }
        }
    }
    
    public void lock(final String statement) {
        this.lock(statement, null);
    }
    
    public void lock(final String statement, final Object parameter) {
        this.persistenceSession.lock(statement, parameter);
    }
    
    public boolean isDirty(final DbEntity dbEntity) {
        final CachedDbEntity cachedEntity = this.dbEntityCache.getCachedEntity(dbEntity);
        return cachedEntity != null && (cachedEntity.isDirty() || cachedEntity.getEntityState() == DbEntityState.MERGED);
    }
    
    @Override
    public void flush() {
        this.flushEntityCache();
        this.flushDbOperationManager();
    }
    
    public void setIgnoreForeignKeysForNextFlush(final boolean ignoreForeignKeysForNextFlush) {
        this.isIgnoreForeignKeysForNextFlush = ignoreForeignKeysForNextFlush;
    }
    
    protected void flushDbOperationManager() {
        final List<DbOperation> operationsToFlush = this.dbOperationManager.calculateFlush();
        if (operationsToFlush == null || operationsToFlush.size() == 0) {
            return;
        }
        DbEntityManager.LOG.databaseFlushSummary(operationsToFlush);
        if (this.isIgnoreForeignKeysForNextFlush) {
            this.persistenceSession.executeNonEmptyUpdateStmt("toggleForeignKey", false);
            this.persistenceSession.flushOperations();
        }
        try {
            final List<List<DbOperation>> batches = CollectionUtil.partition(operationsToFlush, 50);
            for (final List<DbOperation> batch : batches) {
                this.flushDbOperations(batch, operationsToFlush);
            }
        }
        finally {
            if (this.isIgnoreForeignKeysForNextFlush) {
                this.persistenceSession.executeNonEmptyUpdateStmt("toggleForeignKey", true);
                this.persistenceSession.flushOperations();
                this.isIgnoreForeignKeysForNextFlush = false;
            }
        }
    }
    
    protected void flushDbOperations(List<DbOperation> operationsToFlush, final List<DbOperation> allOperations) {
        while (!operationsToFlush.isEmpty()) {
            FlushResult flushResult;
            try {
                flushResult = this.persistenceSession.executeDbOperations(operationsToFlush);
            }
            catch (Exception e) {
                throw DbEntityManager.LOG.flushDbOperationUnexpectedException(allOperations, e);
            }
            final List<DbOperation> failedOperations = flushResult.getFailedOperations();
            for (final DbOperation failedOperation : failedOperations) {
                final DbOperation.State failureState = failedOperation.getState();
                if (failureState == DbOperation.State.FAILED_CONCURRENT_MODIFICATION) {
                    this.handleConcurrentModification(failedOperation);
                }
                else if (failureState == DbOperation.State.FAILED_CONCURRENT_MODIFICATION_CRDB) {
                    this.handleConcurrentModificationCrdb(failedOperation);
                }
                else {
                    if (failureState == DbOperation.State.FAILED_ERROR) {
                        final Exception failure = failedOperation.getFailure();
                        throw DbEntityManager.LOG.flushDbOperationException(allOperations, failedOperation, failure);
                    }
                    throw new ProcessEngineException("Entity session returned a failed operation not in an error state. This indicates a bug");
                }
            }
            final List<DbOperation> remainingOperations = flushResult.getRemainingOperations();
            EnsureUtil.ensureLessThan("Database flush did not process any operations. This indicates a bug.", "remainingOperations", remainingOperations.size(), operationsToFlush.size());
            operationsToFlush = remainingOperations;
        }
    }
    
    public void flushEntity(final DbEntity entity) {
        final CachedDbEntity cachedEntity = this.dbEntityCache.getCachedEntity(entity);
        if (cachedEntity != null) {
            this.flushCachedEntity(cachedEntity);
        }
        this.flushDbOperationManager();
    }
    
    protected void handleConcurrentModification(final DbOperation dbOperation) {
        OptimisticLockingResult handlingResult = this.invokeOptimisticLockingListeners(dbOperation);
        if (OptimisticLockingResult.THROW.equals(handlingResult) && this.canIgnoreHistoryModificationFailure(dbOperation)) {
            handlingResult = OptimisticLockingResult.IGNORE;
        }
        switch (handlingResult) {
            case IGNORE: {}
            default: {
                throw DbEntityManager.LOG.concurrentUpdateDbEntityException(dbOperation);
            }
        }
    }
    
    protected void handleConcurrentModificationCrdb(final DbOperation dbOperation) {
        final OptimisticLockingResult handlingResult = this.invokeOptimisticLockingListeners(dbOperation);
        if (OptimisticLockingResult.IGNORE.equals(handlingResult)) {
            DbEntityManager.LOG.crdbFailureIgnored(dbOperation);
        }
        throw DbEntityManager.LOG.crdbTransactionRetryException(dbOperation);
    }
    
    private OptimisticLockingResult invokeOptimisticLockingListeners(final DbOperation dbOperation) {
        OptimisticLockingResult handlingResult = OptimisticLockingResult.THROW;
        if (this.optimisticLockingListeners != null) {
            for (final OptimisticLockingListener optimisticLockingListener : this.optimisticLockingListeners) {
                if (optimisticLockingListener.getEntityType() == null || optimisticLockingListener.getEntityType().isAssignableFrom(dbOperation.getEntityType())) {
                    handlingResult = optimisticLockingListener.failedOperation(dbOperation);
                }
            }
        }
        return handlingResult;
    }
    
    protected boolean canIgnoreHistoryModificationFailure(final DbOperation dbOperation) {
        final DbEntity dbEntity = ((DbEntityOperation)dbOperation).getEntity();
        return Context.getProcessEngineConfiguration().isSkipHistoryOptimisticLockingExceptions() && (dbEntity instanceof HistoricEntity || this.isHistoricByteArray(dbEntity));
    }
    
    protected boolean isHistoricByteArray(final DbEntity dbEntity) {
        if (dbEntity instanceof ByteArrayEntity) {
            final ByteArrayEntity byteArrayEntity = (ByteArrayEntity)dbEntity;
            return byteArrayEntity.getType().equals(ResourceTypes.HISTORY.getValue());
        }
        return false;
    }
    
    protected void flushEntityCache() {
        final List<CachedDbEntity> cachedEntities = this.dbEntityCache.getCachedEntities();
        for (final CachedDbEntity cachedDbEntity : cachedEntities) {
            this.flushCachedEntity(cachedDbEntity);
        }
        DbEntityManager.LOG.flushedCacheState(this.dbEntityCache.getCachedEntities());
    }
    
    protected void flushCachedEntity(final CachedDbEntity cachedDbEntity) {
        if (cachedDbEntity.getEntityState() == DbEntityState.TRANSIENT) {
            cachedDbEntity.determineEntityReferences();
            this.performEntityOperation(cachedDbEntity, DbOperationType.INSERT);
            cachedDbEntity.setEntityState(DbEntityState.PERSISTENT);
        }
        else if (cachedDbEntity.getEntityState() == DbEntityState.PERSISTENT && cachedDbEntity.isDirty()) {
            this.performEntityOperation(cachedDbEntity, DbOperationType.UPDATE);
        }
        else if (cachedDbEntity.getEntityState() == DbEntityState.MERGED) {
            this.performEntityOperation(cachedDbEntity, DbOperationType.UPDATE);
            cachedDbEntity.setEntityState(DbEntityState.PERSISTENT);
        }
        else if (cachedDbEntity.getEntityState() == DbEntityState.DELETED_TRANSIENT) {
            this.dbEntityCache.remove(cachedDbEntity);
        }
        else if (cachedDbEntity.getEntityState() == DbEntityState.DELETED_PERSISTENT || cachedDbEntity.getEntityState() == DbEntityState.DELETED_MERGED) {
            this.performEntityOperation(cachedDbEntity, DbOperationType.DELETE);
            this.dbEntityCache.remove(cachedDbEntity);
        }
        if (cachedDbEntity.getEntityState() == DbEntityState.PERSISTENT) {
            cachedDbEntity.makeCopy();
            cachedDbEntity.determineEntityReferences();
        }
    }
    
    public void insert(final DbEntity dbEntity) {
        this.ensureHasId(dbEntity);
        this.validateId(dbEntity);
        this.dbEntityCache.putTransient(dbEntity);
    }
    
    public void merge(final DbEntity dbEntity) {
        if (dbEntity.getId() == null) {
            throw DbEntityManager.LOG.mergeDbEntityException(dbEntity);
        }
        this.dbEntityCache.putMerged(dbEntity);
    }
    
    public void forceUpdate(final DbEntity entity) {
        final CachedDbEntity cachedEntity = this.dbEntityCache.getCachedEntity(entity);
        if (cachedEntity != null && cachedEntity.getEntityState() == DbEntityState.PERSISTENT) {
            cachedEntity.forceSetDirty();
        }
    }
    
    public void delete(final DbEntity dbEntity) {
        this.dbEntityCache.setDeleted(dbEntity);
    }
    
    public void undoDelete(final DbEntity entity) {
        this.dbEntityCache.undoDelete(entity);
    }
    
    public void update(final Class<? extends DbEntity> entityType, final String statement, final Object parameter) {
        this.performBulkOperation(entityType, statement, parameter, DbOperationType.UPDATE_BULK);
    }
    
    public void updatePreserveOrder(final Class<? extends DbEntity> entityType, final String statement, final Object parameter) {
        this.performBulkOperationPreserveOrder(entityType, statement, parameter, DbOperationType.UPDATE_BULK);
    }
    
    public void delete(final Class<? extends DbEntity> entityType, final String statement, final Object parameter) {
        this.performBulkOperation(entityType, statement, parameter, DbOperationType.DELETE_BULK);
    }
    
    public DbBulkOperation deletePreserveOrder(final Class<? extends DbEntity> entityType, final String statement, final Object parameter) {
        return this.performBulkOperationPreserveOrder(entityType, statement, parameter, DbOperationType.DELETE_BULK);
    }
    
    protected DbBulkOperation performBulkOperation(final Class<? extends DbEntity> entityType, final String statement, final Object parameter, final DbOperationType operationType) {
        final DbBulkOperation bulkOperation = this.createDbBulkOperation(entityType, statement, parameter, operationType);
        this.dbOperationManager.addOperation(bulkOperation);
        return bulkOperation;
    }
    
    protected DbBulkOperation performBulkOperationPreserveOrder(final Class<? extends DbEntity> entityType, final String statement, final Object parameter, final DbOperationType operationType) {
        final DbBulkOperation bulkOperation = this.createDbBulkOperation(entityType, statement, parameter, operationType);
        this.dbOperationManager.addOperationPreserveOrder(bulkOperation);
        return bulkOperation;
    }
    
    private DbBulkOperation createDbBulkOperation(final Class<? extends DbEntity> entityType, final String statement, final Object parameter, final DbOperationType operationType) {
        final DbBulkOperation bulkOperation = new DbBulkOperation();
        bulkOperation.setOperationType(operationType);
        bulkOperation.setEntityType(entityType);
        bulkOperation.setStatement(statement);
        bulkOperation.setParameter(parameter);
        return bulkOperation;
    }
    
    protected void performEntityOperation(final CachedDbEntity cachedDbEntity, final DbOperationType type) {
        final DbEntityOperation dbOperation = new DbEntityOperation();
        dbOperation.setEntity(cachedDbEntity.getEntity());
        dbOperation.setFlushRelevantEntityReferences(cachedDbEntity.getFlushRelevantEntityReferences());
        dbOperation.setOperationType(type);
        this.dbOperationManager.addOperation(dbOperation);
    }
    
    @Override
    public void close() {
    }
    
    public boolean isDeleted(final DbEntity object) {
        return this.dbEntityCache.isDeleted(object);
    }
    
    protected void ensureHasId(final DbEntity dbEntity) {
        if (dbEntity.getId() == null) {
            final String nextId = this.idGenerator.getNextId();
            dbEntity.setId(nextId);
        }
    }
    
    protected void validateId(final DbEntity dbEntity) {
        EnsureUtil.ensureValidIndividualResourceId("Entity " + dbEntity + " has an invalid id", dbEntity.getId());
    }
    
    public <T extends DbEntity> List<T> pruneDeletedEntities(final List<T> listToPrune) {
        final ArrayList<T> prunedList = new ArrayList<T>();
        for (final T potentiallyDeleted : listToPrune) {
            if (!this.isDeleted(potentiallyDeleted)) {
                prunedList.add(potentiallyDeleted);
            }
        }
        return prunedList;
    }
    
    public boolean contains(final DbEntity dbEntity) {
        return this.dbEntityCache.contains(dbEntity);
    }
    
    public DbOperationManager getDbOperationManager() {
        return this.dbOperationManager;
    }
    
    public void setDbOperationManager(final DbOperationManager operationManager) {
        this.dbOperationManager = operationManager;
    }
    
    public DbEntityCache getDbEntityCache() {
        return this.dbEntityCache;
    }
    
    public void setDbEntityCache(final DbEntityCache dbEntityCache) {
        this.dbEntityCache = dbEntityCache;
    }
    
    public DeploymentQueryImpl createDeploymentQuery() {
        return new DeploymentQueryImpl();
    }
    
    public ProcessDefinitionQueryImpl createProcessDefinitionQuery() {
        return new ProcessDefinitionQueryImpl();
    }
    
    public CaseDefinitionQueryImpl createCaseDefinitionQuery() {
        return new CaseDefinitionQueryImpl();
    }
    
    public ProcessInstanceQueryImpl createProcessInstanceQuery() {
        return new ProcessInstanceQueryImpl();
    }
    
    public ExecutionQueryImpl createExecutionQuery() {
        return new ExecutionQueryImpl();
    }
    
    public TaskQueryImpl createTaskQuery() {
        return new TaskQueryImpl();
    }
    
    public JobQueryImpl createJobQuery() {
        return new JobQueryImpl();
    }
    
    public HistoricProcessInstanceQueryImpl createHistoricProcessInstanceQuery() {
        return new HistoricProcessInstanceQueryImpl();
    }
    
    public HistoricActivityInstanceQueryImpl createHistoricActivityInstanceQuery() {
        return new HistoricActivityInstanceQueryImpl();
    }
    
    public HistoricTaskInstanceQueryImpl createHistoricTaskInstanceQuery() {
        return new HistoricTaskInstanceQueryImpl();
    }
    
    public HistoricDetailQueryImpl createHistoricDetailQuery() {
        return new HistoricDetailQueryImpl();
    }
    
    public HistoricVariableInstanceQueryImpl createHistoricVariableInstanceQuery() {
        return new HistoricVariableInstanceQueryImpl();
    }
    
    public HistoricJobLogQueryImpl createHistoricJobLogQuery() {
        return new HistoricJobLogQueryImpl();
    }
    
    public UserQueryImpl createUserQuery() {
        return new DbUserQueryImpl();
    }
    
    public GroupQueryImpl createGroupQuery() {
        return new DbGroupQueryImpl();
    }
    
    public void registerOptimisticLockingListener(final OptimisticLockingListener optimisticLockingListener) {
        if (this.optimisticLockingListeners == null) {
            this.optimisticLockingListeners = new ArrayList<OptimisticLockingListener>();
        }
        this.optimisticLockingListeners.add(optimisticLockingListener);
    }
    
    public List<String> getTableNamesPresentInDatabase() {
        return this.persistenceSession.getTableNamesPresent();
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
