// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

public interface SessionFactory
{
    Class<?> getSessionType();
    
    Session openSession();
}
