/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * SIP SDP Util
 *
 * @file SdpUtil.java
 * @author Tony Lim
 */

package com.github.kangmoo.utils.sdp;

import gov.nist.core.NameValue;
import gov.nist.javax.sdp.fields.AttributeField;

import javax.sdp.SdpParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SdpUtil {

    private SdpUtil() {
        // nothing
    }

    public static AttributeField newAttributeInstance(String key, String value) {
        return newAttributeInstance(new NameValue(key, value));
    }

    public static AttributeField newAttributeInstance(NameValue nameValue) {
        AttributeField attributeField = new AttributeField();
        attributeField.setAttribute(nameValue);
        return attributeField;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getPayloadIds(SdpInfo sdpInfo) {
        try {
            return new ArrayList<>(sdpInfo.getMd().getMedia().getMediaFormats(false));
        } catch (SdpParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPtime(SdpInfo sdpInfo){
        return sdpInfo.getAttributeFields().stream().map(AttributeField::getAttribute)
                .filter(o -> o.getName().equals("ptime"))
                .findAny().map(o -> Integer.parseInt(o.getValue())).orElse(20);
    }

    public static List<AttributeField> findRtpmapAttr(Collection<AttributeField> attrs, Predicate<String> valueFilter) {
        return findAttr(attrs, o -> o.getName().equals("rtpmap"), o -> valueFilter.test(o.getValue()));
    }

    public static List<AttributeField> findFmtpAttr(Collection<AttributeField> attrs, Predicate<String> valueFilter) {
        return findAttr(attrs, o -> o.getName().equals("fmtp"), o -> valueFilter.test(o.getValue()));
    }

    @SafeVarargs
    public static List<AttributeField> findAttr(Collection<AttributeField> attrs, Predicate<NameValue>... attrFilter) {
        Stream<AttributeField> stream = attrs.stream();
        for (Predicate<NameValue> filter : attrFilter) {
            stream = stream.filter(o ->
            {
                try {
                    return filter.test(o.getAttribute());
                } catch (Exception e) {
                    return false;
                }
            });
        }
        return stream.collect(Collectors.toList());
    }

    public static AttributeField makeAttribute(String name, String value){
        AttributeField attributeField = new AttributeField();
        attributeField.setAttribute(new NameValue(name, value));
        return attributeField;
    }

}
