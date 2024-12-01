package com.github.kangmoo.utils.config.ini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kangmoo.utils.config.json.JsonConfigInjector;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class IniConfigInjector {

    private IniConfigInjector() {
    }

    public static void inject(Object target, String iniFilePath) throws IOException, NoSuchFieldException {
        String iniContent = Files.readString(Path.of(iniFilePath));
        Ini ini = new Ini();
        ini.load(new StringReader(iniContent));
        Map<String, Map<String, String>> iniMap = ini.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        JsonConfigInjector.injectValues(target, new ObjectMapper().writeValueAsString(iniMap));
    }
}
