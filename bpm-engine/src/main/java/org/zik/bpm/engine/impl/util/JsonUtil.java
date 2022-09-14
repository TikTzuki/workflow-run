// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonDeserializer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.HashMap;
import com.google.gson.JsonPrimitive;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import com.google.gson.JsonParseException;
import com.google.gson.JsonNull;
import java.util.Iterator;
import com.google.gson.JsonArray;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

public final class JsonUtil
{
    private static final EngineUtilLogger LOG;
    protected static Gson gsonMapper;
    
    public static void addFieldRawValue(final JsonObject jsonObject, final String memberName, final Object rawValue) {
        if (rawValue != null) {
            JsonElement jsonNode = null;
            try {
                jsonNode = getGsonMapper().toJsonTree(rawValue);
            }
            catch (JsonIOException e) {
                JsonUtil.LOG.logJsonException(e);
            }
            if (jsonNode != null) {
                jsonObject.add(memberName, jsonNode);
            }
        }
    }
    
    public static <T> void addField(final JsonObject jsonObject, final String name, final JsonObjectConverter<T> converter, final T value) {
        if (jsonObject != null && name != null && converter != null && value != null) {
            jsonObject.add(name, converter.toJsonObject(value));
        }
    }
    
    public static void addListField(final JsonObject jsonObject, final String name, final List<String> list) {
        if (jsonObject != null && name != null && list != null) {
            jsonObject.add(name, asArray(list));
        }
    }
    
    public static void addArrayField(final JsonObject jsonObject, final String name, final String[] array) {
        if (jsonObject != null && name != null && array != null) {
            addListField(jsonObject, name, Arrays.asList(array));
        }
    }
    
    public static void addDateField(final JsonObject jsonObject, final String name, final Date date) {
        if (jsonObject != null && name != null && date != null) {
            jsonObject.addProperty(name, date.getTime());
        }
    }
    
    public static <T> void addElement(final JsonArray jsonObject, final JsonObjectConverter<T> converter, final T value) {
        if (jsonObject != null && converter != null && value != null) {
            final JsonObject jsonElement = converter.toJsonObject(value);
            if (jsonElement != null) {
                jsonObject.add(jsonElement);
            }
        }
    }
    
    public static <T> void addListField(final JsonObject jsonObject, final String name, final JsonObjectConverter<T> converter, final List<T> list) {
        if (jsonObject != null && name != null && converter != null && list != null) {
            final JsonArray arrayNode = createArray();
            for (final T item : list) {
                if (item != null) {
                    final JsonObject jsonElement = converter.toJsonObject(item);
                    if (jsonElement == null) {
                        continue;
                    }
                    arrayNode.add(jsonElement);
                }
            }
            jsonObject.add(name, arrayNode);
        }
    }
    
    public static <T> T asJavaObject(final JsonObject jsonObject, final JsonObjectConverter<T> converter) {
        if (jsonObject != null && converter != null) {
            return converter.toObject(jsonObject);
        }
        return null;
    }
    
