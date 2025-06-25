package com.github.kangmoo.utils.config.yaml;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kangmoo Heo
 */
@Getter
@Setter
class YamlConfigTest extends YamlConfig {
    String yamlFilePath = "src/test/resources/sample.yml";
    @ConfigValue({"database","url"})
    private String url;
    @ConfigValue({"database","port"})
    private int port;
    @ConfigValue({"database","user"})
    private List<String> name;

    @Test
    @SuppressWarnings("unchecked")
    public void test() throws IOException, NoSuchFieldException, ConfigurationException {
        YamlConfigTest obj = new YamlConfigTest();
        obj.init(new File(yamlFilePath).toString());
        System.out.println(obj);

        Map<String, Object> yamlData = new Yaml().load(new FileInputStream(yamlFilePath));


        assertThat(obj.url).isEqualTo(((Map<String, String>) yamlData.get("database")).get("url"));
        assertThat(obj.port).isEqualTo(((Map<String, Integer>) yamlData.get("database")).get("port"));
        assertThat(obj.name).isEqualTo(((Map<String, List<String>>) yamlData.get("database")).get("user"));
    }
}
