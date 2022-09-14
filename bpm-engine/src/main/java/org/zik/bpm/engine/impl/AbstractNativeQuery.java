// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineException;
import java.util.List;
import org.zik.bpm.engine.impl.context.Context;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.query.NativeQuery;

public abstract class AbstractNativeQuery<T extends NativeQuery<?, ?>, U> implements Command<Object>, NativeQuery<T, U>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient CommandExecutor commandExecutor;
    protected transient CommandContext commandContext;
    protected int maxResults;
    protected int firstResult;
    protected ResultType resultType;
    private Map<String, Object> parameters;
    private String sqlStatement;
    
    protected AbstractNativeQuery(final CommandExecutor commandExecutor) {
        this.maxResults = Integer.MAX_VALUE;
        this.firstResult = 0;
        this.parameters = new HashMap<String, Object>();
        this.commandExecutor = commandExecutor;
    }
    
    public AbstractNativeQuery(final CommandContext commandContext) {
        this.maxResults = Integer.MAX_VALUE;
        this.firstResult = 0;
        this.parameters = new HashMap<String, Object>();
        this.commandContext = commandContext;
    }
    
    public AbstractNativeQuery<T, U> setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }
    
    @Override
    public T sql(final String sqlStatement) {
        this.sqlStatement = sqlStatement;
        return (T)this;
    }
    
    @Override
    public T parameter(final String name, final Object value) {
        this.parameters.put(name, value);
        return (T)this;
    }
    
    @Override
    public U singleResult() {
        this.resultType = ResultType.SINGLE_RESULT;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<U>)this);
        }
        return this.executeSingleResult(Context.getCommandContext());
    }
    
    @Override
    public List<U> list() {
        this.resultType = ResultType.LIST;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<List<U>>)this);
        }
        return this.executeList(Context.getCommandContext(), this.getParameterMap(), 0, Integer.MAX_VALUE);
    }
    
    @Override
    public List<U> listPage(final int firstResult, final int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.resultType = ResultType.LIST_PAGE;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<List<U>>)this);
        }
        return this.executeList(Context.getCommandContext(), this.getParameterMap(), firstResult, maxResults);
    }
    
    @Override
    public long count() {
        this.resultType = ResultType.COUNT;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<Long>)this);
        }
        return this.executeCount(Context.getCommandContext(), this.getParameterMap());
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        if (this.resultType == ResultType.LIST) {
            return this.executeList(commandContext, this.getParameterMap(), 0, Integer.MAX_VALUE);
        }
        if (this.resultType == ResultType.LIST_PAGE) {
            final Map<String, Object> parameterMap = this.getParameterMap();
            parameterMap.put("resultType", "LIST_PAGE");
            parameterMap.put("firstResult", this.firstResult);
            parameterMap.put("maxResults", this.maxResults);
            parameterMap.put("internalOrderBy", "RES.ID_ asc");
            final int firstRow = this.firstResult + 1;
            parameterMap.put("firstRow", firstRow);
            int lastRow = 0;
            if (this.maxResults == Integer.MAX_VALUE) {
                lastRow = this.maxResults;
            }
            else {
                lastRow = this.firstResult + this.maxResults + 1;
            }
            parameterMap.put("lastRow", lastRow);
            return this.executeList(commandContext, parameterMap, this.firstResult, this.maxResults);
        }
        if (this.resultType == ResultType.SINGLE_RESULT) {
            return this.executeSingleResult(commandContext);
        }
        return this.executeCount(commandContext, this.getParameterMap());
    }
    
    public abstract long executeCount(final CommandContext p0, final Map<String, Object> p1);
    
    public abstract List<U> executeList(final CommandContext p0, final Map<String, Object> p1, final int p2, final int p3);
    
    public U executeSingleResult(final CommandContext commandContext) {
        final List<U> results = this.executeList(commandContext, this.getParameterMap(), 0, Integer.MAX_VALUE);
        if (results.size() == 1) {
            return results.get(0);
        }
        if (results.size() > 1) {
            throw new ProcessEngineException("Query return " + results.size() + " results instead of max 1");
        }
        return null;
    }
    
    private Map<String, Object> getParameterMap() {
        final HashMap<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("sql", this.sqlStatement);
        parameterMap.putAll(this.parameters);
        return parameterMap;
    }
    
    public Map<String, Object> getParameters() {
        return this.parameters;
    }
    
    private enum ResultType
    {
        LIST, 
        LIST_PAGE, 
        SINGLE_RESULT, 
        COUNT;
    }
}
