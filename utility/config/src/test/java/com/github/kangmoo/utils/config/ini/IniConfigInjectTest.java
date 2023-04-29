package com.github.kangmoo.utils.config.ini;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author kangmoo Heo
 */
@Data
class IniConfigInjectTest {
    @ConfigValue("database.url")
    private String url;
    @ConfigValue("database.port")
    private int port;

    @Test
    public void test() throws IOException, NoSuchFieldException {
        IniConfigInjectTest obj = new IniConfigInjectTest();
        IniConfigInjector.inject(obj, new File("src/test/resources/sample.ini").toString());
        System.out.println(obj);
    }
}
