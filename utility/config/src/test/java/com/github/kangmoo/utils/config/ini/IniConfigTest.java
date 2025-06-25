package com.github.kangmoo.utils.config.ini;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kangmoo Heo
 */
@Getter
@Setter
class IniConfigTest extends IniConfig {
    File iniFile = new File("src/test/resources/sample.ini");
    @ConfigValue({"database", "url"})
    private String url;
    @ConfigValue({"database", "port"})
    private int port;

    @Test
    public void test() throws IOException, NoSuchFieldException, ConfigurationException {
        IniConfigTest obj = new IniConfigTest();
        obj.init(iniFile.toString());

        INIConfiguration iniConfig = new INIConfiguration();
        new FileHandler(iniConfig).load(iniFile);
        SubnodeConfiguration section = iniConfig.getSection("database");

        assertThat(obj.url).isEqualTo(section.getString("url"));
        assertThat(obj.port).isEqualTo(section.getInt("port"));
    }
}
