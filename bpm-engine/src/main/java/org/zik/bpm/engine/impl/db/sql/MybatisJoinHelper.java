// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import java.util.HashMap;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;

public class MybatisJoinHelper
{
    protected static final EnginePersistenceLogger LOG;
    protected static final String DEFAULT_ORDER = "RES.ID_ asc";
    public static Map<String, MyBatisTableMapping> mappings;
    
    public static String tableAlias(final String relation, final int index) {
        if (relation == null) {
            return "RES";
        }
        final MyBatisTableMapping mapping = getTableMapping(relation);
        if (mapping.isOneToOneRelation()) {
            return mapping.getTableAlias();
        }
        return mapping.getTableAlias() + index;
    }
    
    public static String tableMapping(final String relation) {
        final MyBatisTableMapping mapping = getTableMapping(relation);
        return mapping.getTableName();
    }
    
    public static String orderBySelection(final QueryOrderingProperty orderingProperty, final int index) {
        final QueryProperty queryProperty = orderingProperty.getQueryProperty();
        final StringBuilder sb = new StringBuilder();
        if (queryProperty.getFunction() != null) {
            sb.append(queryProperty.getFunction());
            sb.append("(");
        }
        sb.append(tableAlias(orderingProperty.getRelation(), index));
        sb.append(".");
        sb.append(queryProperty.getName());
        if (queryProperty.getFunction() != null) {
            sb.append(")");
        }
        return sb.toString();
    }
    
    public static String orderBy(final QueryOrderingProperty orderingProperty, final int index) {
        final QueryProperty queryProperty = orderingProperty.getQueryProperty();
        final StringBuilder sb = new StringBuilder();
        sb.append(tableAlias(orderingProperty.getRelation(), index));
        if (orderingProperty.isContainedProperty()) {
            sb.append(".");
        }
        else {
            sb.append("_");
        }
        sb.append(queryProperty.getName());
        sb.append(" ");
        sb.append(orderingProperty.getDirection().getName());
        return sb.toString();
    }
    
    protected static MyBatisTableMapping getTableMapping(final String relation) {
        final MyBatisTableMapping mapping = MybatisJoinHelper.mappings.get(relation);
        if (mapping == null) {
            throw MybatisJoinHelper.LOG.missingRelationMappingException(relation);
        }
        return mapping;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
        (MybatisJoinHelper.mappings = new HashMap<String, MyBatisTableMapping>()).put("variable", new VariableTableMapping());
        MybatisJoinHelper.mappings.put("process-definition", new ProcessDefinitionTableMapping());
        MybatisJoinHelper.mappings.put("case-definition", new CaseDefinitionTableMapping());
        MybatisJoinHelper.mappings.put("deployment", new DeploymentTableMapping());
    }
}
