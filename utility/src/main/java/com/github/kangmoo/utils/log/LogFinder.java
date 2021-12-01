package com.github.kangmoo.utils.log;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;

/**
 *
 * @author kangmoo Heo
 */
public class LogFinder {
    public static void main(String[] args) {
        try {
            Configuration cfg = getCfg(args);
            if (cfg == null) return;
            List<File> files = getFileListRecursively(new File(cfg.getTarget()));

            files.parallelStream().sorted(Comparator.comparingLong(File::lastModified))
                    .map(o -> splitAndFilter(o.getAbsolutePath(), cfg.getFilters(), cfg.getSplitPattern()))
                    .forEachOrdered(o -> o.forEach(System.out::println));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Configuration getCfg(String args[]) {
        try {
            CommandLineParser parser = new DefaultParser();
            Configuration cfg;
            CommandLine cmd = parser.parse(Configuration.createOptions(), args);
            cfg = new Configuration(cmd);
            if (cmd.hasOption("h") || cfg.getTarget() == null) {
                new HelpFormatter().printHelp("\n", Configuration.createOptions());
                return null;
            }
            return cfg;
        } catch (Exception e) {
            new HelpFormatter().printHelp("\n", Configuration.createOptions());
            return null;
        }
    }

    public static List<File> getFileListRecursively(File file) {
        List<File> files = new ArrayList<>();
        if (file == null || !file.exists()) return files;
        if (file.isDirectory()) {
            Optional.ofNullable(file.listFiles()).ifPresent(listFiles ->
                    Arrays.stream(listFiles)
                            .filter(Objects::nonNull)
                            .forEach(o -> files.addAll(getFileListRecursively(o))));
        } else {
            files.add(file);
        }
        return files;
    }

    public static List<String> splitAndFilter(String filePath, String[] filters, String splitPattern) {
        List<String> filteredLogs = new ArrayList<>();
        filteredLogs.add("<<<" + filePath + ">>>");
        try (Scanner sc = new Scanner(new File(filePath))) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                Optional.ofNullable(sc.findInLine(splitPattern))
                        .ifPresent(s -> {
                            Arrays.stream(filters).filter(sb.toString()::contains).findAny().ifPresent(o -> filteredLogs.add(sb.toString()));
                            sb.setLength(0);
                            sb.append(s);
                        });
                sb.append(sc.nextLine()).append('\n');
            }
            Arrays.stream(filters).filter(sb.toString()::contains).findAny().ifPresent(o -> filteredLogs.add(sb.toString()));
        } catch (Exception ignored) {
            // Do nothing
        }
        if (filteredLogs.size() <= 1) filteredLogs.clear();
        return filteredLogs;
    }
}
