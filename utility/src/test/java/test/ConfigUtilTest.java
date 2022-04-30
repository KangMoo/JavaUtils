package test;

import com.github.kangmoo.utils.utility.ConfigUtil;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author kangmoo Heo
 */
public class ConfigUtilTest {
    @Test
    public void test() throws InterruptedException, ConfigurationException {
        File configFile = new File("src/test/resources/config.ini");
        System.out.println(configFile.getAbsolutePath());
        ConfigUtil config = new ConfigUtil(configFile, () -> System.out.println("config changed"));

        System.out.println(config.getString("RMQ.HOST"));
        System.out.println(config.getString("RMQ.USER"));
        System.out.println(config.getInt("RMQ.PORT", -1));
    }
}
