// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public class QueryEntityRelationCondition
{
    protected QueryProperty property;
    protected QueryProperty comparisonProperty;
    protected Object scalarValue;
    
    public QueryEntityRelationCondition(final QueryProperty queryProperty, final Object scalarValue) {
        this(queryProperty, null, scalarValue);
    }
    
    public QueryEntityRelationCondition(final QueryProperty queryProperty, final QueryProperty comparisonProperty) {
        this(queryProperty, comparisonProperty, null);
    }
    
    public QueryEntityRelationCondition(final QueryProperty queryProperty, final QueryProperty comparisonProperty, final Object scalarValue) {
        this.property = queryProperty;
        this.comparisonProperty = comparisonProperty;
        this.scalarValue = scalarValue;
    }
    
    public QueryProperty getProperty() {
        return this.property;
    }
    
    public QueryProperty getComparisonProperty() {
        return this.comparisonProperty;
    }
    
    public Object getScalarValue() {
        return this.scalarValue;
    }
    
    public boolean isPropertyComparison() {
        return this.comparisonProperty != null;
    }
    
    @Override
    public String toString() {
        return "QueryEntityRelationCondition[property=" + this.property + ", comparisonProperty=" + this.comparisonProperty + ", scalarValue=" + this.scalarValue + "]";
    }
}
