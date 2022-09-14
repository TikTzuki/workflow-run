// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.io.Serializable;

public interface TreeBuilder extends Serializable
{
    Tree build(final String p0) throws TreeBuilderException;
}
