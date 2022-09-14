// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.transformer;

import org.zik.bpm.engine.impl.core.model.CoreActivity;

public interface Transformer<T extends Transform<? extends CoreActivity>>
{
    T createTransform();
}
