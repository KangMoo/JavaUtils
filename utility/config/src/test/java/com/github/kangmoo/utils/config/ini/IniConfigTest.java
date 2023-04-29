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
class IniConfigTest extends IniConfig {
    @ConfigValue("database.url")
    private String url;
    @ConfigValue("database.port")
    private int port;

    @Test
    public void test() throws IOException, NoSuchFieldException {
        IniConfigTest obj = new IniConfigTest();
        obj.init(new File("src/test/resources/sample.ini").toString());
        System.out.println(obj);
    }
}
