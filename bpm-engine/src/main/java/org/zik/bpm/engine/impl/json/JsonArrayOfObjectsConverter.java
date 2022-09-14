// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import java.util.ArrayList;
import com.google.gson.JsonElement;
import java.util.Iterator;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.JsonArray;
import java.util.List;

public class JsonArrayOfObjectsConverter<T> extends JsonArrayConverter<List<T>>
{
    protected JsonObjectConverter<T> objectConverter;
    
    public JsonArrayOfObjectsConverter(final JsonObjectConverter<T> objectConverter) {
        this.objectConverter = objectConverter;
    }
    
    @Override
    public JsonArray toJsonArray(final List<T> objects) {
        final JsonArray jsonArray = JsonUtil.createArray();
        for (final T object : objects) {
            final JsonElement jsonObject = this.objectConverter.toJsonObject(object);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    
    @Override
    public List<T> toObject(final JsonArray jsonArray) {
        final List<T> result = new ArrayList<T>();
        for (final JsonElement jsonElement : jsonArray) {
            final T object = this.objectConverter.toObject(JsonUtil.getObject(jsonElement));
            result.add(object);
        }
        return result;
    }
}
