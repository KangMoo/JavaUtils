package com.github.kangmoo.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kangmoo Heo
 */
public class ULoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        return new ULogger(logger);
    }
}

