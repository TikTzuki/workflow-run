// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.query.QueryProperty;
import java.io.Serializable;

public class QueryOrderingProperty implements Serializable
{
    public static final String RELATION_VARIABLE = "variable";
    public static final String RELATION_PROCESS_DEFINITION = "process-definition";
    public static final String RELATION_CASE_DEFINITION = "case-definition";
    public static final String RELATION_DEPLOYMENT = "deployment";
    protected static final long serialVersionUID = 1L;
    protected String relation;
    protected QueryProperty queryProperty;
    protected Direction direction;
    protected List<QueryEntityRelationCondition> relationConditions;
    
    public QueryOrderingProperty() {
    }
    
    public QueryOrderingProperty(final QueryProperty queryProperty, final Direction direction) {
        this.queryProperty = queryProperty;
        this.direction = direction;
    }
    
    public QueryOrderingProperty(final String relation, final QueryProperty queryProperty) {
        this.relation = relation;
        this.queryProperty = queryProperty;
    }
    
    public QueryProperty getQueryProperty() {
        return this.queryProperty;
    }
    
    public void setQueryProperty(final QueryProperty queryProperty) {
        this.queryProperty = queryProperty;
    }
    
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public List<QueryEntityRelationCondition> getRelationConditions() {
        return this.relationConditions;
    }
    
    public void setRelationConditions(final List<QueryEntityRelationCondition> relationConditions) {
        this.relationConditions = relationConditions;
    }
    
    public boolean hasRelationConditions() {
        return this.relationConditions != null && !this.relationConditions.isEmpty();
    }
    
    public String getRelation() {
        return this.relation;
    }
    
    public void setRelation(final String relation) {
        this.relation = relation;
    }
    
    public boolean isContainedProperty() {
        return this.relation == null && this.queryProperty.getFunction() == null;
    }
    
    @Override
    public String toString() {
        return "QueryOrderingProperty[relation=" + this.relation + ", queryProperty=" + this.queryProperty + ", direction=" + this.direction + ", relationConditions=" + this.getRelationConditionsString() + "]";
    }
    
    public String getRelationConditionsString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (this.relationConditions != null) {
            for (int i = 0; i < this.relationConditions.size(); ++i) {
                if (i > 0) {
                    builder.append(",");
                }
                builder.append(this.relationConditions.get(i));
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
