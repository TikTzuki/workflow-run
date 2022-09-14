// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.impl.javax.el.MapELResolver;
import org.zik.bpm.engine.impl.javax.el.ListELResolver;
import org.zik.bpm.engine.impl.javax.el.ArrayELResolver;
import org.zik.bpm.engine.test.mock.MockElResolver;
import org.zik.bpm.engine.impl.javax.el.CompositeELResolver;
import org.camunda.bpm.engine.variable.context.VariableContext;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import org.zik.bpm.engine.impl.juel.ExpressionFactoryImpl;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ExpressionFactory;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import java.util.List;

public class ExpressionManager
{
    protected List<FunctionMapper> functionMappers;
    protected ExpressionFactory expressionFactory;
    protected ELContext parsingElContext;
    protected Map<Object, Object> beans;
    protected volatile ELResolver elResolver;
    
    public ExpressionManager() {
        this(null);
    }
    
    public ExpressionManager(final Map<Object, Object> beans) {
        this.functionMappers = new ArrayList<FunctionMapper>();
        this.parsingElContext = new ProcessEngineElContext(this.functionMappers);
        this.expressionFactory = new ExpressionFactoryImpl();
        this.beans = beans;
    }
    
    public Expression createExpression(final String expression) {
        final ValueExpression valueExpression = this.createValueExpression(expression);
        return new JuelExpression(valueExpression, this, expression);
    }
    
    public ValueExpression createValueExpression(final String expression) {
        return this.expressionFactory.createValueExpression(this.parsingElContext, expression, Object.class);
    }
    
    public void setExpressionFactory(final ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }
    
    public ELContext getElContext(final VariableScope variableScope) {
        ELContext elContext = null;
        if (variableScope instanceof AbstractVariableScope) {
            final AbstractVariableScope variableScopeImpl = (AbstractVariableScope)variableScope;
            elContext = variableScopeImpl.getCachedElContext();
        }
        if (elContext == null) {
            elContext = this.createElContext(variableScope);
            if (variableScope instanceof AbstractVariableScope) {
                ((AbstractVariableScope)variableScope).setCachedElContext(elContext);
            }
        }
        return elContext;
    }
    
    public ELContext createElContext(final VariableContext variableContext) {
        final ELResolver elResolver = this.getCachedElResolver();
        final ProcessEngineElContext elContext = new ProcessEngineElContext(this.functionMappers, elResolver);
        elContext.putContext(ExpressionFactory.class, this.expressionFactory);
        elContext.putContext(VariableContext.class, variableContext);
        return elContext;
    }
    
    protected ProcessEngineElContext createElContext(final VariableScope variableScope) {
        final ELResolver elResolver = this.getCachedElResolver();
        final ProcessEngineElContext elContext = new ProcessEngineElContext(this.functionMappers, elResolver);
        elContext.putContext(ExpressionFactory.class, this.expressionFactory);
        elContext.putContext(VariableScope.class, variableScope);
        return elContext;
    }
    
    protected ELResolver getCachedElResolver() {
        if (this.elResolver == null) {
            synchronized (this) {
                if (this.elResolver == null) {
                    this.elResolver = this.createElResolver();
                }
            }
        }
        return this.elResolver;
    }
    
    protected ELResolver createElResolver() {
        final CompositeELResolver elResolver = new CompositeELResolver();
        elResolver.add(new VariableScopeElResolver());
        elResolver.add(new VariableContextElResolver());
        elResolver.add(new MockElResolver());
        if (this.beans != null) {
            elResolver.add(new ReadOnlyMapELResolver(this.beans));
        }
        elResolver.add(new ProcessApplicationElResolverDelegate());
        elResolver.add(new ArrayELResolver());
        elResolver.add(new ListELResolver());
        elResolver.add(new MapELResolver());
        elResolver.add(new ProcessApplicationBeanElResolverDelegate());
        return elResolver;
    }
    
    public void addFunctionMapper(final FunctionMapper elFunctionMapper) {
        this.functionMappers.add(elFunctionMapper);
    }
}
