// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.diagram;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

public class Bpmn20NamespaceContext implements NamespaceContext
{
    public static final String BPMN = "bpmn";
    public static final String BPMNDI = "bpmndi";
    public static final String OMGDC = "omgdc";
    public static final String OMGDI = "omgdi";
    protected Map<String, String> namespaceUris;
    
    public Bpmn20NamespaceContext() {
        (this.namespaceUris = new HashMap<String, String>()).put("bpmn", "http://www.omg.org/spec/BPMN/20100524/MODEL");
        this.namespaceUris.put("bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
        this.namespaceUris.put("omgdc", "http://www.omg.org/spec/DD/20100524/DI");
        this.namespaceUris.put("omgdi", "http://www.omg.org/spec/DD/20100524/DC");
    }
    
    @Override
    public String getNamespaceURI(final String prefix) {
        return this.namespaceUris.get(prefix);
    }
    
    @Override
    public String getPrefix(final String namespaceURI) {
        return getKeyByValue(this.namespaceUris, namespaceURI);
    }
    
    @Override
    public Iterator<String> getPrefixes(final String namespaceURI) {
        return getKeysByValue(this.namespaceUris, namespaceURI).iterator();
    }
    
    private static <T, E> Set<T> getKeysByValue(final Map<T, E> map, final E value) {
        final Set<T> keys = new HashSet<T>();
        for (final Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
    
    private static <T, E> T getKeyByValue(final Map<T, E> map, final E value) {
        for (final Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
