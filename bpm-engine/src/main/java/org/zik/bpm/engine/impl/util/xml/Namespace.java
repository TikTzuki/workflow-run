// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

public class Namespace
{
    private final String namespaceUri;
    private final String alternativeUri;
    
    public Namespace(final String namespaceUri) {
        this(namespaceUri, null);
    }
    
    public Namespace(final String namespaceUri, final String alternativeUri) {
        this.namespaceUri = namespaceUri;
        this.alternativeUri = alternativeUri;
    }
    
    public boolean hasAlternativeUri() {
        return this.alternativeUri != null;
    }
    
    public String getNamespaceUri() {
        return this.namespaceUri;
    }
    
    public String getAlternativeUri() {
        return this.alternativeUri;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.namespaceUri == null) ? 0 : this.namespaceUri.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Namespace other = (Namespace)obj;
        if (this.namespaceUri == null) {
            if (other.namespaceUri != null) {
                return false;
            }
        }
        else if (!this.namespaceUri.equals(other.namespaceUri)) {
            return false;
        }
        return true;
    }
}
