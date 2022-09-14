// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Picture
{
    protected byte[] bytes;
    protected String mimeType;
    
    public Picture(final byte[] bytes, final String mimeType) {
        this.bytes = bytes;
        this.mimeType = mimeType;
    }
    
    public byte[] getBytes() {
        return this.bytes;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }
    
    public String getMimeType() {
        return this.mimeType;
    }
}
