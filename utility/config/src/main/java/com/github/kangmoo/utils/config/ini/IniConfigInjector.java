package com.github.kangmoo.utils.config.ini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kangmoo.utils.config.json.JsonConfigInjector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * INI->JSON 변환 주입기
 *
 * @author kangmoo Heo
 */
@Slf4j
public final class IniConfigInjector {

    private IniConfigInjector() {
    }

    public static void inject(Object target, String iniFilePath)
            throws IOException, NoSuchFieldException, ConfigurationException {

        INIConfiguration iniConfig = new INIConfiguration();
        new FileHandler(iniConfig).load(new File(iniFilePath));

        Map<String, Map<String, String>> iniMap = new HashMap<>();
        for (String sectionName : iniConfig.getSections()) {
            SubnodeConfiguration section = iniConfig.getSection(sectionName);
            Map<String, String> kv = new HashMap<>();

            for (Iterator<String> it = section.getKeys(); it.hasNext(); ) {
                String key = it.next();
                kv.put(key, section.getString(key));
            }
            iniMap.put(sectionName, kv);
        }

        String json = new ObjectMapper().writeValueAsString(iniMap);
        JsonConfigInjector.injectValues(target, json);
    }
}