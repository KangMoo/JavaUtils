package com.github.kangmoo.utils.config.json;

import com.github.kangmoo.utils.config.ConfigValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kangmoo Heo
 */
@Getter
@Setter
class JsonConfigTest extends JsonConfig {
    String jsonFilePath = "src/test/resources/sample.json";
    @ConfigValue({"database","url"})
    private String url;
    @ConfigValue({"database","port"})
    private int port;
    @ConfigValue({"database","user"})
    private List<String> name;

    @Test
    public void test() throws IOException, NoSuchFieldException, ConfigurationException {
        JsonConfigTest obj = new JsonConfigTest();
        obj.init(new File(jsonFilePath).toString());
        System.out.println(obj);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(Files.readString(Path.of(jsonFilePath)), JsonElement.class);

        gson.fromJson(jsonElement.getAsJsonObject().get("database").getAsJsonObject().get("user"), List.class);

        assertThat(obj.url).isEqualTo(jsonElement.getAsJsonObject().get("database").getAsJsonObject().get("url").getAsString());
        assertThat(obj.port).isEqualTo(jsonElement.getAsJsonObject().get("database").getAsJsonObject().get("port").getAsInt());
        assertThat(obj.name).isEqualTo(gson.fromJson(jsonElement.getAsJsonObject().get("database").getAsJsonObject().get("user"), List.class));
    }
}
