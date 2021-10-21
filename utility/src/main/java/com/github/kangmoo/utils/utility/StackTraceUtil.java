package com.github.kangmoo.utils.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 *
 * @author kangmoo Heo
 */
public class StackTraceUtil {
    public static String getStackTraceString(){
        StringWriter sw = new StringWriter();
        new Throwable("Stack Trace").printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
