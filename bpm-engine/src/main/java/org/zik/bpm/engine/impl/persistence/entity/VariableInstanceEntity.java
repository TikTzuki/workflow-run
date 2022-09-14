// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.application.InvocationContext;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.db.*;
import org.zik.bpm.engine.impl.persistence.entity.util.ByteArrayField;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueField;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueUpdateListener;
import org.zik.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.runtime.VariableInstance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

class HIHI implements CoreVariableInstance{

    @Override
    public String getName() {
        return null;
    }

    @Override
    public TypedValue getTypedValue(boolean p0) {
        return null;
    }

    @Override
    public void setValue(TypedValue p0) {

    }
}
public class VariableInstanceEntity extends HIHI implements VariableInstance, ValueFields, DbEntity, DbEntityLifecycleAware, TypedValueUpdateListener, HasDbRevision, HasDbReferences, Serializable {
    protected static final EnginePersistenceLogger LOG;
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String name;
    protected String processDefinitionId;
    protected String processInstanceId;
    protected String executionId;
    protected String taskId;
    protected String batchId;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String activityInstanceId;
    protected String tenantId;
    protected Long longValue;
    protected Double doubleValue;
    protected String textValue;
    protected String textValue2;
    protected String variableScopeId;
    protected ByteArrayField byteArrayField;
    protected TypedValueField typedValueField;
    boolean forcedUpdate;
    protected String configuration;
    protected long sequenceCounter;
    protected boolean isConcurrentLocal;
    protected boolean isTransient;
    protected ExecutionEntity execution;

