// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity.util;

import org.camunda.bpm.engine.variable.value.TypedValue;

public interface TypedValueUpdateListener
{
    void onImplicitValueUpdate(final TypedValue p0);
}
