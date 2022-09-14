// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

public class Page
{
    protected int firstResult;
    protected int maxResults;
    
    public Page(final int firstResult, final int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
    
    public int getFirstResult() {
        return this.firstResult;
    }
    
    public int getMaxResults() {
        return this.maxResults;
    }
}
