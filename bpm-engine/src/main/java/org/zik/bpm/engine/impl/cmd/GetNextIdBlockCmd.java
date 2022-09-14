// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.IdBlock;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetNextIdBlockCmd implements Command<IdBlock>
{
    private static final long serialVersionUID = 1L;
    protected int idBlockSize;
    
    public GetNextIdBlockCmd(final int idBlockSize) {
        this.idBlockSize = idBlockSize;
    }
    
    @Override
    public IdBlock execute(final CommandContext commandContext) {
        final PropertyEntity property = commandContext.getPropertyManager().findPropertyById("next.dbid");
        final long oldValue = Long.parseLong(property.getValue());
        final long newValue = oldValue + this.idBlockSize;
        property.setValue(Long.toString(newValue));
        return new IdBlock(oldValue, newValue - 1L);
    }
}
