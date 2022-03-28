package com.github.kangmoo.utils.codec.amr;


/**
 * @author kangmoo Heo
 */
public class AmrPayload {
    private static final int[] AMRNB_FRAME_PAYLOAD_SIZE = new int[]{12, 13, 15, 17, 19, 20, 26, 31, 5, 0};
    private static final int[] AMRWB_FRAME_PAYLOAD_SIZE = new int[]{17, 23, 32, 36, 40, 46, 50, 58, 60, 0};
    private static final int AMR_FRMAE_HEADER_SIZE = 1;
    private static final int AMRWB_SID_FRAME_TYPE = 9;
    private static final int AMRNB_SID_FRAME_TYPE = 8;
    private static final int AMRWB_SPEECH_LOST_FRAME_TYPE = 14;
    private static final int AMR_NODATA_FRAME_TYPE = 15;
    private static final int FT_POS = 1;
    private boolean isWideband;
    private boolean oaMode;
    private byte[] data;

    public AmrPayload(byte[] data, boolean isWideband, boolean oaMode) {
        this.data = data;
        this.isWideband = isWideband;
        this.oaMode = oaMode;
    }

    public byte[] getAmrData() {
        int frameLength = (this.isWideband ? AMRWB_FRAME_PAYLOAD_SIZE[this.getFrameType()] : AMRNB_FRAME_PAYLOAD_SIZE[this.getFrameType()]) + 1;
        byte[] buffer = new byte[frameLength];
        if (this.oaMode) {
            System.arraycopy(this.data, 1, buffer, 0, frameLength);
        } else {
            buffer[0] = (byte)((this.data[0] & 15) << 4 | (this.data[1] & 192) >> 4);

            for(int idx = 1; idx < frameLength; ++idx) {
                if (idx == frameLength - 1) {
                    buffer[idx] = (byte)((this.data[idx] & 63) << 2);
                } else {
                    buffer[idx] = (byte)((this.data[idx] & 63) << 2 | (this.data[idx + 1] & 192) >> 6);
                }
            }
        }

        return buffer;
    }

    public boolean isSIDPacket() {
        return this.getFrameType() == (this.isWideband ? 9 : 8);
    }

    public boolean isNodataPacket() {
        int frameType = this.getFrameType();
        return frameType == 15 || this.isWideband && frameType == 14;
    }

    private int getFrameType() {
        return this.oaMode ? (this.data[1] & 120) >> 3 : (this.data[0] & 7) << 1 | (this.data[1] & 128) >> 7;
    }
}
