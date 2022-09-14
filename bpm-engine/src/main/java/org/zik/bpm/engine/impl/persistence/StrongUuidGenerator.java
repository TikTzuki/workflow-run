// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.zik.bpm.engine.impl.cfg.IdGenerator;

public class StrongUuidGenerator implements IdGenerator
{
    protected static TimeBasedGenerator timeBasedGenerator;
    
    public StrongUuidGenerator() {
        this.ensureGeneratorInitialized();
    }
    
    protected void ensureGeneratorInitialized() {
        if (StrongUuidGenerator.timeBasedGenerator == null) {
            synchronized (StrongUuidGenerator.class) {
                if (StrongUuidGenerator.timeBasedGenerator == null) {
                    StrongUuidGenerator.timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
    }
    
    @Override
    public String getNextId() {
        return StrongUuidGenerator.timeBasedGenerator.generate().toString();
    }
}
