// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.core.handler.HandlerContext;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.zik.bpm.engine.impl.core.handler.ModelElementHandler;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public abstract class CmmnElementHandler<T extends CmmnElement, E> implements ModelElementHandler<T, CmmnHandlerContext, E>
{
    @Override
    public abstract E handleElement(final T p0, final CmmnHandlerContext p1);
}
