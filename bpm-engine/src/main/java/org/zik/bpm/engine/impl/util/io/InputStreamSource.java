// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.io;

import java.io.InputStream;

public class InputStreamSource implements StreamSource
{
    InputStream inputStream;
    
    public InputStreamSource(final InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }
    
    @Override
    public String toString() {
        return "InputStream";
    }
}
