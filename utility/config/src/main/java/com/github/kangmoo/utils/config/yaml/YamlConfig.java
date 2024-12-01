package com.github.kangmoo.utils.config.yaml;

import com.github.kangmoo.utils.config.ConfigReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class YamlConfig extends ConfigReader {

    @Override
    protected final void fieldSetting() throws IOException, NoSuchFieldException {
        YamlConfigInjector.inject(this, filePath.toString());
    }
}
