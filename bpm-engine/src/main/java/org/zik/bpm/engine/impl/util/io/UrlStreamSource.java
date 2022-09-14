// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.io;

import java.io.IOException;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.InputStream;
import java.net.URL;

public class UrlStreamSource implements StreamSource
{
    URL url;
    
    public UrlStreamSource(final URL url) {
        this.url = url;
    }
    
    @Override
    public InputStream getInputStream() {
        try {
            return this.url.openStream();
        }
        catch (IOException e) {
            throw new ProcessEngineException("couldn't open url '" + this.url + "'", e);
        }
    }
}
