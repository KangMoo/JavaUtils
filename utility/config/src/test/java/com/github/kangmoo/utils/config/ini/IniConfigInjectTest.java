package com.github.kangmoo.utils.config.ini;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Data;
import org.ini4j.Ini;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kangmoo Heo
 */
@Data
class IniConfigInjectTest {
    File iniFile = new File("src/test/resources/sample.ini");
    @ConfigValue({"database", "url"})
    private String url;
    @ConfigValue({"database", "port"})
    private int port;

    @Test
    public void test() throws IOException, NoSuchFieldException {
        IniConfigInjectTest obj = new IniConfigInjectTest();
        IniConfigInjector.inject(obj, iniFile.toString());

        Ini ini = new Ini(iniFile);

        assertThat(obj.url).isEqualTo(ini.get("database").get("url"));
        assertThat(obj.port).isEqualTo(Integer.parseInt(ini.get("database").get("port")));
    }
}
