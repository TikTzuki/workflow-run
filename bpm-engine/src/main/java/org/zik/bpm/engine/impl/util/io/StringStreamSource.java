// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringStreamSource implements StreamSource
{
    String string;
    
    public StringStreamSource(final String string) {
        this.string = string;
    }
    
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.string.getBytes());
    }
    
    @Override
    public String toString() {
        return "String";
    }
}
