package com.github.kangmoo.utils.log;
import org.apache.commons.cli.*;

public class Configuration {
    private static Options opts;
    private final String target;
    private final String splitPattern;
    private final String[] filters;


    public Configuration(CommandLine cmd) {
        this.target = cmd.getArgs().length > 0 ? cmd.getArgs()[0] : null;
        this.splitPattern = cmd.getOptionValue("p", "");
        this.filters = cmd.getOptionValues("f");
    }

    public static Options createOptions() {
        if (opts != null) return opts;
        opts = new Options();

        opts.addOption(Option.builder("p").argName("pattern").hasArg().desc("Log Pattern (regex)").build());
        opts.addOption(Option.builder("f").argName("filters").hasArgs().desc("Filtering Strings").build());
        return opts;
    }

    public String getSplitPattern() {
        return splitPattern;
    }

    public String[] getFilters() {
        return filters;
    }

    public String getTarget() {
        return target;
    }
}