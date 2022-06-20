package com.github.kangmoo.utils.codec.pcm.audioresampler.audio;


import com.github.kangmoo.utils.codec.pcm.audioresampler.io.FileSink;
import com.github.kangmoo.utils.codec.pcm.audioresampler.io.FileSource;

public abstract class AudioController {
    protected FileSource fileSource;
    protected FileSink fileSink;
    protected AudioProperties properties;

    public AudioController(FileSource source, FileSink sink) {
        if (source == null || sink == null) {
            throw new IllegalArgumentException();
        }

        fileSource = source;
        fileSink = sink;
        properties = new AudioProperties();

        initializeProperties();
    }

    public AudioProperties getProperties() {
        return properties.copy();
    }

    protected abstract void initializeProperties();

    public abstract void applyFilter(AudioFilter filter);

    public abstract void saveToFile(String outputFilePath);

    public void close() {
        if (fileSource != null) {
            fileSource.close();
        }

        if (fileSink != null) {
            fileSink.close();
        }
    }
}
