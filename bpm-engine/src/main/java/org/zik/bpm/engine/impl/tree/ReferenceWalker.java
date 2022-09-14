// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ReferenceWalker<T>
{
    protected List<T> currentElements;
    protected List<TreeVisitor<T>> preVisitor;
    protected List<TreeVisitor<T>> postVisitor;
    
    protected abstract Collection<T> nextElements();
    
    public ReferenceWalker(final T initialElement) {
        this.preVisitor = new ArrayList<TreeVisitor<T>>();
        this.postVisitor = new ArrayList<TreeVisitor<T>>();
        (this.currentElements = new LinkedList<T>()).add(initialElement);
    }
    
    public ReferenceWalker(final List<T> initialElements) {
        this.preVisitor = new ArrayList<TreeVisitor<T>>();
        this.postVisitor = new ArrayList<TreeVisitor<T>>();
        this.currentElements = new LinkedList<T>((Collection<? extends T>)initialElements);
    }
    
    public ReferenceWalker<T> addPreVisitor(final TreeVisitor<T> collector) {
        this.preVisitor.add(collector);
        return this;
    }
    
    public ReferenceWalker<T> addPostVisitor(final TreeVisitor<T> collector) {
        this.postVisitor.add(collector);
        return this;
    }
    
    public T walkWhile() {
        return this.walkWhile(new NullCondition<T>());
    }
    
    public T walkUntil() {
        return this.walkUntil(new NullCondition<T>());
    }
    
    public T walkWhile(final WalkCondition<T> condition) {
        while (!condition.isFulfilled(this.getCurrentElement())) {
            for (final TreeVisitor<T> collector : this.preVisitor) {
                collector.visit(this.getCurrentElement());
            }
            this.currentElements.addAll(this.nextElements());
            this.currentElements.remove(0);
            for (final TreeVisitor<T> collector : this.postVisitor) {
                collector.visit(this.getCurrentElement());
            }
        }
        return this.getCurrentElement();
    }
    
    public T walkUntil(final WalkCondition<T> condition) {
        do {
            for (final TreeVisitor<T> collector : this.preVisitor) {
                collector.visit(this.getCurrentElement());
            }
            this.currentElements.addAll(this.nextElements());
            this.currentElements.remove(0);
            for (final TreeVisitor<T> collector : this.postVisitor) {
                collector.visit(this.getCurrentElement());
            }
        } while (!condition.isFulfilled(this.getCurrentElement()));
        return this.getCurrentElement();
    }
    
    public T getCurrentElement() {
        return this.currentElements.isEmpty() ? null : this.currentElements.get(0);
    }
    
    public static class NullCondition<S> implements WalkCondition<S>
    {
        @Override
        public boolean isFulfilled(final S element) {
            return element == null;
        }
        
        public static <S> WalkCondition<S> notNull() {
            return new NullCondition<S>();
        }
    }
    
    public interface WalkCondition<S>
    {
        boolean isFulfilled(final S p0);
    }
}
