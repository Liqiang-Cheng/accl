/*
 * Copyright Medtronic, Inc. 2015
 *
 *  MEDTRONIC CONFIDENTIAL - This document is the property of Medtronic,
 *  Inc.,and must be accounted for. Information herein is confidential. Do
 *  not reproduce it, reveal it to unauthorized persons, or send it outside
 *  Medtronic without proper authorization.
 */

package com.medtronic.neuro.acclmobile.app;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.TimeZone;

public class AcclData {
    private short mX;
    private short mY;
    private short mZ;
    private boolean mIsVibrate;
    private long mTimestamp;

    public AcclData(short x, short y, short z, boolean isVibrate, long  timestamp) {
        mX = x;
        mY = y;
        mZ = z;
        mIsVibrate = isVibrate;
        mTimestamp = timestamp;
    }
    public static AcclData decodeByte(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
        short x = buffer.getShort();
        short y = buffer.getShort();
        short z = buffer.getShort();
        boolean isVibrate = buffer.get() == 1;
        long timeStamp = buffer.getLong();
        return new AcclData(x,y,z,isVibrate, timeStamp);
    }

    @Override
    public String toString() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return String.format("X: %d, Y: %d, Z: %d, isVibrate: %b, Timestamp: %s",
                mX, mY, mZ, mIsVibrate, new Date(mTimestamp).toString());
    }
}
