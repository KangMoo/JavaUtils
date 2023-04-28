package com.github.kangmoo.utils.config.ini;

import com.github.kangmoo.utils.config.ConfigReader;
import com.github.kangmoo.utils.config.FileWatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class IniConfig extends ConfigReader {

    @Override
    protected final void fieldSetting() throws IOException, NoSuchFieldException {
        IniConfigInjector.inject(this, filePath.toString());
    }
}
