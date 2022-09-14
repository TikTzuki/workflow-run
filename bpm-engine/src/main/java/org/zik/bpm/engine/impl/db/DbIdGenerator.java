// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.GetNextIdBlockCmd;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.IdGenerator;

public class DbIdGenerator implements IdGenerator
{
    protected int idBlockSize;
    protected long nextId;
    protected long lastId;
    protected CommandExecutor commandExecutor;
    
    public DbIdGenerator() {
        this.reset();
    }
    
    @Override
    public synchronized String getNextId() {
        if (this.lastId < this.nextId) {
            this.getNewBlock();
        }
        final long _nextId = this.nextId++;
        return Long.toString(_nextId);
    }
    
    protected synchronized void getNewBlock() {
        final IdBlock idBlock = this.commandExecutor.execute((Command<IdBlock>)new GetNextIdBlockCmd(this.idBlockSize));
        this.nextId = idBlock.getNextId();
        this.lastId = idBlock.getLastId();
    }
    
    public int getIdBlockSize() {
        return this.idBlockSize;
    }
    
    public void setIdBlockSize(final int idBlockSize) {
        this.idBlockSize = idBlockSize;
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public void setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
    
    public void reset() {
        this.nextId = 0L;
        this.lastId = -1L;
    }
}
