package com.github.kangmoo.utils.codec.pcm;

import java.io.*;
import java.util.Objects;

/**
 * @author kangmoo Heo
 */
public class WaveFile {
    /**
     * Make Wave Header Bytes
     *
     * @param sampleRate  Sample rate - 8000, 16000, etc.
     * @param channel Number of channels - Mono = 1, Stereo = 2, etc..
     * @param format Number of bits per sample (16 here)
     * @param dataSize Raw Data size
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static byte[] getData(int sampleRate, int channel, int format, int dataSize) {
        byte[] header = new byte[44];
        long totalDataLen = (long) dataSize + 36;
        long bitrate = (long) sampleRate * channel * format;
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = (byte) format;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channel;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) ((bitrate / 8) & 0xff);
        header[29] = (byte) (((bitrate / 8) >> 8) & 0xff);
        header[30] = (byte) (((bitrate / 8) >> 16) & 0xff);
        header[31] = (byte) (((bitrate / 8) >> 24) & 0xff);
        header[32] = (byte) ((channel * format) / 8);
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (dataSize & 0xff);
        header[41] = (byte) ((dataSize >> 8) & 0xff);
        header[42] = (byte) ((dataSize >> 16) & 0xff);
        header[43] = (byte) ((dataSize >> 24) & 0xff);
        return header;
    }

    public static void rawToWav(final File rawFile, final File waveFile, int sampleRate, int channel, int format) throws IOException {
        try (FileInputStream fis = new FileInputStream(rawFile); FileOutputStream fos = new FileOutputStream(waveFile)) {
            fos.write(getData(sampleRate, channel, format, (int) rawFile.length()));
            transferTo(fis, fos);
        }
    }

    private static long transferTo(InputStream in, OutputStream out) throws IOException {
        Objects.requireNonNull(out, "out");
        long transferred = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
            out.write(buffer, 0, read);
            transferred += read;
        }
        return transferred;
    }
}