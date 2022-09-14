// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.Objects;
import java.io.Serializable;
import java.util.Map;

public class ImmutablePair<L, R> implements Map.Entry<L, R>, Serializable, Comparable<ImmutablePair<L, R>>
{
    private static final long serialVersionUID = -7043970803192830955L;
    protected L left;
    protected R right;
    
    public L getLeft() {
        return this.left;
    }
    
    public R getRight() {
        return this.right;
    }
    
    public ImmutablePair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public final L getKey() {
        return this.getLeft();
    }
    
    @Override
    public R getValue() {
        return this.getRight();
    }
    
    @Override
    public R setValue(final R value) {
        throw new UnsupportedOperationException("setValue not allowed for an ImmutablePair");
    }
    
    @Override
    public int compareTo(final ImmutablePair<L, R> o) {
        if (o == null) {
            throw new IllegalArgumentException("Pair to compare to must not be null");
        }
        try {
            final int leftComparison = this.compare(this.getLeft(), (Comparable)o.getLeft());
            return (leftComparison == 0) ? this.compare(this.getRight(), (Comparable)o.getRight()) : leftComparison;
        }
        catch (ClassCastException cce) {
            throw new IllegalArgumentException("Please provide comparable elements", cce);
        }
    }
    
    protected int compare(final Comparable original, final Comparable other) {
        if (original == other) {
            return 0;
        }
        if (original == null) {
            return -1;
        }
        if (other == null) {
            return 1;
        }
        return original.compareTo(other);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        final Map.Entry<?, ?> other = (Map.Entry<?, ?>)obj;
        return Objects.equals(this.getKey(), other.getKey()) && Objects.equals(this.getValue(), other.getValue());
    }
    
    @Override
    public int hashCode() {
        return ((this.getKey() == null) ? 0 : this.getKey().hashCode()) ^ ((this.getValue() == null) ? 0 : this.getValue().hashCode());
    }
    
    @Override
    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }
}
