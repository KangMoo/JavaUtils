package com.github.kangmoo.utils.config.env;

import com.github.kangmoo.utils.config.SysEnv;
import com.github.kangmoo.utils.config.SysEnvInjector;
import lombok.Data;
import org.junit.jupiter.api.Test;

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
