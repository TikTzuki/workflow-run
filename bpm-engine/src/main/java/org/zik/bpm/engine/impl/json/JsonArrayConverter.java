// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import com.google.gson.JsonArray;

public abstract class JsonArrayConverter<T>
{
    public String toJson(final T object) {
        return this.toJsonArray(object).toString();
    }
    
    public abstract JsonArray toJsonArray(final T p0);
    
    public abstract T toObject(final JsonArray p0);
}
