// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jndi;

import org.zik.bpm.BpmPlatform;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

public class ProcessApplicationServiceObjectFactory implements ObjectFactory
{
    @Override
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        return BpmPlatform.getProcessApplicationService();
    }
}
