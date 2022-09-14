// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class BitMaskUtil
{
    private static final EngineUtilLogger LOG;
    private static final int FLAG_BIT_1 = 1;
    private static final int FLAG_BIT_2 = 2;
    private static final int FLAG_BIT_3 = 4;
    private static final int FLAG_BIT_4 = 8;
    private static final int FLAG_BIT_5 = 16;
    private static final int FLAG_BIT_6 = 32;
    private static final int FLAG_BIT_7 = 64;
    private static final int FLAG_BIT_8 = 128;
    private static int[] MASKS;
    
    public static int setBitOn(final int value, final int bitNumber) {
        ensureBitRange(bitNumber);
        return value | BitMaskUtil.MASKS[bitNumber - 1];
    }
    
    public static int setBitOff(final int value, final int bitNumber) {
        ensureBitRange(bitNumber);
        return value & ~BitMaskUtil.MASKS[bitNumber - 1];
    }
    
    public static boolean isBitOn(final int value, final int bitNumber) {
        ensureBitRange(bitNumber);
        return (value & BitMaskUtil.MASKS[bitNumber - 1]) == BitMaskUtil.MASKS[bitNumber - 1];
    }
    
    public static int setBit(final int value, final int bitNumber, final boolean bitValue) {
        if (bitValue) {
            return setBitOn(value, bitNumber);
        }
        return setBitOff(value, bitNumber);
    }
    
    public static int getMaskForBit(final int bitNumber) {
        return BitMaskUtil.MASKS[bitNumber - 1];
    }
    
    private static void ensureBitRange(final int bitNumber) {
        if (bitNumber <= 0 && bitNumber > 8) {
            throw BitMaskUtil.LOG.invalidBitNumber(bitNumber);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        BitMaskUtil.MASKS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
    }
}
