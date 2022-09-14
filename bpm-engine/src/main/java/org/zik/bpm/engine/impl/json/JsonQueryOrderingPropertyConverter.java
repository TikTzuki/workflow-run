// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.QueryEntityRelationCondition;
import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.impl.VariableOrderProperty;
import com.google.gson.JsonArray;
import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import java.util.List;
import org.zik.bpm.engine.impl.QueryOrderingProperty;

public class JsonQueryOrderingPropertyConverter extends JsonObjectConverter<QueryOrderingProperty>
{
    protected static JsonQueryOrderingPropertyConverter INSTANCE;
    protected static JsonArrayConverter<List<QueryOrderingProperty>> ARRAY_CONVERTER;
    public static final String RELATION = "relation";
    public static final String QUERY_PROPERTY = "queryProperty";
    public static final String QUERY_PROPERTY_FUNCTION = "queryPropertyFunction";
    public static final String DIRECTION = "direction";
    public static final String RELATION_CONDITIONS = "relationProperties";
    
    @Override
    public JsonObject toJsonObject(final QueryOrderingProperty property) {
        final JsonObject jsonObject = JsonUtil.createObject();
        JsonUtil.addField(jsonObject, "relation", property.getRelation());
        final QueryProperty queryProperty = property.getQueryProperty();
        if (queryProperty != null) {
            JsonUtil.addField(jsonObject, "queryProperty", queryProperty.getName());
            JsonUtil.addField(jsonObject, "queryPropertyFunction", queryProperty.getFunction());
        }
        final Direction direction = property.getDirection();
        if (direction != null) {
            JsonUtil.addField(jsonObject, "direction", direction.getName());
        }
        if (property.hasRelationConditions()) {
            final JsonArray relationConditionsJson = JsonQueryFilteringPropertyConverter.ARRAY_CONVERTER.toJsonArray(property.getRelationConditions());
            JsonUtil.addField(jsonObject, "relationProperties", relationConditionsJson);
        }
        return jsonObject;
    }
    
    @Override
    public QueryOrderingProperty toObject(final JsonObject jsonObject) {
        String relation = null;
        if (jsonObject.has("relation")) {
            relation = JsonUtil.getString(jsonObject, "relation");
        }
        QueryOrderingProperty property = null;
        if ("variable".equals(relation)) {
            property = new VariableOrderProperty();
        }
        else {
            property = new QueryOrderingProperty();
        }
        property.setRelation(relation);
        if (jsonObject.has("queryProperty")) {
            final String propertyName = JsonUtil.getString(jsonObject, "queryProperty");
            String propertyFunction = null;
            if (jsonObject.has("queryPropertyFunction")) {
                propertyFunction = JsonUtil.getString(jsonObject, "queryPropertyFunction");
            }
            final QueryProperty queryProperty = new QueryPropertyImpl(propertyName, propertyFunction);
            property.setQueryProperty(queryProperty);
        }
        if (jsonObject.has("direction")) {
            final String direction = JsonUtil.getString(jsonObject, "direction");
            property.setDirection(Direction.findByName(direction));
        }
        if (jsonObject.has("relationProperties")) {
            final List<QueryEntityRelationCondition> relationConditions = JsonQueryFilteringPropertyConverter.ARRAY_CONVERTER.toObject(JsonUtil.getArray(jsonObject, "relationProperties"));
            property.setRelationConditions(relationConditions);
        }
        return property;
    }
    
    static {
        JsonQueryOrderingPropertyConverter.INSTANCE = new JsonQueryOrderingPropertyConverter();
        JsonQueryOrderingPropertyConverter.ARRAY_CONVERTER = (JsonArrayConverter<List<QueryOrderingProperty>>)new JsonArrayOfObjectsConverter((JsonObjectConverter<Object>)JsonQueryOrderingPropertyConverter.INSTANCE);
    }
}
