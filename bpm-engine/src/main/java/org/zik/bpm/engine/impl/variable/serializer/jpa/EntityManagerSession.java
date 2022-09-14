// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import javax.persistence.EntityManager;
import org.zik.bpm.engine.impl.interceptor.Session;

public interface EntityManagerSession extends Session
{
    EntityManager getEntityManager();
}
