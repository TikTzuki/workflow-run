// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.query.Query;

public abstract class AbstractVariableQueryImpl<T extends Query<?, ?>, U> extends AbstractQuery<T, U>
{
    private static final long serialVersionUID = 1L;
    protected List<QueryVariableValue> queryVariableValues;
    protected Boolean variableNamesIgnoreCase;
    protected Boolean variableValuesIgnoreCase;
    
    public AbstractVariableQueryImpl() {
        this.queryVariableValues = new ArrayList<QueryVariableValue>();
    }
    
    public AbstractVariableQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.queryVariableValues = new ArrayList<QueryVariableValue>();
    }
    
    @Override
    public abstract long executeCount(final CommandContext p0);
    
    @Override
    public abstract List<U> executeList(final CommandContext p0, final Page p1);
    
    public T variableValueEquals(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.EQUALS, true);
        return (T)this;
    }
    
    public T variableValueNotEquals(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.NOT_EQUALS, true);
        return (T)this;
    }
    
    public T variableValueGreaterThan(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.GREATER_THAN, true);
        return (T)this;
    }
    
    public T variableValueGreaterThanOrEqual(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.GREATER_THAN_OR_EQUAL, true);
        return (T)this;
    }
    
    public T variableValueLessThan(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.LESS_THAN, true);
        return (T)this;
    }
    
    public T variableValueLessThanOrEqual(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.LESS_THAN_OR_EQUAL, true);
        return (T)this;
    }
    
    public T variableValueLike(final String name, final String value) {
        this.addVariable(name, value, QueryOperator.LIKE, true);
        return (T)this;
    }
    
    public T variableValueNotLike(final String name, final String value) {
        this.addVariable(name, value, QueryOperator.NOT_LIKE, true);
        return (T)this;
    }
    
    public T matchVariableNamesIgnoreCase() {
        this.variableNamesIgnoreCase = true;
        for (final QueryVariableValue variable : this.getQueryVariableValues()) {
            variable.setVariableNameIgnoreCase(true);
        }
        return (T)this;
    }
    
    public T matchVariableValuesIgnoreCase() {
        this.variableValuesIgnoreCase = true;
        for (final QueryVariableValue variable : this.getQueryVariableValues()) {
            variable.setVariableValueIgnoreCase(true);
        }
        return (T)this;
    }
    
    protected void addVariable(final String name, final Object value, final QueryOperator operator, final boolean processInstanceScope) {
        final QueryVariableValue queryVariableValue = this.createQueryVariableValue(name, value, operator, processInstanceScope);
        this.getQueryVariableValues().add(queryVariableValue);
    }
    
    protected QueryVariableValue createQueryVariableValue(final String name, final Object value, final QueryOperator operator, final boolean processInstanceScope) {
        this.validateVariable(name, value, operator);
        final boolean shouldMatchVariableValuesIgnoreCase = Boolean.TRUE.equals(this.variableValuesIgnoreCase) && value != null && String.class.isAssignableFrom(value.getClass());
        final boolean shouldMatchVariableNamesIgnoreCase = Boolean.TRUE.equals(this.variableNamesIgnoreCase);
        return new QueryVariableValue(name, value, operator, processInstanceScope, shouldMatchVariableNamesIgnoreCase, shouldMatchVariableValuesIgnoreCase);
    }
    
    protected void validateVariable(final String name, final Object value, final QueryOperator operator) {
        EnsureUtil.ensureNotNull(NotValidException.class, "name", (Object)name);
        if (value == null || this.isBoolean(value)) {
            switch (operator) {
                case GREATER_THAN: {
                    throw new NotValidException("Booleans and null cannot be used in 'greater than' condition");
                }
                case LESS_THAN: {
                    throw new NotValidException("Booleans and null cannot be used in 'less than' condition");
                }
                case GREATER_THAN_OR_EQUAL: {
                    throw new NotValidException("Booleans and null cannot be used in 'greater than or equal' condition");
                }
                case LESS_THAN_OR_EQUAL: {
                    throw new NotValidException("Booleans and null cannot be used in 'less than or equal' condition");
                }
                case LIKE: {
                    throw new NotValidException("Booleans and null cannot be used in 'like' condition");
                }
                case NOT_LIKE: {
                    throw new NotValidException("Booleans and null cannot be used in 'not like' condition");
                }
            }
        }
    }
    
    private boolean isBoolean(final Object value) {
        return value != null && (Boolean.class.isAssignableFrom(value.getClass()) || Boolean.TYPE.isAssignableFrom(value.getClass()));
    }
    
    protected void ensureVariablesInitialized() {
        if (!this.getQueryVariableValues().isEmpty()) {
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
            final String dbType = processEngineConfiguration.getDatabaseType();
            for (final QueryVariableValue queryVariableValue : this.getQueryVariableValues()) {
                queryVariableValue.initialize(variableSerializers, dbType);
            }
        }
    }
    
    public List<QueryVariableValue> getQueryVariableValues() {
        return this.queryVariableValues;
    }
    
    public Boolean isVariableNamesIgnoreCase() {
        return this.variableNamesIgnoreCase;
    }
    
    public Boolean isVariableValuesIgnoreCase() {
        return this.variableValuesIgnoreCase;
    }
}
