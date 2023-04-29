package com.github.kangmoo.utils.config.yaml;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author kangmoo Heo
 */
@Data
class YamlConfigTest extends YamlConfig {
    @ConfigValue("database.url")
    private String url;
    @ConfigValue("database.port")
    private int port;
    @ConfigValue("database.user")
    private List<String> name;

    @Test
    public void test() throws IOException, NoSuchFieldException {
        YamlConfigTest obj = new YamlConfigTest();
        obj.init(new File("src/test/resources/sample.yml").toString());
        System.out.println(obj);
    }
}
