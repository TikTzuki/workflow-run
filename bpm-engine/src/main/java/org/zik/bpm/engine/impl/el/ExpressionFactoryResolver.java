// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.impl.juel.ExpressionFactoryImpl;
import org.zik.bpm.engine.impl.javax.el.ExpressionFactory;

public abstract class ExpressionFactoryResolver
{
    public static ExpressionFactory resolveExpressionFactory() {
        return new ExpressionFactoryImpl();
    }
}
