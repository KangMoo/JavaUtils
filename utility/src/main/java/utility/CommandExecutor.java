package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author kangmoo Heo
 */
public class CommandExecutor {
    public static Future<String> runCommand(String... commands) {
        ExecutorService runner = Executors.newSingleThreadExecutor();
        try {
            return runner.submit(() -> runCommandForOutput(commands));
        } finally {
            runner.shutdown();
        }
    }

    public static String runCommandForOutput(String... commands) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process p;
        String result = "";
        p = pb.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
        reader.lines().iterator().forEachRemaining(sj::add);
        result = sj.toString();
        p.waitFor();
        p.destroy();
        return result;
    }

    public static String runBashCommandForOut(String... commands) throws Exception {
        String[] newCommands = new String[commands.length+2];
        newCommands[0] = "bash";
        newCommands[1] = "-c";
        System.arraycopy(commands, 0, newCommands, 2, commands.length);
        return runCommandForOutput(newCommands);
    }
}
