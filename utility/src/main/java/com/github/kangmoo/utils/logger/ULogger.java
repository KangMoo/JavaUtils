package com.github.kangmoo.utils.logger;

import ch.qos.logback.classic.spi.EventArgUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Logger will print Throwable full stack trace if log level is more than DEBUG.
 * Logger will print Throwable simple message if log level below DEBUG.
 * @author kangmoo Heo
 */
public class ULogger implements Logger {
    private Logger logger;

    public ULogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.info(String.format("%s. Exception by %s", format, arg));
        else
            logger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.info(format, new Object[]{arg1, arg2});
    }

    @Override
    public void info(String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.info(msg, t);
        } else {
            logger.info(msg, String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.info(marker, String.format("%s. Exception by %s", format, arg));
        else
            logger.info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.info(marker, format, new Object[]{arg1, arg2});
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.info(marker, msg, t);
        } else {
            logger.info(marker, String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.warn(String.format("%s. Exception by %s", format, arg));
        else
            logger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.warn(format, new Object[]{arg1, arg2});
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.warn(msg, t);
        } else {
            logger.warn(String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.warn(marker, String.format("%s. Exception by %s", format, arg));
        else
            logger.warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.warn(marker, format, new Object[]{arg1, arg2});
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.warn(marker, msg, t);
        } else {
            logger.warn(marker, String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.error(String.format("%s. Exception by %s", format, arg));
        else
            logger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.error(format, new Object[]{arg1, arg2});
    }

    @Override
    public void error(String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.error(msg, t);
        } else {
            logger.error(String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if (!logger.isDebugEnabled() && arg instanceof Throwable)
            logger.error(marker, String.format("%s. Exception by %s", format, arg));
        else
            logger.error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.error(marker, format, new Object[]{arg1, arg2});
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if (!logger.isDebugEnabled()) {
            Pair<Object[], Throwable> ex = extractThrowableAnRearrangeArguments(arguments);
            arguments = ex.getLeft();
            Throwable t = ex.getRight();
            if(t != null){
                format = String.format("%s. Exception by %s", format, t);
            }
        }
        logger.error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.error(marker, msg, t);
        } else {
            logger.error(marker, String.format("%s. Exception by %s", msg, t.toString()));
        }
    }

    private Pair<Object[], Throwable> extractThrowableAnRearrangeArguments(Object[] argArray) {
        Throwable extractedThrowable = EventArgUtil.extractThrowable(argArray);
        if (EventArgUtil.successfulExtraction(extractedThrowable)) {
            argArray = EventArgUtil.trimmedCopy(argArray);
        }
        return Pair.of(argArray, extractedThrowable);
    }
}
