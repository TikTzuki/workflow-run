// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public class QueryPropertyImpl implements QueryProperty
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String function;
    
    public QueryPropertyImpl(final String name) {
        this(name, null);
    }
    
    public QueryPropertyImpl(final String name, final String function) {
        this.name = name;
        this.function = function;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getFunction() {
        return this.function;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.function == null) ? 0 : this.function.hashCode());
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final QueryPropertyImpl other = (QueryPropertyImpl)obj;
        if (this.function == null) {
            if (other.function != null) {
                return false;
            }
        }
        else if (!this.function.equals(other.function)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "QueryProperty[name=" + this.name + ", function=" + this.function + "]";
    }
}
