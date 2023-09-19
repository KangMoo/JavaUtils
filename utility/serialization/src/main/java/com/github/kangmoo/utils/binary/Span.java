package com.github.kangmoo.utils.binary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kangmoo Heo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Span {
    int offset();

    int size() default 0;

    SpanByteOrder order() default SpanByteOrder.BIG_ENDIAN;

    enum SpanByteOrder {
        BIG_ENDIAN, LITTLE_ENDIAN
    }
}
