// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import java.util.List;
import java.io.Serializable;

public class ListQueryParameterObject implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected AuthorizationCheck authCheck;
    protected TenantCheck tenantCheck;
    protected List<QueryOrderingProperty> orderingProperties;
    protected int maxResults;
    protected int firstResult;
    protected Object parameter;
    protected String databaseType;
    
    public ListQueryParameterObject() {
        this.authCheck = new AuthorizationCheck();
        this.tenantCheck = new TenantCheck();
        this.orderingProperties = new ArrayList<QueryOrderingProperty>();
        this.maxResults = Integer.MAX_VALUE;
        this.firstResult = 0;
    }
    
    public ListQueryParameterObject(final Object parameter, final int firstResult, final int maxResults) {
        this.authCheck = new AuthorizationCheck();
        this.tenantCheck = new TenantCheck();
        this.orderingProperties = new ArrayList<QueryOrderingProperty>();
        this.maxResults = Integer.MAX_VALUE;
        this.firstResult = 0;
        this.parameter = parameter;
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
    
    public int getFirstResult() {
        return this.firstResult;
    }
    
    public int getFirstRow() {
        return this.firstResult + 1;
    }
    
    public int getLastRow() {
        if (this.maxResults == Integer.MAX_VALUE) {
            return this.maxResults;
        }
        return this.firstResult + this.maxResults + 1;
    }
    
    public int getMaxResults() {
        return this.maxResults;
    }
    
    public Object getParameter() {
        return this.parameter;
    }
    
    public void setFirstResult(final int firstResult) {
        this.firstResult = firstResult;
    }
    
    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }
    
    public void setParameter(final Object parameter) {
        this.parameter = parameter;
    }
    
    public void setDatabaseType(final String databaseType) {
        this.databaseType = databaseType;
    }
    
    public String getDatabaseType() {
        return this.databaseType;
    }
    
    public AuthorizationCheck getAuthCheck() {
        return this.authCheck;
    }
    
    public void setAuthCheck(final AuthorizationCheck authCheck) {
        this.authCheck = authCheck;
    }
    
    public TenantCheck getTenantCheck() {
        return this.tenantCheck;
    }
    
    public void setTenantCheck(final TenantCheck tenantCheck) {
        this.tenantCheck = tenantCheck;
    }
    
    public List<QueryOrderingProperty> getOrderingProperties() {
        return this.orderingProperties;
    }
    
    public void setOrderingProperties(final List<QueryOrderingProperty> orderingProperties) {
        this.orderingProperties = orderingProperties;
    }
}
