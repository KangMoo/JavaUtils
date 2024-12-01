package com.github.kangmoo.utils.config.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kangmoo.utils.config.json.JsonConfigInjector;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class YamlConfigInjector {

    private YamlConfigInjector() {
    }

    public static void inject(Object target, String yamlFilePath) throws NoSuchFieldException, IOException {
        String yamlContent = Files.readString(Path.of(yamlFilePath));
        // YAML 문자열 읽기
        Map<String, Object> yamlMap = new Yaml().load(new StringReader(yamlContent));
        JsonConfigInjector.injectValues(target, new ObjectMapper().writeValueAsString(yamlMap));
    }
}
