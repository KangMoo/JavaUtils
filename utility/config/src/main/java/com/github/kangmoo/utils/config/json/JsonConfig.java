package com.github.kangmoo.utils.config.json;

import com.github.kangmoo.utils.config.ConfigReader;

import java.io.IOException;

/**
 * @author kangmoo Heo
 */
public class JsonConfig extends ConfigReader {

    @Override
    protected final void fieldSetting() throws IOException, NoSuchFieldException {
        JsonConfigInjector.inject(this, filePath.toString());
    }
}
