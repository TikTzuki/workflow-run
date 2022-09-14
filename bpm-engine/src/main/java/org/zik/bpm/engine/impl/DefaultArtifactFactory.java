// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ArtifactFactory;

public class DefaultArtifactFactory implements ArtifactFactory
{
    @Override
    public <T> T getArtifact(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (Exception e) {
            throw new ProcessEngineException("couldn't instantiate class " + clazz.getName(), e);
        }
    }
}
