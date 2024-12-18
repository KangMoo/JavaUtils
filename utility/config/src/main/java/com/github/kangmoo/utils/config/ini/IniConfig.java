package com.github.kangmoo.utils.config.ini;

import com.github.kangmoo.utils.config.ConfigReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
