// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.json.JsonTaskQueryConverter;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.Validator;
import org.zik.bpm.engine.impl.QueryValidators;
import java.util.HashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;
import org.zik.bpm.engine.filter.Filter;

public class FilterEntity implements Filter, Serializable, DbEntity, HasDbRevision, HasDbReferences, DbEntityLifecycleAware
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    public static final Map<String, JsonObjectConverter<?>> queryConverter;
    protected String id;
    protected String resourceType;
    protected String name;
    protected String owner;
    protected AbstractQuery query;
    protected Map<String, Object> properties;
    protected int revision;
    
    protected FilterEntity() {
        this.revision = 0;
    }
    
    public FilterEntity(final String resourceType) {
        this.revision = 0;
        this.setResourceType(resourceType);
        this.setQueryInternal("{}");
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public Filter setResourceType(final String resourceType) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Filter resource type must not be null or empty", "resourceType", resourceType);
        EnsureUtil.ensureNull(NotValidException.class, "Cannot overwrite filter resource type", "resourceType", this.resourceType);
        this.resourceType = resourceType;
        return this;
    }
    
    @Override
    public String getResourceType() {
        return this.resourceType;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Filter setName(final String name) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Filter name must not be null or empty", "name", name);
        this.name = name;
        return this;
    }
    
    @Override
    public String getOwner() {
        return this.owner;
    }
    
    @Override
    public Filter setOwner(final String owner) {
        this.owner = owner;
        return this;
    }
    
    @Override
    public <T extends Query<?, ?>> T getQuery() {
        return (T)this.query;
    }
    
    public String getQueryInternal() {
        final JsonObjectConverter<Object> converter = this.getConverter();
        return converter.toJson(this.query);
    }
    
    @Override
    public <T extends Query<?, ?>> Filter setQuery(final T query) {
        EnsureUtil.ensureNotNull(NotValidException.class, "query", query);
        this.query = (AbstractQuery)query;
        return this;
    }
    
    public void setQueryInternal(final String query) {
        EnsureUtil.ensureNotNull(NotValidException.class, "query", (Object)query);
        final JsonObjectConverter<Object> converter = this.getConverter();
        this.query = converter.toObject(JsonUtil.asObject(query));
    }
    
    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }
    
    public String getPropertiesInternal() {
        return JsonUtil.asString(this.properties);
    }
    
    @Override
    public Filter setProperties(final Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }
    
    public void setPropertiesInternal(final String properties) {
        if (properties != null) {
            final JsonObject json = JsonUtil.asObject(properties);
            this.properties = JsonUtil.asMap(json);
        }
        else {
            this.properties = null;
        }
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public <T extends Query<?, ?>> Filter extend(final T extendingQuery) {
        EnsureUtil.ensureNotNull(NotValidException.class, "extendingQuery", extendingQuery);
        if (!extendingQuery.getClass().equals(this.query.getClass())) {
            throw FilterEntity.LOG.queryExtensionException(this.query.getClass().getName(), extendingQuery.getClass().getName());
        }
        final FilterEntity copy = this.copyFilter();
        copy.setQuery((Query)this.query.extend(extendingQuery));
        return copy;
    }
    
    protected <T> JsonObjectConverter<T> getConverter() {
        final JsonObjectConverter<T> converter = (JsonObjectConverter<T>)FilterEntity.queryConverter.get(this.resourceType);
        if (converter != null) {
            return converter;
        }
        throw FilterEntity.LOG.unsupportedResourceTypeException(this.resourceType);
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("name", this.name);
        persistentState.put("owner", this.owner);
        persistentState.put("query", this.query);
        persistentState.put("properties", this.properties);
        return persistentState;
    }
    
    protected FilterEntity copyFilter() {
        final FilterEntity copy = new FilterEntity(this.getResourceType());
        copy.setName(this.getName());
        copy.setOwner(this.getOwner());
        copy.setQueryInternal(this.getQueryInternal());
        copy.setPropertiesInternal(this.getPropertiesInternal());
        return copy;
    }
    
    @Override
    public void postLoad() {
        if (this.query != null) {
            this.query.addValidator(QueryValidators.StoredQueryValidator.get());
        }
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        return referenceIdAndClass;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
        (queryConverter = new HashMap<String, JsonObjectConverter<?>>()).put("Task", new JsonTaskQueryConverter());
    }
}
