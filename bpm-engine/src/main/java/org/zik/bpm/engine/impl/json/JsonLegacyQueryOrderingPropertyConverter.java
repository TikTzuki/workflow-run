// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import java.util.List;

public class JsonLegacyQueryOrderingPropertyConverter
{
    public static final String ORDER_BY_DELIMITER = ",";
    public static JsonLegacyQueryOrderingPropertyConverter INSTANCE;
    
    public List<QueryOrderingProperty> fromOrderByString(final String orderByString) {
        final List<QueryOrderingProperty> properties = new ArrayList<QueryOrderingProperty>();
        final String[] split;
        final String[] orderByClauses = split = orderByString.split(",");
        for (String orderByClause : split) {
            orderByClause = orderByClause.trim();
            final String[] clauseParts = orderByClause.split(" ");
            if (clauseParts.length != 0) {
                if (clauseParts.length > 2) {
                    throw new ProcessEngineException("Invalid order by clause: " + orderByClause);
                }
                String function = null;
                String propertyPart = clauseParts[0];
                final int functionArgumentBegin = propertyPart.indexOf("(");
                if (functionArgumentBegin >= 0) {
                    function = propertyPart.substring(0, functionArgumentBegin);
                    final int functionArgumentEnd = propertyPart.indexOf(")");
                    propertyPart = propertyPart.substring(functionArgumentBegin + 1, functionArgumentEnd);
                }
                final String[] propertyParts = propertyPart.split("\\.");
                String property = null;
                if (propertyParts.length == 1) {
                    property = propertyParts[0];
                }
                else {
                    if (propertyParts.length != 2) {
                        throw new ProcessEngineException("Invalid order by property part: " + clauseParts[0]);
                    }
                    property = propertyParts[1];
                }
                final QueryProperty queryProperty = new QueryPropertyImpl(property, function);
                Direction direction = null;
                if (clauseParts.length == 2) {
                    final String directionPart = clauseParts[1];
                    direction = Direction.findByName(directionPart);
                }
                final QueryOrderingProperty orderingProperty = new QueryOrderingProperty(null, queryProperty);
                orderingProperty.setDirection(direction);
                properties.add(orderingProperty);
            }
        }
        return properties;
    }
    
    static {
        JsonLegacyQueryOrderingPropertyConverter.INSTANCE = new JsonLegacyQueryOrderingPropertyConverter();
    }
}
