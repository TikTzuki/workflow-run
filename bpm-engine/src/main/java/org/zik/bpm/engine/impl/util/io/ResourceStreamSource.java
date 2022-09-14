// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.io;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.io.InputStream;

public class ResourceStreamSource implements StreamSource
{
    String resource;
    ClassLoader classLoader;
    
    public ResourceStreamSource(final String resource) {
        this.resource = resource;
    }
    
    public ResourceStreamSource(final String resource, final ClassLoader classLoader) {
        this.resource = resource;
        this.classLoader = classLoader;
    }
    
    @Override
    public InputStream getInputStream() {
        InputStream inputStream = null;
        if (this.classLoader == null) {
            inputStream = ReflectUtil.getResourceAsStream(this.resource);
        }
        else {
            this.classLoader.getResourceAsStream(this.resource);
        }
        EnsureUtil.ensureNotNull("resource '" + this.resource + "' doesn't exist", "inputStream", inputStream);
        return inputStream;
    }
    
    @Override
    public String toString() {
        return "Resource[" + this.resource + "]";
    }
}
