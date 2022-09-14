// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompositeCondition
{
    protected CopyOnWriteArrayList<SingleConsumerCondition> conditions;
    
    public CompositeCondition() {
        this.conditions = new CopyOnWriteArrayList<SingleConsumerCondition>();
    }
    
    public void addConsumer(final SingleConsumerCondition condition) {
        this.conditions.add(condition);
    }
    
    public void removeConsumer(final SingleConsumerCondition condition) {
        this.conditions.remove(condition);
    }
    
    public void signalAll() {
        for (final SingleConsumerCondition condition : this.conditions) {
            condition.signal();
        }
    }
}
