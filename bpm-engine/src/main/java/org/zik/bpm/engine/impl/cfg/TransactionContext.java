// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

public interface TransactionContext
{
    void commit();
    
    void rollback();
    
    void addTransactionListener(final TransactionState p0, final TransactionListener p1);
    
    boolean isTransactionActive();
}
