// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonObject;
import java.util.List;
import org.zik.bpm.engine.impl.QueryEntityRelationCondition;

public class JsonQueryFilteringPropertyConverter extends JsonObjectConverter<QueryEntityRelationCondition>
{
    protected static JsonQueryFilteringPropertyConverter INSTANCE;
    protected static JsonArrayConverter<List<QueryEntityRelationCondition>> ARRAY_CONVERTER;
    public static final String BASE_PROPERTY = "baseField";
    public static final String COMPARISON_PROPERTY = "comparisonField";
    public static final String SCALAR_VALUE = "value";
    
    @Override
    public JsonObject toJsonObject(final QueryEntityRelationCondition filteringProperty) {
        final JsonObject jsonObject = JsonUtil.createObject();
        JsonUtil.addField(jsonObject, "baseField", filteringProperty.getProperty().getName());
        final QueryProperty comparisonProperty = filteringProperty.getComparisonProperty();
        if (comparisonProperty != null) {
            JsonUtil.addField(jsonObject, "comparisonField", comparisonProperty.getName());
        }
        final Object scalarValue = filteringProperty.getScalarValue();
        if (scalarValue != null) {
            JsonUtil.addFieldRawValue(jsonObject, "value", scalarValue);
        }
        return jsonObject;
    }
    
    @Override
    public QueryEntityRelationCondition toObject(final JsonObject jsonObject) {
        String scalarValue = null;
        if (jsonObject.has("value")) {
            scalarValue = JsonUtil.getString(jsonObject, "value");
        }
        QueryProperty baseProperty = null;
        if (jsonObject.has("baseField")) {
            baseProperty = new QueryPropertyImpl(JsonUtil.getString(jsonObject, "baseField"));
        }
        QueryProperty comparisonProperty = null;
        if (jsonObject.has("comparisonField")) {
            comparisonProperty = new QueryPropertyImpl(JsonUtil.getString(jsonObject, "comparisonField"));
        }
        return new QueryEntityRelationCondition(baseProperty, comparisonProperty, scalarValue);
    }
    
    static {
        JsonQueryFilteringPropertyConverter.INSTANCE = new JsonQueryFilteringPropertyConverter();
        JsonQueryFilteringPropertyConverter.ARRAY_CONVERTER = (JsonArrayConverter<List<QueryEntityRelationCondition>>)new JsonArrayOfObjectsConverter((JsonObjectConverter<Object>)JsonQueryFilteringPropertyConverter.INSTANCE);
    }
}
