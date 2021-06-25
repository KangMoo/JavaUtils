package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringJoiner;

/**
 *
 * @author kangmoo Heo
 */
public class CommandExecutor {
    public static String runCommandForOutput(String... commands) {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process p;
        String result = "";
        try {
            p = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            result = sj.toString();
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
