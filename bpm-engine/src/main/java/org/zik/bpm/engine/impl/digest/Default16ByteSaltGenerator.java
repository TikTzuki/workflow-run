// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

public class Default16ByteSaltGenerator extends Base64EncodedSaltGenerator
{
    @Override
    protected Integer getSaltLengthInByte() {
        return 16;
    }
}
