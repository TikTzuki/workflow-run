// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test.mock;

import org.zik.bpm.engine.impl.javax.el.BeanELResolver;
import org.zik.bpm.engine.impl.javax.el.MapELResolver;
import org.zik.bpm.engine.impl.javax.el.ListELResolver;
import org.zik.bpm.engine.impl.javax.el.ArrayELResolver;
import org.zik.bpm.engine.impl.el.VariableContextElResolver;
import org.zik.bpm.engine.impl.el.VariableScopeElResolver;
import org.zik.bpm.engine.impl.javax.el.CompositeELResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.el.ExpressionManager;

public class MockExpressionManager extends ExpressionManager
{
    protected ELResolver createElResolver(final VariableScope scope) {
        return this.createElResolver();
    }
    
    @Override
    protected ELResolver createElResolver() {
        final CompositeELResolver compositeElResolver = new CompositeELResolver();
        compositeElResolver.add(new VariableScopeElResolver());
        compositeElResolver.add(new VariableContextElResolver());
        compositeElResolver.add(new MockElResolver());
        compositeElResolver.add(new ArrayELResolver());
        compositeElResolver.add(new ListELResolver());
        compositeElResolver.add(new MapELResolver());
        compositeElResolver.add(new BeanELResolver());
        return compositeElResolver;
    }
}
