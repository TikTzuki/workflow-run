// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteUserPictureCmd implements Command<Void>
{
    protected String userId;
    
    public DeleteUserPictureCmd(final String userId) {
        this.userId = userId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("UserId", (Object)this.userId);
        final IdentityInfoEntity infoEntity = commandContext.getIdentityInfoManager().findUserInfoByUserIdAndKey(this.userId, "picture");
        if (infoEntity != null) {
            final String byteArrayId = infoEntity.getValue();
            if (byteArrayId != null) {
                commandContext.getByteArrayManager().deleteByteArrayById(byteArrayId);
            }
            commandContext.getIdentityInfoManager().delete(infoEntity);
        }
        return null;
    }
}
