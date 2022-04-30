package com.github.kangmoo.utils.utility;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.slf4j.Logger;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;


public class ConfigUtil {
    private static final Logger log = getLogger(ConfigUtil.class);
    private ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    private Configuration config;

    public ConfigUtil(File file, Runnable onChanged) throws ConfigurationException, NullPointerException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(onChanged);
        this.builder = new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(INIConfiguration.class)
                .configure(new Parameters().fileBased()
                        .setFile(file)
                        .setReloadingRefreshDelay(0L));

        this.config = this.builder.getConfiguration();
        new PeriodicReloadingTrigger(builder.getReloadingController(), null, 1, TimeUnit.SECONDS).start();
        this.builder.addEventListener(ConfigurationBuilderEvent.RESET, new EventListener<ConfigurationBuilderEvent>() {
            @Override
            public void onEvent(ConfigurationBuilderEvent configurationBuilderEvent) {
                log.warn("Config Changed [{}]", file.getAbsolutePath());
                try {
                    Configuration newConfig = builder.getConfiguration();

                    ConfigurationComparator comparator = new StrictConfigurationComparator();
                    if (!comparator.compare(config, newConfig)) {
                        diffConfig(config, newConfig);
                    }
                    newConfig.clear();
                } catch (Exception e) {
                    log.warn("Err Occurs while Config change event", e);
                }
            }

            private void diffConfig(Configuration config1, Configuration config2) {
                boolean changed = false;
                for (Iterator<String> keys = config1.getKeys(); keys.hasNext(); ) {
                    String key = keys.next();
                    Object v1 = config1.getProperty(key);
                    Object v2 = config2.getProperty(key);

                    if (!Objects.equals(v1, v2)) {
                        config.setProperty(key, v2);
                        if (!changed) {
                            changed = true;
                        }
                    }
                }

                for (Iterator<String> keys = config2.getKeys(); keys.hasNext(); ) {
                    String key = keys.next();
                    Object v2 = config2.getProperty(key);

                    if (!config1.containsKey(key)) {
                        config.setProperty(key, v2);
                        if (!changed) {
                            changed = true;
                        }
                    }
                }
                if (changed) {
                    onChanged.run();
                }
            }
        });
    }

    public ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> getBuilder() {
        return builder;
    }

    public Configuration getConfig() {
        return config;
    }

    public boolean isEmpty() {
        return config.isEmpty();
    }

    public int size() {
        return config.size();
    }

    public boolean containsKey(String key) {
        return config.containsKey(key);
    }

    public Object getProperty(String key) {
        return config.getProperty(key);
    }

    public Iterator<String> getKeys(String prefix) {
        return config.getKeys(prefix);
    }

    public Iterator<String> getKeys() {
        return config.getKeys();
    }

    public Properties getProperties(String key) {
        return config.getProperties(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public byte getByte(String key) {
        return config.getByte(key);
    }

    public byte getByte(String key, byte defaultValue) {
        return config.getByte(key, defaultValue);
    }

    public Byte getByte(String key, Byte defaultValue) {
        return config.getByte(key, defaultValue);
    }

    public double getDouble(String key) {
        return config.getDouble(key);
    }

    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public float getFloat(String key) {
        return config.getFloat(key);
    }

    public float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return config.getInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return config.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public short getShort(String key) {
        return config.getShort(key);
    }

    public short getShort(String key, short defaultValue) {
        return config.getShort(key, defaultValue);
    }

    public Short getShort(String key, Short defaultValue) {
        return config.getShort(key, defaultValue);
    }

    public BigDecimal getBigDecimal(String key) {
        return config.getBigDecimal(key);
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return config.getBigDecimal(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return config.getBigInteger(key);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return config.getBigInteger(key, defaultValue);
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public String getEncodedString(String key, ConfigurationDecoder decoder) {
        return config.getEncodedString(key, decoder);
    }

    public String getEncodedString(String key) {
        return config.getEncodedString(key);
    }

    public String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    public List<Object> getList(String key) {
        return config.getList(key);
    }

    public List<Object> getList(String key, List<?> defaultValue) {
        return config.getList(key, defaultValue);
    }

    public <T> T get(Class<T> cls, String key) {
        return config.get(cls, key);
    }

    public <T> T get(Class<T> cls, String key, T defaultValue) {
        return config.get(cls, key, defaultValue);
    }

    public Object getArray(Class<?> cls, String key) {
        return config.getArray(cls, key);
    }

    @Deprecated
    public Object getArray(Class<?> cls, String key, Object defaultValue) {
        return config.getArray(cls, key, defaultValue);
    }

    public <T> List<T> getList(Class<T> cls, String key) {
        return config.getList(cls, key);
    }

    public <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
        return config.getList(cls, key, defaultValue);
    }

    public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target) {
        return config.getCollection(cls, key, target);
    }

    public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target, Collection<T> defaultValue) {
        return config.getCollection(cls, key, target, defaultValue);
    }

    public ImmutableConfiguration immutableSubset(String prefix) {
        return config.immutableSubset(prefix);
    }
}