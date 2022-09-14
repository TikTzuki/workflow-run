// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

public class HistoricVariableUpdateEventEntity extends HistoricDetailEventEntity
{
    private static final long serialVersionUID = 1L;
    protected int revision;
    protected String variableName;
    protected String variableInstanceId;
    protected String scopeActivityInstanceId;
    protected String serializerName;
    protected Long longValue;
    protected Double doubleValue;
    protected String textValue;
    protected String textValue2;
    protected byte[] byteValue;
    protected String byteArrayId;
    protected Boolean isInitial;
    
    public HistoricVariableUpdateEventEntity() {
        this.isInitial = false;
    }
    
    public String getSerializerName() {
        return this.serializerName;
    }
    
    public void setSerializerName(final String serializerName) {
        this.serializerName = serializerName;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
    
    public Long getLongValue() {
        return this.longValue;
    }
    
    public void setLongValue(final Long longValue) {
        this.longValue = longValue;
    }
    
    public Double getDoubleValue() {
        return this.doubleValue;
    }
    
    public void setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    
    public String getTextValue() {
        return this.textValue;
    }
    
    public void setTextValue(final String textValue) {
        this.textValue = textValue;
    }
    
    public String getTextValue2() {
        return this.textValue2;
    }
    
    public void setTextValue2(final String textValue2) {
        this.textValue2 = textValue2;
    }
    
    public byte[] getByteValue() {
        return this.byteValue;
    }
    
    public void setByteValue(final byte[] byteValue) {
        this.byteValue = byteValue;
    }
    
    public int getRevision() {
        return this.revision;
    }
    
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public void setByteArrayId(final String id) {
        this.byteArrayId = id;
    }
    
    public String getByteArrayId() {
        return this.byteArrayId;
    }
    
    public String getVariableInstanceId() {
        return this.variableInstanceId;
    }
    
    public void setVariableInstanceId(final String variableInstanceId) {
        this.variableInstanceId = variableInstanceId;
    }
    
    public String getScopeActivityInstanceId() {
        return this.scopeActivityInstanceId;
    }
    
    public void setScopeActivityInstanceId(final String scopeActivityInstanceId) {
        this.scopeActivityInstanceId = scopeActivityInstanceId;
    }
    
    public void setInitial(final Boolean isInitial) {
        this.isInitial = isInitial;
    }
    
    public Boolean isInitial() {
        return this.isInitial;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[variableName=" + this.variableName + ", variableInstanceId=" + this.variableInstanceId + ", revision=" + this.revision + ", serializerName=" + this.serializerName + ", longValue=" + this.longValue + ", doubleValue=" + this.doubleValue + ", textValue=" + this.textValue + ", textValue2=" + this.textValue2 + ", byteArrayId=" + this.byteArrayId + ", activityInstanceId=" + this.activityInstanceId + ", scopeActivityInstanceId=" + this.scopeActivityInstanceId + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", id=" + this.id + ", processDefinitionId=" + this.processInstanceId + ", processInstanceId=" + this.processInstanceId + ", taskId=" + this.taskId + ", timestamp=" + this.timestamp + ", tenantId=" + this.tenantId + ", isInitial=" + this.isInitial + "]";
    }
}
