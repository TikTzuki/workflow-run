// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.json;

import com.google.gson.JsonObject;

public abstract class JsonObjectConverter<T>
{
    public String toJson(final T object) {
        return this.toJsonObject(object).toString();
    }
    
    public abstract JsonObject toJsonObject(final T p0);
    
    public abstract T toObject(final JsonObject p0);
}
