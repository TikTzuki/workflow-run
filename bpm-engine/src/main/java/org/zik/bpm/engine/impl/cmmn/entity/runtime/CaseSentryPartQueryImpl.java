// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.AbstractQuery;

public class CaseSentryPartQueryImpl extends AbstractQuery<CaseSentryPartQueryImpl, CaseSentryPartEntity>
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String sentryId;
    protected String type;
    protected String sourceCaseExecutionId;
    protected String standardEvent;
    protected String variableEvent;
    protected String variableName;
    protected boolean satisfied;
    
    public CaseSentryPartQueryImpl() {
    }
    
    public CaseSentryPartQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    public CaseSentryPartQueryImpl caseSentryPartId(final String caseSentryPartId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseSentryPartId", (Object)caseSentryPartId);
        this.id = caseSentryPartId;
        return this;
    }
    
    public CaseSentryPartQueryImpl caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    public CaseSentryPartQueryImpl caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseExecutionId", (Object)caseExecutionId);
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    public CaseSentryPartQueryImpl sentryId(final String sentryId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "sentryId", (Object)sentryId);
        this.sentryId = sentryId;
        return this;
    }
    
    public CaseSentryPartQueryImpl type(final String type) {
        EnsureUtil.ensureNotNull(NotValidException.class, "type", (Object)type);
        this.type = type;
        return this;
    }
    
    public CaseSentryPartQueryImpl sourceCaseExecutionId(final String sourceCaseExecutionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "sourceCaseExecutionId", (Object)sourceCaseExecutionId);
        this.sourceCaseExecutionId = sourceCaseExecutionId;
        return this;
    }
    
    public CaseSentryPartQueryImpl standardEvent(final String standardEvent) {
        EnsureUtil.ensureNotNull(NotValidException.class, "standardEvent", (Object)standardEvent);
        this.standardEvent = standardEvent;
        return this;
    }
    
    public CaseSentryPartQueryImpl variableEvent(final String variableEvent) {
        EnsureUtil.ensureNotNull(NotValidException.class, "variableEvent", (Object)variableEvent);
        this.variableEvent = variableEvent;
        return this;
    }
    
    public CaseSentryPartQueryImpl variableName(final String variableName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "variableName", (Object)variableName);
        this.variableName = variableName;
        return this;
    }
    
    public CaseSentryPartQueryImpl satisfied() {
        this.satisfied = true;
        return this;
    }
    
    public CaseSentryPartQueryImpl orderByCaseSentryId() {
        this.orderBy(CaseSentryPartQueryProperty.CASE_SENTRY_PART_ID);
        return this;
    }
    
    public CaseSentryPartQueryImpl orderByCaseInstanceId() {
        this.orderBy(CaseSentryPartQueryProperty.CASE_INSTANCE_ID);
        return this;
    }
    
    public CaseSentryPartQueryImpl orderByCaseExecutionId() {
        this.orderBy(CaseSentryPartQueryProperty.CASE_EXECUTION_ID);
        return this;
    }
    
    public CaseSentryPartQueryImpl orderBySentryId() {
        this.orderBy(CaseSentryPartQueryProperty.SENTRY_ID);
        return this;
    }
    
    public CaseSentryPartQueryImpl orderBySource() {
        this.orderBy(CaseSentryPartQueryProperty.SOURCE);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getCaseSentryPartManager().findCaseSentryPartCountByQueryCriteria(this);
    }
    
    @Override
    public List<CaseSentryPartEntity> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final List<CaseSentryPartEntity> result = commandContext.getCaseSentryPartManager().findCaseSentryPartByQueryCriteria(this, page);
        return result;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public String getSentryId() {
        return this.sentryId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getSourceCaseExecutionId() {
        return this.sourceCaseExecutionId;
    }
    
    public String getStandardEvent() {
        return this.standardEvent;
    }
    
    public String getVariableEvent() {
        return this.variableEvent;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public boolean isSatisfied() {
        return this.satisfied;
    }
}
