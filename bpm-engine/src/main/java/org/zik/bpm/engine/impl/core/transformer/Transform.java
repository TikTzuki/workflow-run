// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.transformer;

import java.util.List;
import org.zik.bpm.engine.impl.core.model.CoreActivity;

public interface Transform<T extends CoreActivity>
{
    List<T> transform();
}
