// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.util.QueryMaxResultsLimitUtil;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.joda.time.DateTime;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.query.QueryProperty;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.query.Query;

public abstract class AbstractQuery<T extends Query<?, ?>, U> extends ListQueryParameterObject implements Command<Object>, Query<T, U>, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String SORTORDER_ASC = "asc";
    public static final String SORTORDER_DESC = "desc";
    protected transient CommandExecutor commandExecutor;
    protected ResultType resultType;
    protected Map<String, String> expressions;
    protected Set<Validator<AbstractQuery<?, ?>>> validators;
    protected boolean maxResultsLimitEnabled;
    
    protected AbstractQuery() {
        this.expressions = new HashMap<String, String>();
        this.validators = new HashSet<Validator<AbstractQuery<?, ?>>>();
    }
    
    protected AbstractQuery(final CommandExecutor commandExecutor) {
        this.expressions = new HashMap<String, String>();
        this.validators = new HashSet<Validator<AbstractQuery<?, ?>>>();
        this.commandExecutor = commandExecutor;
        this.addValidator((Validator<AbstractQuery<?, ?>>)QueryValidators.AdhocQueryValidator.get());
    }
    
    public AbstractQuery<T, U> setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }
    
    public T orderBy(final QueryProperty property) {
        return this.orderBy(new QueryOrderingProperty(null, property));
    }
    
    public T orderBy(final QueryOrderingProperty orderProperty) {
        this.orderingProperties.add(orderProperty);
        return (T)this;
    }
    
    @Override
    public T asc() {
        return this.direction(Direction.ASCENDING);
    }
    
    @Override
    public T desc() {
        return this.direction(Direction.DESCENDING);
    }
    
    public T direction(final Direction direction) {
        QueryOrderingProperty currentOrderingProperty = null;
        if (!this.orderingProperties.isEmpty()) {
            currentOrderingProperty = this.orderingProperties.get(this.orderingProperties.size() - 1);
        }
        EnsureUtil.ensureNotNull(NotValidException.class, "You should call any of the orderBy methods first before specifying a direction", "currentOrderingProperty", currentOrderingProperty);
        if (currentOrderingProperty.getDirection() != null) {
            EnsureUtil.ensureNull(NotValidException.class, "Invalid query: can specify only one direction desc() or asc() for an ordering constraint", "direction", direction);
        }
        currentOrderingProperty.setDirection(direction);
        return (T)this;
    }
    
    protected void checkQueryOk() {
        for (final QueryOrderingProperty orderingProperty : this.orderingProperties) {
            EnsureUtil.ensureNotNull(NotValidException.class, "Invalid query: call asc() or desc() after using orderByXX()", "direction", orderingProperty.getDirection());
        }
    }
    
    @Override
    public U singleResult() {
        this.resultType = ResultType.SINGLE_RESULT;
        return (U)this.executeResult(this.resultType);
    }
    
    @Override
    public List<U> list() {
        this.resultType = ResultType.LIST;
        return (List<U>)this.executeResult(this.resultType);
    }
    
    @Override
    public List<U> listPage(final int firstResult, final int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.resultType = ResultType.LIST_PAGE;
        return (List<U>)this.executeResult(this.resultType);
    }
    
    public Object executeResult(final ResultType resultType) {
        if (this.commandExecutor != null) {
            if (!this.maxResultsLimitEnabled) {
                this.maxResultsLimitEnabled = (Context.getCommandContext() == null);
            }
            return this.commandExecutor.execute((Command<Object>)this);
        }
        switch (resultType) {
            case SINGLE_RESULT: {
                return this.executeSingleResult(Context.getCommandContext());
            }
            case LIST_PAGE:
            case LIST: {
                return this.evaluateExpressionsAndExecuteList(Context.getCommandContext(), null);
            }
            default: {
                throw new ProcessEngineException("Unknown result type!");
            }
        }
    }
    
    @Override
    public long count() {
        this.resultType = ResultType.COUNT;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<Long>)this);
        }
        return this.evaluateExpressionsAndExecuteCount(Context.getCommandContext());
    }
    
    @Override
    public List<U> unlimitedList() {
        this.resultType = ResultType.LIST;
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute((Command<List<U>>)this);
        }
        return this.evaluateExpressionsAndExecuteList(Context.getCommandContext(), null);
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        if (this.resultType == ResultType.LIST) {
            return this.evaluateExpressionsAndExecuteList(commandContext, null);
        }
        if (this.resultType == ResultType.SINGLE_RESULT) {
            return this.executeSingleResult(commandContext);
        }
        if (this.resultType == ResultType.LIST_PAGE) {
            return this.evaluateExpressionsAndExecuteList(commandContext, null);
        }
        if (this.resultType == ResultType.LIST_IDS) {
            return this.evaluateExpressionsAndExecuteIdsList(commandContext);
        }
        if (this.resultType == ResultType.LIST_DEPLOYMENT_ID_MAPPINGS) {
            return this.evaluateExpressionsAndExecuteDeploymentIdMappingsList(commandContext);
        }
        return this.evaluateExpressionsAndExecuteCount(commandContext);
    }
    
    public long evaluateExpressionsAndExecuteCount(final CommandContext commandContext) {
        this.validate();
        this.evaluateExpressions();
        return this.hasExcludingConditions() ? 0L : this.executeCount(commandContext);
    }
    
    public abstract long executeCount(final CommandContext p0);
    
    public List<U> evaluateExpressionsAndExecuteList(final CommandContext commandContext, final Page page) {
        this.checkMaxResultsLimit();
        this.validate();
        this.evaluateExpressions();
        return this.hasExcludingConditions() ? new ArrayList<U>() : this.executeList(commandContext, page);
    }
    
    protected boolean hasExcludingConditions() {
        return false;
    }
    
    public abstract List<U> executeList(final CommandContext p0, final Page p1);
    
    public U executeSingleResult(final CommandContext commandContext) {
        this.disableMaxResultsLimit();
        final List<U> results = this.evaluateExpressionsAndExecuteList(commandContext, new Page(0, 2));
        if (results.size() == 1) {
            return results.get(0);
        }
        if (results.size() > 1) {
            throw new ProcessEngineException("Query return " + results.size() + " results instead of max 1");
        }
        return null;
    }
    
    public Map<String, String> getExpressions() {
        return this.expressions;
    }
    
    public void setExpressions(final Map<String, String> expressions) {
        this.expressions = expressions;
    }
    
    public void addExpression(final String key, final String expression) {
        this.expressions.put(key, expression);
    }
    
    protected void evaluateExpressions() {
        final ArrayList<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(this.expressions.entrySet());
        for (final Map.Entry<String, String> entry : entries) {
            final String methodName = entry.getKey();
            final String expression = entry.getValue();
            Object value;
            try {
                value = Context.getProcessEngineConfiguration().getExpressionManager().createExpression(expression).getValue(null);
            }
            catch (ProcessEngineException e) {
                throw new ProcessEngineException("Unable to resolve expression '" + expression + "' for method '" + methodName + "' on class '" + this.getClass().getCanonicalName() + "'", e);
            }
            if (value instanceof DateTime) {
                value = ((DateTime)value).toDate();
            }
            try {
                final Method method = this.getMethod(methodName);
                method.invoke(this, value);
            }
            catch (InvocationTargetException e2) {
                throw new ProcessEngineException("Unable to invoke method '" + methodName + "' on class '" + this.getClass().getCanonicalName() + "'", e2);
            }
            catch (IllegalAccessException e3) {
                throw new ProcessEngineException("Unable to access method '" + methodName + "' on class '" + this.getClass().getCanonicalName() + "'", e3);
            }
        }
    }
    
    protected Method getMethod(final String methodName) {
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new ProcessEngineException("Unable to find method '" + methodName + "' on class '" + this.getClass().getCanonicalName() + "'");
    }
    
    public T extend(final T extendingQuery) {
        throw new ProcessEngineException("Extending of query type '" + extendingQuery.getClass().getCanonicalName() + "' currently not supported");
    }
    
    protected void mergeOrdering(final AbstractQuery<?, ?> extendedQuery, final AbstractQuery<?, ?> extendingQuery) {
        extendedQuery.orderingProperties = this.orderingProperties;
        if (extendingQuery.orderingProperties != null) {
            if (extendedQuery.orderingProperties == null) {
                extendedQuery.orderingProperties = extendingQuery.orderingProperties;
            }
            else {
                extendedQuery.orderingProperties.addAll(extendingQuery.orderingProperties);
            }
        }
    }
    
    protected void mergeExpressions(final AbstractQuery<?, ?> extendedQuery, final AbstractQuery<?, ?> extendingQuery) {
        final Map<String, String> mergedExpressions = new HashMap<String, String>(extendingQuery.getExpressions());
        for (final Map.Entry<String, String> entry : this.getExpressions().entrySet()) {
            if (!mergedExpressions.containsKey(entry.getKey())) {
                mergedExpressions.put(entry.getKey(), entry.getValue());
            }
        }
        extendedQuery.setExpressions(mergedExpressions);
    }
    
    public void validate() {
        for (final Validator<AbstractQuery<?, ?>> validator : this.validators) {
            this.validate(validator);
        }
    }
    
    public void validate(final Validator<AbstractQuery<?, ?>> validator) {
        validator.validate(this);
    }
    
    public void addValidator(final Validator<AbstractQuery<?, ?>> validator) {
        this.validators.add(validator);
    }
    
    public void removeValidator(final Validator<AbstractQuery<?, ?>> validator) {
        this.validators.remove(validator);
    }
    
    public List<String> listIds() {
        this.resultType = ResultType.LIST_IDS;
        List<String> ids = null;
        if (this.commandExecutor != null) {
            ids = this.commandExecutor.execute((Command<List<String>>)this);
        }
        else {
            ids = this.evaluateExpressionsAndExecuteIdsList(Context.getCommandContext());
        }
        if (ids != null) {
            QueryMaxResultsLimitUtil.checkMaxResultsLimit(ids.size());
        }
        return ids;
    }
    
    public List<ImmutablePair<String, String>> listDeploymentIdMappings() {
        this.resultType = ResultType.LIST_DEPLOYMENT_ID_MAPPINGS;
        List<ImmutablePair<String, String>> ids = null;
        if (this.commandExecutor != null) {
            ids = this.commandExecutor.execute((Command<List<ImmutablePair<String, String>>>)this);
        }
        else {
            ids = this.evaluateExpressionsAndExecuteDeploymentIdMappingsList(Context.getCommandContext());
        }
        if (ids != null) {
            QueryMaxResultsLimitUtil.checkMaxResultsLimit(ids.size());
        }
        return ids;
    }
    
    public List<String> evaluateExpressionsAndExecuteIdsList(final CommandContext commandContext) {
        this.validate();
        this.evaluateExpressions();
        return this.hasExcludingConditions() ? new ArrayList<String>() : this.executeIdsList(commandContext);
    }
    
    public List<String> executeIdsList(final CommandContext commandContext) {
        throw new UnsupportedOperationException("executeIdsList not supported by " + this.getClass().getCanonicalName());
    }
    
    public List<ImmutablePair<String, String>> evaluateExpressionsAndExecuteDeploymentIdMappingsList(final CommandContext commandContext) {
        this.validate();
        this.evaluateExpressions();
        return this.hasExcludingConditions() ? new ArrayList<ImmutablePair<String, String>>() : this.executeDeploymentIdMappingsList(commandContext);
    }
    
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        throw new UnsupportedOperationException("executeDeploymentIdMappingsList not supported by " + this.getClass().getCanonicalName());
    }
    
    protected void checkMaxResultsLimit() {
        if (this.maxResultsLimitEnabled) {
            QueryMaxResultsLimitUtil.checkMaxResultsLimit(this.maxResults);
        }
    }
    
    public void enableMaxResultsLimit() {
        this.maxResultsLimitEnabled = true;
    }
    
    public void disableMaxResultsLimit() {
        this.maxResultsLimitEnabled = false;
    }
    
    protected enum ResultType
    {
        LIST, 
        LIST_PAGE, 
        LIST_IDS, 
        LIST_DEPLOYMENT_ID_MAPPINGS, 
        SINGLE_RESULT, 
        COUNT;
    }
}
