package com.github.kangmoo.utils.config.env;

import com.github.kangmoo.utils.config.ConfigValue;
import com.github.kangmoo.utils.config.SysEnv;
import com.github.kangmoo.utils.config.SysEnvInjector;
import com.github.kangmoo.utils.config.ini.IniConfigInjector;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author kangmoo Heo
 */
@Data
class EnvConfigInjectTest {
    @SysEnv("HOME")
    private String home;

    @Test
    public void test() {
        SysEnvInjector.inject(this);
        assertThat(this.home).isEqualTo(System.getenv("HOME"));
    }
}