    public VariableInstanceEntity() {
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.RUNTIME);
        this.typedValueField = new TypedValueField(this, true);
        this.sequenceCounter = 1L;
        this.isConcurrentLocal = false;
        this.isTransient = false;
        this.typedValueField.addImplicitUpdateListener(this);
    }

    public VariableInstanceEntity(final String name, final TypedValue value, final boolean isTransient) {
        this();
        this.name = name;
        this.isTransient = isTransient;
        this.typedValueField.setValue(value);
    }

    public static VariableInstanceEntity createAndInsert(final String name, final TypedValue value) {
        final VariableInstanceEntity variableInstance = create(name, value, value.isTransient());
        insert(variableInstance);
        return variableInstance;
    }

    public static void insert(final VariableInstanceEntity variableInstance) {
        if (!variableInstance.isTransient()) {
            Context.getCommandContext().getDbEntityManager().insert(variableInstance);
        }
    }

    public static VariableInstanceEntity create(final String name, final TypedValue value, final boolean isTransient) {
        return new VariableInstanceEntity(name, value, isTransient);
    }

    public void delete() {
        if (!this.isTransient()) {
            this.typedValueField.notifyImplicitValueUpdate();
        }
        this.clearValueFields();
        if (!this.isTransient) {
            Context.getCommandContext().getDbEntityManager().delete(this);
        }
    }

    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        if (this.typedValueField.getSerializerName() != null) {
            persistentState.put("serializerName", this.typedValueField.getSerializerName());
        }
        if (this.longValue != null) {
            persistentState.put("longValue", this.longValue);
        }
        if (this.doubleValue != null) {
            persistentState.put("doubleValue", this.doubleValue);
        }
        if (this.textValue != null) {
            persistentState.put("textValue", this.textValue);
        }
        if (this.textValue2 != null) {
            persistentState.put("textValue2", this.textValue2);
        }
        if (this.byteArrayField.getByteArrayId() != null) {
            persistentState.put("byteArrayValueId", this.byteArrayField.getByteArrayId());
        }
        persistentState.put("sequenceCounter", this.getSequenceCounter());
        persistentState.put("concurrentLocal", this.isConcurrentLocal);
        persistentState.put("executionId", this.executionId);
        persistentState.put("taskId", this.taskId);
        persistentState.put("caseExecutionId", this.caseExecutionId);
        persistentState.put("caseInstanceId", this.caseInstanceId);
        persistentState.put("tenantId", this.tenantId);
        persistentState.put("processInstanceId", this.processInstanceId);
        persistentState.put("processDefinitionId", this.processDefinitionId);
        return persistentState;
    }

    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }

    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }

    public void setCaseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    public void setCaseExecutionId(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }

    public void setCaseExecution(final CaseExecutionEntity caseExecution) {
        if (caseExecution != null) {
            this.caseInstanceId = caseExecution.getCaseInstanceId();
            this.caseExecutionId = caseExecution.getId();
            this.tenantId = caseExecution.getTenantId();
        } else {
            this.caseInstanceId = null;
            this.caseExecutionId = null;
            this.tenantId = null;
        }
    }

    public String getByteArrayValueId() {
        return this.byteArrayField.getByteArrayId();
    }

    public void setByteArrayValueId(final String byteArrayValueId) {
        this.byteArrayField.setByteArrayId(byteArrayValueId);
    }

    @Override
    public byte[] getByteArrayValue() {
        return this.byteArrayField.getByteArrayValue();
    }

    @Override
    public void setByteArrayValue(final byte[] bytes) {
        this.byteArrayField.setByteArrayValue(bytes, this.isTransient);
    }

    protected void deleteByteArrayValue() {
        this.byteArrayField.deleteByteArrayValue();
    }

    @Override
    public Object getValue() {
        return this.typedValueField.getValue();
    }

    @Override
    public TypedValue getTypedValue() {
        return this.typedValueField.getTypedValue(this.isTransient);
    }

    @Override
    public TypedValue getTypedValue(final boolean deserializeValue) {
        return this.typedValueField.getTypedValue(deserializeValue, this.isTransient);
    }

    @Override
    public void setValue(final TypedValue value) {
        this.clearValueFields();
        this.typedValueField.setValue(value);
    }

    public void clearValueFields() {
        this.longValue = null;
        this.doubleValue = null;
        this.textValue = null;
        this.textValue2 = null;
        this.typedValueField.clear();
        if (this.byteArrayField.getByteArrayId() != null) {
            this.deleteByteArrayValue();
            this.setByteArrayValueId(null);
        }
    }

    @Override
    public String getTypeName() {
        return this.typedValueField.getTypeName();
    }

    @Override
    public void postLoad() {
        this.typedValueField.postLoad();
    }

    protected void ensureExecutionInitialized() {
        if (this.execution == null && this.executionId != null) {
            this.execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
        }
    }

    public ExecutionEntity getExecution() {
        this.ensureExecutionInitialized();
        return this.execution;
    }

    public void setExecution(final ExecutionEntity execution) {
        this.execution = execution;
        if (execution == null) {
            this.executionId = null;
            this.processInstanceId = null;
            this.processDefinitionId = null;
            this.tenantId = null;
        } else {
            this.setExecutionId(execution.getId());
            this.processDefinitionId = execution.getProcessDefinitionId();
            this.processInstanceId = execution.getProcessInstanceId();
            this.tenantId = execution.getTenantId();
        }
    }

    public CaseExecutionEntity getCaseExecution() {
        if (this.caseExecutionId != null) {
            return Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        }
        return null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getTextValue() {
        return this.textValue;
    }

    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }

    @Override
    public String getExecutionId() {
        return this.executionId;
    }

    @Override
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }

    @Override
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }

    @Override
    public Long getLongValue() {
        return this.longValue;
    }

    @Override
    public void setLongValue(final Long longValue) {
        this.longValue = longValue;
    }

    @Override
    public Double getDoubleValue() {
        return this.doubleValue;
    }

    @Override
    public void setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setTextValue(final String textValue) {
        this.textValue = textValue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRevision() {
        return this.revision;
    }

    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }

    public void setSerializer(final TypedValueSerializer<?> serializer) {
        this.typedValueField.setSerializerName(serializer.getName());
    }

    public void setSerializerName(final String type) {
        this.typedValueField.setSerializerName(type);
    }

    public TypedValueSerializer<?> getSerializer() {
        return this.typedValueField.getSerializer();
    }

    @Override
    public String getTextValue2() {
        return this.textValue2;
    }

    @Override
    public void setTextValue2(final String textValue2) {
        this.textValue2 = textValue2;
    }

    @Override
    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getBatchId() {
        return this.batchId;
    }

    public void setBatchId(final String batchId) {
        this.batchId = batchId;
    }

    public void setTask(final TaskEntity task) {
        if (task != null) {
            this.taskId = task.getId();
            this.tenantId = task.getTenantId();
            if (task.getExecution() != null) {
                this.setExecution(task.getExecution());
            }
            if (task.getCaseExecution() != null) {
                this.setCaseExecution(task.getCaseExecution());
            }
        } else {
            this.taskId = null;
            this.tenantId = null;
            this.setExecution(null);
            this.setCaseExecution(null);
        }
    }

    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }

    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    public String getSerializerName() {
        return this.typedValueField.getSerializerName();
    }

    @Override
    public String getErrorMessage() {
        return this.typedValueField.getErrorMessage();
    }

    public String getVariableScopeId() {
        if (this.variableScopeId != null) {
            return this.variableScopeId;
        }
        if (this.taskId != null) {
            return this.taskId;
        }
        if (this.executionId != null) {
            return this.executionId;
        }
        return this.caseExecutionId;
    }

    public void setVariableScopeId(final String variableScopeId) {
        this.variableScopeId = variableScopeId;
    }

    protected VariableScope getVariableScope() {
        if (this.taskId != null) {
            return this.getTask();
        }
        if (this.executionId != null) {
            return this.getExecution();
        }
        if (this.caseExecutionId != null) {
            return this.getCaseExecution();
        }
        return null;
    }

    protected TaskEntity getTask() {
        if (this.taskId != null) {
            return Context.getCommandContext().getTaskManager().findTaskById(this.taskId);
        }
        return null;
    }

    public long getSequenceCounter() {
        return this.sequenceCounter;
    }

    public void setSequenceCounter(final long sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
    }

    public void incrementSequenceCounter() {
        ++this.sequenceCounter;
    }

    public boolean isConcurrentLocal() {
        return this.isConcurrentLocal;
    }

    public void setConcurrentLocal(final boolean isConcurrentLocal) {
        this.isConcurrentLocal = isConcurrentLocal;
    }

    @Override
    public void onImplicitValueUpdate(final TypedValue updatedValue) {
        final ProcessApplicationReference targetProcessApplication = this.getContextProcessApplication();
        if (targetProcessApplication != null) {
            Context.executeWithinProcessApplication(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    VariableInstanceEntity.this.getVariableScope().setVariableLocal(VariableInstanceEntity.this.name, updatedValue);
                    return null;
                }
            }, targetProcessApplication, new InvocationContext(this.getExecution()));
        } else if (!this.isTransient) {
            this.getVariableScope().setVariableLocal(this.name, updatedValue);
        }
    }

    protected ProcessApplicationReference getContextProcessApplication() {
        if (this.taskId != null) {
            return ProcessApplicationContextUtil.getTargetProcessApplication(this.getTask());
        }
        if (this.executionId != null) {
            return ProcessApplicationContextUtil.getTargetProcessApplication(this.getExecution());
        }
        if (this.caseExecutionId != null) {
            return ProcessApplicationContextUtil.getTargetProcessApplication(this.getCaseExecution());
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", name=" + this.name + ", processDefinitionId=" + this.processDefinitionId + ", processInstanceId=" + this.processInstanceId + ", executionId=" + this.executionId + ", caseInstanceId=" + this.caseInstanceId + ", caseExecutionId=" + this.caseExecutionId + ", taskId=" + this.taskId + ", activityInstanceId=" + this.activityInstanceId + ", tenantId=" + this.tenantId + ", longValue=" + this.longValue + ", doubleValue=" + this.doubleValue + ", textValue=" + this.textValue + ", textValue2=" + this.textValue2 + ", byteArrayValueId=" + this.getByteArrayValueId() + ", configuration=" + this.configuration + ", isConcurrentLocal=" + this.isConcurrentLocal + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final VariableInstanceEntity other = (VariableInstanceEntity) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public void setTransient(final boolean isTransient) {
        this.isTransient = isTransient;
    }

    public boolean isTransient() {
        return this.isTransient;
    }

    @Override
    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }

    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.processInstanceId != null) {
            referenceIdAndClass.put(this.processInstanceId, ExecutionEntity.class);
        }
        if (this.executionId != null) {
            referenceIdAndClass.put(this.executionId, ExecutionEntity.class);
        }
        if (this.caseInstanceId != null) {
            referenceIdAndClass.put(this.caseInstanceId, CaseExecutionEntity.class);
        }
        if (this.caseExecutionId != null) {
            referenceIdAndClass.put(this.caseExecutionId, CaseExecutionEntity.class);
        }
        if (this.getByteArrayValueId() != null) {
            referenceIdAndClass.put(this.getByteArrayValueId(), ByteArrayEntity.class);
        }
        return referenceIdAndClass;
    }

    public boolean wasCreatedBefore713() {
        return this.getProcessDefinitionId() == null;
    }

    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
