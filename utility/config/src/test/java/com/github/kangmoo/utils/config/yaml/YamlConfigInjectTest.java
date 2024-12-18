package com.github.kangmoo.utils.config.yaml;

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.Data;
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
@Data
class YamlConfigInjectTest {
    String yamlFilePath = "src/test/resources/sample.yml";
    @ConfigValue({"database","url"})
    private String url;
    @ConfigValue({"database","port"})
    private int port;
    @ConfigValue({"database","user"})
    private List<String> name;

    @Test
    @SuppressWarnings("unchecked")
    public void test() throws IOException, NoSuchFieldException {
        YamlConfigInjectTest obj = new YamlConfigInjectTest();
        YamlConfigInjector.inject(obj, new File(yamlFilePath).toString());

        Map<String, Object> yamlData = new Yaml().load(new FileInputStream(yamlFilePath));

        assertThat(obj.url).isEqualTo(((Map<String, String>) yamlData.get("database")).get("url"));
        assertThat(obj.port).isEqualTo(((Map<String, Integer>) yamlData.get("database")).get("port"));
        assertThat(obj.name).isEqualTo(((Map<String, List<String>>) yamlData.get("database")).get("user"));
    }
}
