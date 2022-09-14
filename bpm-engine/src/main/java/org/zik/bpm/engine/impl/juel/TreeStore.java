// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

public class TreeStore
{
    private final TreeCache cache;
    private final TreeBuilder builder;
    
    public TreeStore(final TreeBuilder builder, final TreeCache cache) {
        this.builder = builder;
        this.cache = cache;
    }
    
    public TreeBuilder getBuilder() {
        return this.builder;
    }
    
    public Tree get(final String expression) throws TreeBuilderException {
        if (this.cache == null) {
            return this.builder.build(expression);
        }
        Tree tree = this.cache.get(expression);
        if (tree == null) {
            this.cache.put(expression, tree = this.builder.build(expression));
        }
        return tree;
    }
}
