// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.CrdbTransactionRetryException;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.WrongDbException;
import org.zik.bpm.engine.SuspendedEntityInteractionException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.BadUserRequestException;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.authorization.MissingAuthorization;
import org.zik.bpm.engine.AuthorizationException;
import org.zik.bpm.engine.impl.util.ClassNameUtil;
import org.zik.bpm.engine.impl.db.entitymanager.cache.CachedDbEntity;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.List;
import org.zik.bpm.engine.impl.db.entitymanager.cache.DbEntityState;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class EnginePersistenceLogger extends ProcessEngineLogger
{
    protected static final String HINT_TEXT = "Hint: Set <property name=\"databaseSchemaUpdate\" to value=\"true\" or value=\"create-drop\" (use create-drop for testing only!) in bean processEngineConfiguration in camunda.cfg.xml for automatic schema creation";
    
    protected String buildStringFromList(final Collection<?> list) {
        final StringBuilder message = new StringBuilder();
        message.append("[");
        message.append("\n");
        for (final Object object : list) {
            message.append("  ");
            message.append(object.toString());
            message.append("\n");
        }
        message.append("]");
        return message.toString();
    }
    
    private String buildStringFromMap(final Map<String, ?> map) {
        final StringBuilder message = new StringBuilder();
        message.append("[");
        message.append("\n");
        for (final Map.Entry<String, ?> entry : map.entrySet()) {
            message.append("  ");
            message.append(entry.getKey());
            message.append(": ");
            message.append(entry.getValue().toString());
            message.append("\n");
        }
        message.append("]");
        return message.toString();
    }
    
    public <T extends DbEntity> ProcessEngineException entityCacheLookupException(final Class<T> type, final String id, final Class<? extends DbEntity> entity, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("001", "Could not lookup entity of type '{}' and id '{}': found entity of type '{}'.", new Object[] { type, id, entity }), cause);
    }
    
    public ProcessEngineException entityCacheDuplicateEntryException(final String currentState, final String id, final Class<? extends DbEntity> entityClass, final DbEntityState foundState) {
        return new ProcessEngineException(this.exceptionMessage("002", "Cannot add {} entity with id '{}' and type '{}' into cache. An entity with the same id and type is already in state '{}'", new Object[] { currentState, id, entityClass, foundState }));
    }
    
    public ProcessEngineException alreadyMarkedEntityInEntityCacheException(final String id, final Class<? extends DbEntity> entityClass, final DbEntityState state) {
        return new ProcessEngineException(this.exceptionMessage("003", "Inserting an entity with Id '{}' and type '{}' which is already marked with state '{}'", new Object[] { id, entityClass, state }));
    }
    
    public ProcessEngineException flushDbOperationException(final List<DbOperation> operationsToFlush, final DbOperation failedOperation, final Throwable e) {
        final String message = ExceptionUtil.collectExceptionMessages(e);
        final String exceptionMessage = this.exceptionMessage("004", "Exception while executing Database Operation '{}' with message '{}'. Flush summary: \n {}", new Object[] { failedOperation, message, this.buildStringFromList(operationsToFlush) });
        final ProcessEngineException subException = new ProcessEngineException(exceptionMessage, e);
        return ExceptionUtil.wrapPersistenceException(subException);
    }
    
    public OptimisticLockingException concurrentUpdateDbEntityException(final DbOperation operation) {
        return new OptimisticLockingException(this.exceptionMessage("005", "Execution of '{}' failed. Entity was updated by another transaction concurrently.", new Object[] { operation }));
    }
    
    public void flushedCacheState(final List<CachedDbEntity> cachedEntities) {
        if (this.isDebugEnabled()) {
            this.logDebug("006", "Cache state after flush: {}", new Object[] { this.buildStringFromList(cachedEntities) });
        }
    }
    
    public ProcessEngineException mergeDbEntityException(final DbEntity entity) {
        return new ProcessEngineException(this.exceptionMessage("007", "Cannot merge DbEntity '{}' without id", new Object[] { entity }));
    }
    
    public void databaseFlushSummary(final Collection<DbOperation> operations) {
        if (this.isDebugEnabled()) {
            this.logDebug("008", "Flush Summary: {}", new Object[] { this.buildStringFromList(operations) });
        }
    }
    
    public void executeDatabaseOperation(final String operationType, final Object parameter) {
        if (this.isDebugEnabled()) {
            String message;
            if (parameter != null) {
                message = parameter.toString();
            }
            else {
                message = "null";
            }
            if (parameter instanceof DbEntity) {
                final DbEntity dbEntity = (DbEntity)parameter;
                message = ClassNameUtil.getClassNameWithoutPackage(dbEntity) + "[id=" + dbEntity.getId() + "]";
            }
            this.logDebug("009", "SQL operation: '{}'; Entity: '{}'", new Object[] { operationType, message });
        }
    }
    
    public void executeDatabaseBulkOperation(final String operationType, final String statement, final Object parameter) {
        this.logDebug("010", "SQL bulk operation: '{}'; Statement: '{}'; Parameter: '{}'", new Object[] { operationType, statement, parameter });
    }
    
    public void fetchDatabaseTables(final String source, final List<String> tableNames) {
        if (this.isDebugEnabled()) {
            this.logDebug("011", "Retrieving process engine tables from: '{}'. Retrieved tables: {}", new Object[] { source, this.buildStringFromList(tableNames) });
        }
    }
    
    public void missingSchemaResource(final String resourceName, final String operation) {
        this.logDebug("012", "There is no schema resource '{}' for operation '{}'.", new Object[] { resourceName, operation });
    }
    
    public ProcessEngineException missingSchemaResourceException(final String resourceName, final String operation) {
        return new ProcessEngineException(this.exceptionMessage("013", "There is no schema resource '{}' for operation '{}'.", new Object[] { resourceName, operation }));
    }
    
    public ProcessEngineException missingSchemaResourceFileException(final String fileName, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("014", "Cannot find schema resource file with name '{}'", new Object[] { fileName }), cause);
    }
    
    public void failedDatabaseOperation(final String operation, final String statement, final Throwable cause) {
        this.logError("015", "Problem during schema operation '{}' with statement '{}'. Cause: '{}'", new Object[] { operation, statement, cause.getMessage() });
    }
    
    public void performingDatabaseOperation(final String operation, final String component, final String resourceName) {
        this.logInfo("016", "Performing database operation '{}' on component '{}' with resource '{}'", new Object[] { operation, component, resourceName });
    }
    
    public void successfulDatabaseOperation(final String operation, final String component) {
        this.logDebug("Database schema operation '{}' for component '{}' was successful.", operation, new Object[] { component });
    }
    
    public ProcessEngineException performDatabaseOperationException(final String operation, final String sql, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("017", "Could not perform operation '{}' on database schema for SQL Statement: '{}'.", new Object[] { operation, sql }), cause);
    }
    
    public ProcessEngineException checkDatabaseTableException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("018", "Could not check if tables are already present using metadata.", new Object[0]), cause);
    }
    
    public ProcessEngineException getDatabaseTableNameException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("019", "Unable to fetch process engine table names.", new Object[0]), cause);
    }
    
    public ProcessEngineException missingRelationMappingException(final String relation) {
        return new ProcessEngineException(this.exceptionMessage("020", "There is no mapping for the relation '{}' registered.", new Object[] { relation }));
    }
    
    public ProcessEngineException databaseHistoryLevelException(final String level) {
        return new ProcessEngineException(this.exceptionMessage("021", "historyLevel '{}' is higher then 'none' and dbHistoryUsed is set to false.", new Object[] { level }));
    }
    
    public ProcessEngineException invokeSchemaResourceToolException(final int length) {
        return new ProcessEngineException(this.exceptionMessage("022", "Schema resource tool was invoked with '{}' parameters.Schema resource tool must be invoked with exactly 2 parameters:\n - 1st parameter is the process engine configuration file,\n - 2nd parameter is the schema resource file name", new Object[] { length }));
    }
    
    public ProcessEngineException loadModelException(final String type, final String modelName, final String id, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("023", "Could not load {} Model for {} definition with id '{}'.", new Object[] { type, modelName, id }), cause);
    }
    
    public void removeEntryFromDeploymentCacheFailure(final String modelName, final String id, final Throwable cause) {
        this.logWarn("024", "Could not remove {} definition with id '{}' from the cache. Reason: '{}'", new Object[] { modelName, id, cause.getMessage(), cause });
    }
    
    public ProcessEngineException engineAuthorizationTypeException(final int usedType, final int global, final int grant, final int revoke) {
        return new ProcessEngineException(this.exceptionMessage("025", "Unrecognized authorization type '{}'. Must be one of ['{}', '{}', '{}']", new Object[] { usedType, global, grant, revoke }));
    }
    
    public IllegalStateException permissionStateException(final String methodName, final String type) {
        return new IllegalStateException(this.exceptionMessage("026", "Method '{}' cannot be used for authorization with type '{}'.", new Object[] { methodName, type }));
    }
    
    public ProcessEngineException notUsableGroupIdForGlobalAuthorizationException() {
        return new ProcessEngineException(this.exceptionMessage("027", "Cannot use 'groupId' for GLOBAL authorization", new Object[0]));
    }
    
    public ProcessEngineException illegalValueForUserIdException(final String id, final String expected) {
        return new ProcessEngineException(this.exceptionMessage("028", "Illegal value '{}' for userId for GLOBAL authorization. Must be '{}'", new Object[] { id, expected }));
    }
    
    public AuthorizationException requiredCamundaAdmin() {
        return this.requiredCamundaAdminOrPermissionException(null);
    }
    
    public AuthorizationException requiredCamundaAdminOrPermissionException(final List<MissingAuthorization> missingAuthorizations) {
        String exceptionCode = "029";
        final StringBuilder sb = new StringBuilder();
        sb.append("Required admin authenticated group or user");
        if (missingAuthorizations != null && !missingAuthorizations.isEmpty()) {
            sb.append(" or any of the following permissions: ");
            sb.append(AuthorizationException.generateMissingAuthorizationsList(missingAuthorizations));
            exceptionCode = "110";
        }
        sb.append(".");
        return new AuthorizationException(this.exceptionMessage(exceptionCode, sb.toString(), new Object[0]));
    }
    
    public void createChildExecution(final ExecutionEntity child, final ExecutionEntity parent) {
        if (this.isDebugEnabled()) {
            this.logDebug("030", "Child execution '{}' created with parent '{}'.", new Object[] { child.toString(), parent.toString() });
        }
    }
    
    public void initializeExecution(final ExecutionEntity entity) {
        this.logDebug("031", "Initializing execution '{}'", new Object[] { entity.toString() });
    }
    
    public void initializeTimerDeclaration(final ExecutionEntity entity) {
        this.logDebug("032", "Initializing timer declaration '{}'", new Object[] { entity.toString() });
    }
    
    public ProcessEngineException requiredAsyncContinuationException(final String id) {
        return new ProcessEngineException(this.exceptionMessage("033", "Asynchronous Continuation for activity with id '{}' requires a message job declaration", new Object[] { id }));
    }
    
    public ProcessEngineException restoreProcessInstanceException(final ExecutionEntity entity) {
        return new ProcessEngineException(this.exceptionMessage("034", "Can only restore process instances. This method must be called on a process instance execution but was called on '{}'", new Object[] { entity.toString() }));
    }
    
    public ProcessEngineException executionNotFoundException(final String id) {
        return new ProcessEngineException(this.exceptionMessage("035", "Unable to find execution for id '{}'", new Object[] { id }));
    }
    
    public ProcessEngineException castModelInstanceException(final ModelElementInstance instance, final String toElement, final String type, final String namespace, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("036", "Cannot cast '{}' to '{}'. Element is of type '{}' with namespace '{}'.", new Object[] { instance, toElement, type, namespace }), cause);
    }
    
    public BadUserRequestException requestedProcessInstanceNotFoundException(final String id) {
        return new BadUserRequestException(this.exceptionMessage("037", "No process instance found for id '{}'", new Object[] { id }));
    }
    
    public NotValidException queryExtensionException(final String extendedClassName, final String extendingClassName) {
        return new NotValidException(this.exceptionMessage("038", "Unable to extend a query of class '{}' by a query of class '{}'.", new Object[] { extendedClassName, extendingClassName }));
    }
    
    public ProcessEngineException unsupportedResourceTypeException(final String type) {
        return new ProcessEngineException(this.exceptionMessage("039", "Unsupported resource type '{}'", new Object[] { type }));
    }
    
    public ProcessEngineException serializerNotDefinedException(final Object entity) {
        return new ProcessEngineException(this.exceptionMessage("040", "No serializer defined for variable instance '{}'", new Object[] { entity }));
    }
    
    public ProcessEngineException serializerOutOfContextException() {
        return new ProcessEngineException(this.exceptionMessage("041", "Cannot work with serializers outside of command context.", new Object[0]));
    }
    
    public ProcessEngineException taskIsAlreadyAssignedException(final String usedId, final String foundId) {
        return new ProcessEngineException(this.exceptionMessage("042", "Cannot assign '{}' to a task assignment that has already '{}' set.", new Object[] { usedId, foundId }));
    }
    
    public SuspendedEntityInteractionException suspendedEntityException(final String type, final String id) {
        return new SuspendedEntityInteractionException(this.exceptionMessage("043", "{} with id '{}' is suspended.", new Object[] { type, id }));
    }
    
    public void logUpdateUnrelatedProcessDefinitionEntity(final String thisKey, final String thatKey, final String thisDeploymentId, final String thatDeploymentId) {
        this.logDebug("044", "Cannot update entity from an unrelated process definition: this key '{}', that key '{}', this deploymentId '{}', that deploymentId '{}'", new Object[] { thisKey, thatKey, thisDeploymentId, thatDeploymentId });
    }
    
    public ProcessEngineException toManyProcessDefinitionsException(final int count, final String key, final String versionAttribute, final String versionValue, final String tenantId) {
        return new ProcessEngineException(this.exceptionMessage("045", "There are '{}' results for a process definition with key '{}', {} '{}' and tenant-id '{}'.", new Object[] { count, key, versionAttribute, versionValue }));
    }
    
    public ProcessEngineException notAllowedIdException(final String id) {
        return new ProcessEngineException(this.exceptionMessage("046", "Cannot set id '{}'. Only the provided id generation is allowed for properties.", new Object[] { id }));
    }
    
    public void countRowsPerProcessEngineTable(final Map<String, Long> map) {
        if (this.isDebugEnabled()) {
            this.logDebug("047", "Number of rows per process engine table: {}", new Object[] { this.buildStringFromMap(map) });
        }
    }
    
    public ProcessEngineException countTableRowsException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("048", "Could not fetch table counts.", new Object[0]), cause);
    }
    
    public void selectTableCountForTable(final String name) {
        this.logDebug("049", "Selecting table count for table with name '{}'", new Object[] { name });
    }
    
    public ProcessEngineException retrieveMetadataException(final Throwable cause) {
        final String exceptionMessage = this.exceptionMessage("050", "Could not retrieve database metadata. Reason: '{}'", new Object[] { cause.getMessage() });
        final ProcessEngineException exception = new ProcessEngineException(exceptionMessage, cause);
        return ExceptionUtil.wrapPersistenceException(exception);
    }
    
    public ProcessEngineException invokeTaskListenerException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("051", "There was an exception while invoking the TaskListener. Message: '{}'", new Object[] { cause.getMessage() }), cause);
    }
    
    public BadUserRequestException uninitializedFormKeyException() {
        return new BadUserRequestException(this.exceptionMessage("052", "The form key / form reference is not initialized. You must call initializeFormKeys() on the task query before you can retrieve the form key or the form reference.", new Object[0]));
    }
    
    public ProcessEngineException disabledHistoryException() {
        return new ProcessEngineException(this.exceptionMessage("053", "History is not enabled.", new Object[0]));
    }
    
    public ProcessEngineException instantiateSessionException(final String name, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("054", "Could not instantiate class '{}'. Message: '{}'", new Object[] { name, cause.getMessage() }), cause);
    }
    
    public WrongDbException wrongDbVersionException(final String version, final String dbVersion) {
        return new WrongDbException(this.exceptionMessage("055", "Version mismatch: Camunda library version is '{}' and db version is '{}'. Hint: Set <property name=\"databaseSchemaUpdate\" to value=\"true\" or value=\"create-drop\" (use create-drop for testing only!) in bean processEngineConfiguration in camunda.cfg.xml for automatic schema creation", new Object[] { version, dbVersion }), version, dbVersion);
    }
    
    public ProcessEngineException missingTableException(final List<String> components) {
        return new ProcessEngineException(this.exceptionMessage("056", "Tables are missing for the following components: {}", new Object[] { this.buildStringFromList(components) }));
    }
    
    public ProcessEngineException missingActivitiTablesException() {
        return new ProcessEngineException(this.exceptionMessage("057", "There are no Camunda tables in the database. Hint: Set <property name=\"databaseSchemaUpdate\" to value=\"true\" or value=\"create-drop\" (use create-drop for testing only!) in bean processEngineConfiguration in camunda.cfg.xml for automatic schema creation", new Object[0]));
    }
    
    public ProcessEngineException unableToFetchDbSchemaVersion(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("058", "Could not fetch the database schema version.", new Object[0]), cause);
    }
    
    public void failedTofetchVariableValue(final Throwable cause) {
        this.logDebug("059", "Could not fetch value for variable.", new Object[] { cause });
    }
    
    public ProcessEngineException historicDecisionInputInstancesNotFetchedException() {
        return new ProcessEngineException(this.exceptionMessage("060", "The input instances for the historic decision instance are not fetched. You must call 'includeInputs()' on the query to enable fetching.", new Object[0]));
    }
    
    public ProcessEngineException historicDecisionOutputInstancesNotFetchedException() {
        return new ProcessEngineException(this.exceptionMessage("061", "The output instances for the historic decision instance are not fetched. You must call 'includeOutputs()' on the query to enable fetching.", new Object[0]));
    }
    
    public void executingDDL(final List<String> logLines) {
        if (this.isDebugEnabled()) {
            this.logDebug("062", "Executing Schmema DDL {}", new Object[] { this.buildStringFromList(logLines) });
        }
    }
    
    public ProcessEngineException collectResultValueOfUnsupportedTypeException(final TypedValue collectResultValue) {
        return new ProcessEngineException(this.exceptionMessage("063", "The collect result value '{}' of the decision table result is not of type integer, long or double.", new Object[] { collectResultValue }));
    }
    
    public void creatingHistoryLevelPropertyInDatabase(final HistoryLevel historyLevel) {
        this.logInfo("065", "Creating historyLevel property in database for level: {}", new Object[] { historyLevel });
    }
    
    public void couldNotSelectHistoryLevel(final String message) {
        this.logWarn("066", "Could not select history level property: {}", new Object[] { message });
    }
    
    public void noHistoryLevelPropertyFound() {
        this.logInfo("067", "No history level property found in database", new Object[0]);
    }
    
    public void noDeploymentLockPropertyFound() {
        this.logError("068", "No deployment lock property found in databse", new Object[0]);
    }
    
    public void debugJobExecuted(final JobEntity jobEntity) {
        this.logDebug("069", "Job executed, deleting it", new Object[] { jobEntity });
    }
    
    public ProcessEngineException multipleTenantsForProcessDefinitionKeyException(final String processDefinitionKey) {
        return new ProcessEngineException(this.exceptionMessage("070", "Cannot resolve a unique process definition for key '{}' because it exists for multiple tenants.", new Object[] { processDefinitionKey }));
    }
    
    public ProcessEngineException cannotDeterminePaDataformats(final ProcessApplicationUnavailableException e) {
        return new ProcessEngineException(this.exceptionMessage("071", "Cannot determine process application variable serializers. Context Process Application is unavailable.", new Object[0]), e);
    }
    
    public ProcessEngineException cannotChangeTenantIdOfTask(final String taskId, final String currentTenantId, final String tenantIdToSet) {
        return new ProcessEngineException(this.exceptionMessage("072", "Cannot change tenantId of Task '{}'. Current tenant id '{}', Tenant id to set '{}'", new Object[] { taskId, currentTenantId, tenantIdToSet }));
    }
    
    public ProcessEngineException cannotSetDifferentTenantIdOnSubtask(final String parentTaskId, final String tenantId, final String tenantIdToSet) {
        return new ProcessEngineException(this.exceptionMessage("073", "Cannot set different tenantId on subtask than on parent Task. Parent taskId: '{}', tenantId: '{}', tenant id to set '{}'", new Object[] { parentTaskId, tenantId, tenantIdToSet }));
    }
    
    public ProcessEngineException multipleTenantsForDecisionDefinitionKeyException(final String decisionDefinitionKey) {
        return new ProcessEngineException(this.exceptionMessage("074", "Cannot resolve a unique decision definition for key '{}' because it exists for multiple tenants.", new Object[] { decisionDefinitionKey }));
    }
    
    public ProcessEngineException multipleTenantsForCaseDefinitionKeyException(final String caseDefinitionKey) {
        return new ProcessEngineException(this.exceptionMessage("075", "Cannot resolve a unique case definition for key '{}' because it exists for multiple tenants.", new Object[] { caseDefinitionKey }));
    }
    
    public ProcessEngineException deleteProcessDefinitionWithProcessInstancesException(final String processDefinitionId, final Long processInstanceCount) {
        return new ProcessEngineException(this.exceptionMessage("076", "Deletion of process definition without cascading failed. Process definition with id: {} can't be deleted, since there exists {} dependening process instances.", new Object[] { processDefinitionId, processInstanceCount }));
    }
    
    public ProcessEngineException resolveParentOfExecutionFailedException(final String parentId, final String executionId) {
        return new ProcessEngineException(this.exceptionMessage("077", "Cannot resolve parent with id '{}' of execution '{}', perhaps it was deleted in the meantime", new Object[] { parentId, executionId }));
    }
    
    public void noHistoryCleanupLockPropertyFound() {
        this.logError("078", "No history cleanup lock property found in databse", new Object[0]);
    }
    
    public void logUpdateUnrelatedCaseDefinitionEntity(final String thisKey, final String thatKey, final String thisDeploymentId, final String thatDeploymentId) {
        this.logDebug("079", "Cannot update entity from an unrelated case definition: this key '{}', that key '{}', this deploymentId '{}', that deploymentId '{}'", new Object[] { thisKey, thatKey, thisDeploymentId, thatDeploymentId });
    }
    
    public void logUpdateUnrelatedDecisionDefinitionEntity(final String thisKey, final String thatKey, final String thisDeploymentId, final String thatDeploymentId) {
        this.logDebug("080", "Cannot update entity from an unrelated decision definition: this key '{}', that key '{}', this deploymentId '{}', that deploymentId '{}'", new Object[] { thisKey, thatKey, thisDeploymentId, thatDeploymentId });
    }
    
    public void noStartupLockPropertyFound() {
        this.logError("081", "No startup lock property found in database", new Object[0]);
    }
    
    public ProcessEngineException flushDbOperationUnexpectedException(final List<DbOperation> operationsToFlush, final Throwable cause) {
        final String exceptionMessage = this.exceptionMessage("083", "Unexpected exception while executing database operations with message '{}'. Flush summary: \n {}", new Object[] { ExceptionUtil.collectExceptionMessages(cause), this.buildStringFromList(operationsToFlush) });
        final ProcessEngineException subException = new ProcessEngineException(exceptionMessage, cause);
        return ExceptionUtil.wrapPersistenceException(subException);
    }
    
    public ProcessEngineException wrongBatchResultsSizeException(final List<DbOperation> operationsToFlush) {
        return new ProcessEngineException(this.exceptionMessage("084", "Exception while executing Batch Database Operations: the size of Batch Result does not correspond to the number of flushed operations. Flush summary: \n {}", new Object[] { this.buildStringFromList(operationsToFlush) }));
    }
    
    public ProcessEngineException multipleDefinitionsForVersionTagException(final String decisionDefinitionKey, final String decisionDefinitionVersionTag) {
        return new ProcessEngineException(this.exceptionMessage("085", "Found more than one decision definition for key '{}' and versionTag '{}'", new Object[] { decisionDefinitionKey, decisionDefinitionVersionTag }));
    }
    
    public BadUserRequestException invalidResourceForPermission(final String resourceType, final String permission) {
        return new BadUserRequestException(this.exceptionMessage("086", "The resource type '{}' is not valid for '{}' permission.", new Object[] { resourceType, permission }));
    }
    
    public BadUserRequestException invalidResourceForAuthorization(final int resourceType, final String permission) {
        return new BadUserRequestException(this.exceptionMessage("087", "The resource type with id:'{}' is not valid for '{}' permission.", new Object[] { resourceType, permission }));
    }
    
    public BadUserRequestException disabledPermissionException(final String permission) {
        return new BadUserRequestException(this.exceptionMessage("088", "The '{}' permission is disabled, please check your process engine configuration.", new Object[] { permission }));
    }
    
    public ProcessEngineException batchingNotSupported(final DbOperation operation) {
        throw new ProcessEngineException(this.exceptionMessage("089", "Batching not supported: The jdbc driver in use does not return the number of affected rows when executing statement batches. Consider setting the engine configuration property 'jdbcBatchProcessing' to false.Failed operation: {}", new Object[] { operation }));
    }
    
    public ProcessEngineException disabledHistoricInstancePermissionsException() {
        return new BadUserRequestException(this.exceptionMessage("090", "Historic instance permissions are disabled, please check your process engine configuration.", new Object[0]));
    }
    
    public void noTelemetryLockPropertyFound() {
        this.logDebug("091", "No telemetry lock property found in the database", new Object[0]);
    }
    
    public void noTelemetryPropertyFound() {
        this.logDebug("092", "No telemetry property found in the database", new Object[0]);
    }
    
    public void creatingTelemetryPropertyInDatabase(final Boolean telemetryEnabled) {
        this.logDebug("093", "Creating the telemetry property in database with the value: {}", new Object[] { telemetryEnabled });
    }
    
    public void errorFetchingTelemetryPropertyInDatabase(final Exception exception) {
        this.logDebug("094", "Error while fetching the telemetry property from the database: {}", new Object[] { exception.getMessage() });
    }
    
    public void errorConfiguringTelemetryProperty(final Exception exception) {
        this.logDebug("095", "Error while configuring the telemetry property: {}", new Object[] { exception.getMessage() });
    }
    
    public void noInstallationIdPropertyFound() {
        this.logDebug("096", "No installation id property found in database", new Object[0]);
    }
    
    public void creatingInstallationPropertyInDatabase(final String value) {
        this.logDebug("097", "Creating the installation id property in database with the value: {}", new Object[] { value });
    }
    
    public void couldNotSelectInstallationId(final String message) {
        this.logDebug("098", "Could not select installation id property: {}", new Object[] { message });
    }
    
    public void noInstallationIdLockPropertyFound() {
        this.logDebug("099", "No installation id lock property found in the database", new Object[0]);
    }
    
    public void installationIdPropertyFound(final String value) {
        this.logDebug("100", "Installation id property found in the database: {}", new Object[] { value });
    }
    
    public void ignoreFailureDuePreconditionNotMet(final DbOperation ignoredOperation, final String preconditionMessage, final DbOperation failedOperation) {
        this.logDebug("101", "Ignoring '{}' database operation failure due to an unmet precondition. {}: '{}'", new Object[] { ignoredOperation.toString(), preconditionMessage, failedOperation.toString() });
    }
    
    public CrdbTransactionRetryException crdbTransactionRetryException(final DbOperation operation) {
        return new CrdbTransactionRetryException(this.exceptionMessage("102", "Execution of '{}' failed. Entity was updated by another transaction concurrently, and the transaction needs to be retried", new Object[] { operation }), operation.getFailure());
    }
    
    public CrdbTransactionRetryException crdbTransactionRetryExceptionOnSelect(final Throwable cause) {
        return new CrdbTransactionRetryException(this.exceptionMessage("103", "Execution of SELECT statement failed. The transaction needs to be retried.", new Object[0]), cause);
    }
    
    public CrdbTransactionRetryException crdbTransactionRetryExceptionOnCommit(final Throwable cause) {
        return new CrdbTransactionRetryException(this.exceptionMessage("104", "Could not commit transaction. The transaction needs to be retried.", new Object[0]), cause);
    }
    
    public void crdbFailureIgnored(final DbOperation operation) {
        this.logDebug("105", "An OptimisticLockingListener attempted to ignore a failure of: {}. Since CockroachDB aborted the transaction, ignoring the failure is not possible and an exception is thrown instead.", new Object[] { operation });
    }
    
    public void debugDisabledPessimisticLocks() {
        this.logDebug("106", "No exclusive lock is acquired on CockroachDB or H2, as pessimistic locks are disabled on these databases.", new Object[0]);
    }
    
    public void errorFetchingTelemetryInitialMessagePropertyInDatabase(final Exception exception) {
        this.logDebug("107", "Error while fetching the telemetry initial message status property from the database: {}", new Object[] { exception.getMessage() });
    }
    
    public void logTaskWithoutExecution(final String taskId) {
        this.logDebug("108", "Execution of external task {} is null. This indicates that the task was concurrently completed or deleted. It is not returned by the current fetch and lock command.", new Object[] { taskId });
    }
    
    public ProcessEngineException multipleTenantsForCamundaFormDefinitionKeyException(final String camundaFormDefinitionKey) {
        return new ProcessEngineException(this.exceptionMessage("109", "Cannot resolve a unique Camunda Form definition for key '{}' because it exists for multiple tenants.", new Object[] { camundaFormDefinitionKey }));
    }
}
