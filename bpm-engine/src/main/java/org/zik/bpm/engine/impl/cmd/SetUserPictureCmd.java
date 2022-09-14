// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.Picture;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetUserPictureCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected Picture picture;
    
    public SetUserPictureCmd(final String userId, final Picture picture) {
        this.userId = userId;
        this.picture = picture;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("userId", (Object)this.userId);
        IdentityInfoEntity pictureInfo = commandContext.getIdentityInfoManager().findUserInfoByUserIdAndKey(this.userId, "picture");
        if (pictureInfo != null) {
            final String byteArrayId = pictureInfo.getValue();
            if (byteArrayId != null) {
                commandContext.getByteArrayManager().deleteByteArrayById(byteArrayId);
            }
        }
        else {
            pictureInfo = new IdentityInfoEntity();
            pictureInfo.setUserId(this.userId);
            pictureInfo.setKey("picture");
            commandContext.getDbEntityManager().insert(pictureInfo);
        }
        final ByteArrayEntity byteArrayEntity = new ByteArrayEntity(this.picture.getMimeType(), this.picture.getBytes(), ResourceTypes.REPOSITORY);
        commandContext.getByteArrayManager().insertByteArray(byteArrayEntity);
        pictureInfo.setValue(byteArrayEntity.getId());
        return null;
    }
}
