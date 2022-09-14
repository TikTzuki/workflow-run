// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.handler;

import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public interface ModelElementHandler<T extends ModelElementInstance, V extends HandlerContext, E>
{
    E handleElement(final T p0, final V p1);
}
