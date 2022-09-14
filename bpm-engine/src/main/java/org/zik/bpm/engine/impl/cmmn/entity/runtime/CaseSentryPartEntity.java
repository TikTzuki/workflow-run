// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnSentryPart;

public class CaseSentryPartEntity extends CmmnSentryPart implements DbEntity, HasDbRevision, HasDbReferences
{
    private static final long serialVersionUID = 1L;
    protected CaseExecutionEntity caseInstance;
    protected CaseExecutionEntity caseExecution;
    protected CaseExecutionEntity sourceCaseExecution;
    protected String id;
    protected int revision;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String sourceCaseExecutionId;
    protected String tenantId;
    private boolean forcedUpdate;
    
    public CaseSentryPartEntity() {
        this.revision = 1;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    @Override
    public CaseExecutionEntity getCaseInstance() {
        this.ensureCaseInstanceInitialized();
        return this.caseInstance;
    }
    
    protected void ensureCaseInstanceInitialized() {
        if (this.caseInstance == null && this.caseInstanceId != null) {
            this.caseInstance = this.findCaseExecutionById(this.caseInstanceId);
        }
    }
    
    @Override
    public void setCaseInstance(final CmmnExecution caseInstance) {
        this.caseInstance = (CaseExecutionEntity)caseInstance;
        if (caseInstance != null) {
            this.caseInstanceId = caseInstance.getId();
        }
        else {
            this.caseInstanceId = null;
        }
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    @Override
    public CaseExecutionEntity getCaseExecution() {
        this.ensureCaseExecutionInitialized();
        return this.caseExecution;
    }
    
    protected void ensureCaseExecutionInitialized() {
        if (this.caseExecution == null && this.caseExecutionId != null) {
            this.caseExecution = this.findCaseExecutionById(this.caseExecutionId);
        }
    }
    
    @Override
    public void setCaseExecution(final CmmnExecution caseExecution) {
        this.caseExecution = (CaseExecutionEntity)caseExecution;
        if (caseExecution != null) {
            this.caseExecutionId = caseExecution.getId();
        }
        else {
            this.caseExecutionId = null;
        }
    }
    
    @Override
    public String getSourceCaseExecutionId() {
        return this.sourceCaseExecutionId;
    }
    
    @Override
    public CmmnExecution getSourceCaseExecution() {
        this.ensureSourceCaseExecutionInitialized();
        return this.sourceCaseExecution;
    }
    
    protected void ensureSourceCaseExecutionInitialized() {
        if (this.sourceCaseExecution == null && this.sourceCaseExecutionId != null) {
            this.sourceCaseExecution = this.findCaseExecutionById(this.sourceCaseExecutionId);
        }
    }
    
    @Override
    public void setSourceCaseExecution(final CmmnExecution sourceCaseExecution) {
        this.sourceCaseExecution = (CaseExecutionEntity)sourceCaseExecution;
        if (sourceCaseExecution != null) {
            this.sourceCaseExecutionId = sourceCaseExecution.getId();
        }
        else {
            this.sourceCaseExecutionId = null;
        }
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public void forceUpdate() {
        this.forcedUpdate = true;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("satisfied", this.isSatisfied());
        if (this.forcedUpdate) {
            persistentState.put("forcedUpdate", Boolean.TRUE);
        }
        return persistentState;
    }
    
    protected CaseExecutionEntity findCaseExecutionById(final String caseExecutionId) {
        return Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(caseExecutionId);
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.caseExecutionId != null) {
            referenceIdAndClass.put(this.caseExecutionId, CaseExecutionEntity.class);
        }
        if (this.caseInstanceId != null) {
            referenceIdAndClass.put(this.caseInstanceId, CaseExecutionEntity.class);
        }
        return referenceIdAndClass;
    }
}
