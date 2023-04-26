package com.github.kangmoo.utils.config.yaml;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class YamlConfig {
    private FileWatcher fileWatcher;
    protected Path filePath;

    public void init(String filePath) throws FileNotFoundException, NoSuchFieldException {
        if (this.filePath != null) {
            log.warn("Config Already initiated");
            return;
        }

        this.filePath = Path.of(filePath);
        YamlConfigInjector.inject(this, this.filePath.toString());
        afterFieldSetting();
        log.info("Config init done");
    }

    private void fieldSetting() {
        try {
            YamlConfigInjector.inject(this, filePath.toString());
            afterFieldSetting();
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        }
    }

    protected void afterFieldSetting() {
    }

    public void startWatch() {
        if (this.fileWatcher != null) {
            log.warn("Config Already Watching");
            return;
        }
        this.fileWatcher = new FileWatcher(filePath);
        this.fileWatcher.startWatch(this::fieldSetting);
        log.info("Config Watch Start");
    }

    public void stopWatch() {
        if (this.fileWatcher == null) {
            log.warn("Config Already Not Watching");
            return;
        }
        this.fileWatcher.stopWatching();
        log.info("Config Watch Stop");
    }
}
