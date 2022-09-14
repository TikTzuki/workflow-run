// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.io.Serializable;
import org.zik.bpm.engine.management.TablePage;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.management.TablePageQuery;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;

public class TablePageQueryImpl extends ListQueryParameterObject implements TablePageQuery, Command<TablePage>, Serializable
{
    private static final long serialVersionUID = 1L;
    transient CommandExecutor commandExecutor;
    protected String tableName;
    protected String order;
    
    public TablePageQueryImpl() {
    }
    
    public TablePageQueryImpl(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public TablePageQueryImpl tableName(final String tableName) {
        this.tableName = tableName;
        return this;
    }
    
    @Override
    public TablePageQueryImpl orderAsc(final String column) {
        this.orderingProperties.add(new QueryOrderingProperty(new QueryPropertyImpl(column), Direction.ASCENDING));
        return this;
    }
    
    @Override
    public TablePageQueryImpl orderDesc(final String column) {
        this.orderingProperties.add(new QueryOrderingProperty(new QueryPropertyImpl(column), Direction.DESCENDING));
        return this;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    @Override
    public TablePage listPage(final int firstResult, final int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        return this.commandExecutor.execute((Command<TablePage>)this);
    }
    
    @Override
    public TablePage execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdmin();
        return commandContext.getTableDataManager().getTablePage(this);
    }
    
    public String getOrder() {
        return this.order;
    }
}
