// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

public class DefaultCmmnTransformFactory implements CmmnTransformFactory
{
    @Override
    public CmmnTransform createTransform(final CmmnTransformer cmmnTransformer) {
        return new CmmnTransform(cmmnTransformer);
    }
}
