// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.scanning.spi;

import java.util.Map;
import java.net.URL;

public interface ProcessApplicationScanner
{
    Map<String, byte[]> findResources(final ClassLoader p0, final String p1, final URL p2);
    
    Map<String, byte[]> findResources(final ClassLoader p0, final String p1, final URL p2, final String[] p3);
}
