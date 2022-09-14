// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class CompositePermissionCheck
{
    protected boolean disjunctive;
    protected List<CompositePermissionCheck> compositeChecks;
    protected List<PermissionCheck> atomicChecks;
    
    public CompositePermissionCheck() {
        this(true);
    }
    
    public CompositePermissionCheck(final boolean disjunctive) {
        this.compositeChecks = new ArrayList<CompositePermissionCheck>();
        this.atomicChecks = new ArrayList<PermissionCheck>();
        this.disjunctive = disjunctive;
    }
    
    public void addAtomicCheck(final PermissionCheck permissionCheck) {
        this.atomicChecks.add(permissionCheck);
    }
    
    public void setAtomicChecks(final List<PermissionCheck> atomicChecks) {
        this.atomicChecks = atomicChecks;
    }
    
    public void setCompositeChecks(final List<CompositePermissionCheck> subChecks) {
        this.compositeChecks = subChecks;
    }
    
    public void addCompositeCheck(final CompositePermissionCheck subCheck) {
        this.compositeChecks.add(subCheck);
    }
    
    public boolean isDisjunctive() {
        return this.disjunctive;
    }
    
    public List<CompositePermissionCheck> getCompositeChecks() {
        return this.compositeChecks;
    }
    
    public List<PermissionCheck> getAtomicChecks() {
        return this.atomicChecks;
    }
    
    public void clear() {
        this.compositeChecks.clear();
        this.atomicChecks.clear();
    }
    
    public List<PermissionCheck> getAllPermissionChecks() {
        final List<PermissionCheck> allChecks = new ArrayList<PermissionCheck>();
        allChecks.addAll(this.atomicChecks);
        for (final CompositePermissionCheck compositePermissionCheck : this.compositeChecks) {
            allChecks.addAll(compositePermissionCheck.getAllPermissionChecks());
        }
        return allChecks;
    }
}