    public static void addNullField(final JsonObject jsonObject, final String name) {
        if (jsonObject != null && name != null) {
            jsonObject.add(name, JsonNull.INSTANCE);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final JsonArray value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.add(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final String value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final Boolean value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final Integer value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final Short value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final Long value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addField(final JsonObject jsonObject, final String name, final Double value) {
        if (jsonObject != null && name != null && value != null) {
            jsonObject.addProperty(name, value);
        }
    }
    
    public static void addDefaultField(final JsonObject jsonObject, final String name, final boolean defaultValue, final Boolean value) {
        if (jsonObject != null && name != null && value != null && !value.equals(defaultValue)) {
            addField(jsonObject, name, value);
        }
    }
    
    public static byte[] asBytes(final JsonElement jsonObject) {
        String jsonString = null;
        if (jsonObject != null) {
            try {
                jsonString = getGsonMapper().toJson(jsonObject);
            }
            catch (JsonIOException e) {
                JsonUtil.LOG.logJsonException(e);
            }
        }
        if (jsonString == null) {
            jsonString = "";
        }
        return StringUtil.toByteArray(jsonString);
    }
    
    public static JsonObject asObject(final byte[] byteArray) {
        String stringValue = null;
        if (byteArray != null) {
            stringValue = StringUtil.fromBytes(byteArray);
        }
        if (stringValue == null) {
            return createObject();
        }
        JsonObject jsonObject = null;
        try {
            jsonObject = getGsonMapper().fromJson(stringValue, JsonObject.class);
        }
        catch (JsonParseException e) {
            JsonUtil.LOG.logJsonException(e);
        }
        if (jsonObject != null) {
            return jsonObject;
        }
        return createObject();
    }
    
    public static JsonObject asObject(final String jsonString) {
        JsonObject jsonObject = null;
        if (jsonString != null) {
            try {
                jsonObject = getGsonMapper().fromJson(jsonString, JsonObject.class);
            }
            catch (JsonParseException | ClassCastException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
            }
        }
        if (jsonObject != null) {
            return jsonObject;
        }
        return createObject();
    }
    
    public static JsonObject asObject(final Map<String, Object> properties) {
        if (properties == null) {
            return createObject();
        }
        JsonObject jsonObject = null;
        try {
            jsonObject = (JsonObject)getGsonMapper().toJsonTree(properties);
        }
        catch (JsonIOException | ClassCastException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            JsonUtil.LOG.logJsonException(e);
        }
        if (jsonObject != null) {
            return jsonObject;
        }
        return createObject();
    }
    
    public static List<String> asStringList(final JsonElement jsonObject) {
        JsonArray jsonArray = null;
        if (jsonObject != null) {
            try {
                jsonArray = jsonObject.getAsJsonArray();
            }
            catch (IllegalStateException | ClassCastException ex3) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
            }
        }
        if (jsonArray == null) {
            return Collections.emptyList();
        }
        final List<String> list = new ArrayList<String>();
        for (final JsonElement entry : jsonArray) {
            String stringValue = null;
            try {
                stringValue = entry.getAsString();
            }
            catch (IllegalStateException | ClassCastException ex4) {
                final RuntimeException ex2;
                final RuntimeException e2 = ex2;
                JsonUtil.LOG.logJsonException(e2);
            }
            if (stringValue != null) {
                list.add(stringValue);
            }
        }
        return list;
    }
    
    public static <T, S extends List<T>> S asList(final JsonArray jsonArray, final JsonObjectConverter<T> converter, final Supplier<S> listSupplier) {
        if (jsonArray == null || converter == null) {
            return (S)Collections.emptyList();
        }
        final S list = listSupplier.get();
        for (final JsonElement element : jsonArray) {
            JsonObject jsonObject = null;
            try {
                jsonObject = element.getAsJsonObject();
            }
            catch (IllegalStateException | ClassCastException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
            }
            if (jsonObject != null) {
                final T rawObject = converter.toObject(jsonObject);
                if (rawObject == null) {
                    continue;
                }
                list.add(rawObject);
            }
        }
        return list;
    }
    
    public static <T> List<T> asList(final JsonArray jsonArray, final JsonObjectConverter<T> converter) {
        return asList(jsonArray, converter, (Supplier<List<T>>)ArrayList::new);
    }
    
    public static List<Object> asList(final JsonElement jsonElement) {
        if (jsonElement == null) {
            return Collections.emptyList();
        }
        JsonArray jsonArray = null;
        try {
            jsonArray = jsonElement.getAsJsonArray();
        }
        catch (IllegalStateException | ClassCastException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            JsonUtil.LOG.logJsonException(e);
        }
        if (jsonArray == null) {
            return Collections.emptyList();
        }
        final List<Object> list = new ArrayList<Object>();
        for (final JsonElement entry : jsonArray) {
            if (entry.isJsonPrimitive()) {
                final Object rawObject = asPrimitiveObject((JsonPrimitive)entry);
                if (rawObject == null) {
                    continue;
                }
                list.add(rawObject);
            }
            else if (entry.isJsonNull()) {
                list.add(null);
            }
            else if (entry.isJsonObject()) {
                list.add(asMap(entry));
            }
            else {
                if (!entry.isJsonArray()) {
                    continue;
                }
                list.add(asList(entry));
            }
        }
        return list;
    }
    
    public static Map<String, Object> asMap(final JsonElement jsonElement) {
        if (jsonElement == null) {
            return Collections.emptyMap();
        }
        JsonObject jsonObject = null;
        try {
            jsonObject = jsonElement.getAsJsonObject();
        }
        catch (IllegalStateException | ClassCastException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            JsonUtil.LOG.logJsonException(e);
        }
        if (jsonObject == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final Map.Entry<String, JsonElement> jsonEntry : jsonObject.entrySet()) {
            final String key = jsonEntry.getKey();
            final JsonElement value = jsonEntry.getValue();
            if (value.isJsonPrimitive()) {
                final Object rawObject = asPrimitiveObject((JsonPrimitive)value);
                if (rawObject == null) {
                    continue;
                }
                map.put(key, rawObject);
            }
            else if (value.isJsonNull()) {
                map.put(key, null);
            }
            else if (value.isJsonObject()) {
                map.put(key, asMap(value));
            }
            else {
                if (!value.isJsonArray()) {
                    continue;
                }
                map.put(key, asList(value));
            }
        }
        return map;
    }
    
    public static String asString(final Map<String, Object> properties) {
        String stringValue = createObject().toString();
        if (properties != null) {
            final JsonObject jsonObject = asObject(properties);
            if (jsonObject != null) {
                stringValue = jsonObject.toString();
            }
        }
        return stringValue;
    }
    
    public static String asString(final Object data) {
        return JsonUtil.gsonMapper.toJson(data);
    }
    
    public static JsonArray asArray(final List<String> list) {
        if (list == null) {
            return createArray();
        }
        JsonElement jsonElement = null;
        try {
            jsonElement = getGsonMapper().toJsonTree(list);
        }
        catch (JsonIOException e) {
            JsonUtil.LOG.logJsonException(e);
        }
        if (jsonElement != null) {
            return getArray(jsonElement);
        }
        return createArray();
    }
    
    public static Object getRawObject(final JsonObject jsonObject, final String memberName) {
        if (jsonObject == null || memberName == null) {
            return null;
        }
        Object rawValue = null;
        if (jsonObject.has(memberName)) {
            JsonPrimitive jsonPrimitive = null;
            try {
                jsonPrimitive = jsonObject.getAsJsonPrimitive(memberName);
            }
            catch (ClassCastException e) {
                JsonUtil.LOG.logJsonException(e);
            }
            if (jsonPrimitive != null) {
                rawValue = asPrimitiveObject(jsonPrimitive);
            }
        }
        if (rawValue != null) {
            return rawValue;
        }
        return null;
    }
    
    public static Object asPrimitiveObject(final JsonPrimitive jsonValue) {
        if (jsonValue == null) {
            return null;
        }
        Object rawObject = null;
        if (jsonValue.isNumber()) {
            Object numberValue = null;
            try {
                numberValue = jsonValue.getAsNumber();
            }
            catch (NumberFormatException e) {
                JsonUtil.LOG.logJsonException(e);
            }
            if (numberValue != null && numberValue instanceof LazilyParsedNumber) {
                final String numberString = numberValue.toString();
                if (numberString != null) {
                    rawObject = parseNumber(numberString);
                }
            }
            else {
                rawObject = numberValue;
            }
        }
        else {
            try {
                rawObject = getGsonMapper().fromJson(jsonValue, Object.class);
            }
            catch (JsonSyntaxException | JsonIOException ex2) {
                final JsonParseException ex;
                final JsonParseException e2 = ex;
                JsonUtil.LOG.logJsonException(e2);
            }
        }
        if (rawObject != null) {
            return rawObject;
        }
        return null;
    }
    
    protected static Number parseNumber(final String numberString) {
        if (numberString == null) {
            return null;
        }
        try {
            return Integer.parseInt(numberString);
        }
        catch (NumberFormatException ex) {
            try {
                return Long.parseLong(numberString);
            }
            catch (NumberFormatException ex2) {
                try {
                    return Double.parseDouble(numberString);
                }
                catch (NumberFormatException ex3) {
                    return null;
                }
            }
        }
    }
    
    public static boolean getBoolean(final JsonObject json, final String memberName) {
        if (json != null && memberName != null && json.has(memberName)) {
            try {
                return json.get(memberName).getAsBoolean();
            }
            catch (ClassCastException | IllegalStateException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
                return false;
            }
        }
        return false;
    }
    
    public static String getString(final JsonObject json, final String memberName) {
        return getString(json, memberName, "");
    }
    
    public static String getString(final JsonObject json, final String memberName, final String defaultString) {
        if (json != null && memberName != null && json.has(memberName)) {
            return getString(json.get(memberName));
        }
        return defaultString;
    }
    
    public static String getString(final JsonElement jsonElement) {
        if (jsonElement == null) {
            return "";
        }
        try {
            return jsonElement.getAsString();
        }
        catch (ClassCastException | IllegalStateException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            JsonUtil.LOG.logJsonException(e);
            return "";
        }
    }
    
    public static int getInt(final JsonObject json, final String memberName) {
        if (json != null && memberName != null && json.has(memberName)) {
            try {
                return json.get(memberName).getAsInt();
            }
            catch (ClassCastException | IllegalStateException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
                return 0;
            }
        }
        return 0;
    }
    
    public static boolean isNull(final JsonObject jsonObject, final String memberName) {
        return jsonObject != null && memberName != null && jsonObject.has(memberName) && jsonObject.get(memberName).isJsonNull();
    }
    
    public static long getLong(final JsonObject json, final String memberName) {
        if (json != null && memberName != null && json.has(memberName)) {
            try {
                return json.get(memberName).getAsLong();
            }
            catch (ClassCastException | IllegalStateException ex2) {
                final RuntimeException ex;
                final RuntimeException e = ex;
                JsonUtil.LOG.logJsonException(e);
                return 0L;
            }
        }
        return 0L;
    }
    
    public static JsonArray getArray(final JsonObject json, final String memberName) {
        if (json != null && memberName != null && json.has(memberName)) {
            return getArray(json.get(memberName));
        }
        return createArray();
    }
    
    public static JsonArray getArray(final JsonElement json) {
        if (json != null && json.isJsonArray()) {
            return json.getAsJsonArray();
        }
        return createArray();
    }
    
    public static JsonObject getObject(final JsonObject json, final String memberName) {
        if (json != null && memberName != null && json.has(memberName)) {
            return getObject(json.get(memberName));
        }
        return createObject();
    }
    
    public static JsonObject getObject(final JsonElement json) {
        if (json != null && json.isJsonObject()) {
            return json.getAsJsonObject();
        }
        return createObject();
    }
    
    public static JsonObject createObject() {
        return new JsonObject();
    }
    
    public static JsonArray createArray() {
        return new JsonArray();
    }
    
    public static Gson getGsonMapper() {
        return JsonUtil.gsonMapper;
    }
    
    public static Gson createGsonMapper() {
        return new GsonBuilder().serializeNulls().registerTypeAdapter(Map.class, new JsonDeserializer<Map<String, Object>>() {
            @Override
            public Map<String, Object> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
                final Map<String, Object> map = new HashMap<String, Object>();
                for (final Map.Entry<String, JsonElement> entry : JsonUtil.getObject(json).entrySet()) {
                    if (entry != null) {
                        final String key = entry.getKey();
                        final JsonElement jsonElement = entry.getValue();
                        if (jsonElement != null && jsonElement.isJsonNull()) {
                            map.put(key, null);
                        }
                        else {
                            if (jsonElement == null || !jsonElement.isJsonPrimitive()) {
                                continue;
                            }
                            final Object rawValue = JsonUtil.asPrimitiveObject((JsonPrimitive)jsonElement);
                            if (rawValue == null) {
                                continue;
                            }
                            map.put(key, rawValue);
                        }
                    }
                }
                return map;
            }
        }).create();
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        JsonUtil.gsonMapper = createGsonMapper();
    }
}
