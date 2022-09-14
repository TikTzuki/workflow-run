// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

public class MessageEntity extends JobEntity
{
    public static final String TYPE = "message";
    private static final long serialVersionUID = 1L;
    private String repeat;
    
    public MessageEntity() {
        this.repeat = null;
    }
    
    public String getRepeat() {
        return this.repeat;
    }
    
    public void setRepeat(final String repeat) {
        this.repeat = repeat;
    }
    
    @Override
    public String getType() {
        return "message";
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[repeat=" + this.repeat + ", id=" + this.id + ", revision=" + this.revision + ", duedate=" + this.duedate + ", lockOwner=" + this.lockOwner + ", lockExpirationTime=" + this.lockExpirationTime + ", executionId=" + this.executionId + ", processInstanceId=" + this.processInstanceId + ", isExclusive=" + this.isExclusive + ", retries=" + this.retries + ", jobHandlerType=" + this.jobHandlerType + ", jobHandlerConfiguration=" + this.jobHandlerConfiguration + ", exceptionByteArray=" + this.exceptionByteArray + ", exceptionByteArrayId=" + this.exceptionByteArrayId + ", exceptionMessage=" + this.exceptionMessage + ", deploymentId=" + this.deploymentId + "]";
    }
}
