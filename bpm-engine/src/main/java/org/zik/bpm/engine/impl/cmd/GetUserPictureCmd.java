// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.identity.Picture;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUserPictureCmd implements Command<Picture>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    
    public GetUserPictureCmd(final String userId) {
        this.userId = userId;
    }
    
    @Override
    public Picture execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("userId", (Object)this.userId);
        final IdentityInfoEntity pictureInfo = commandContext.getIdentityInfoManager().findUserInfoByUserIdAndKey(this.userId, "picture");
        if (pictureInfo != null) {
            final String pictureByteArrayId = pictureInfo.getValue();
            if (pictureByteArrayId != null) {
                final ByteArrayEntity byteArray = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, pictureByteArrayId);
                return new Picture(byteArray.getBytes(), byteArray.getName());
            }
        }
        return null;
    }
}
