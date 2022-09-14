// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;

public class JpaHelper
{
    public static EntityManagerFactory createEntityManagerFactory(final String jpaPersistenceUnitName) {
        return Persistence.createEntityManagerFactory(jpaPersistenceUnitName);
    }
}
