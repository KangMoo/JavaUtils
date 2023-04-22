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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SdpUtil {
    private static final String RTPMAP = "rtpmap";
    private static final String FMTP = "fmtp";

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

    public static List<Integer> getPayloadIds(SdpInfo sdpInfo) {
        try {
            return ((Stream<String>) sdpInfo.getMd().getMedia().getMediaFormats(false).stream())
                    .map(Integer::parseInt).collect(Collectors.toList());
        } catch (SdpParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getDtmfPayloadIds(SdpInfo sdpInfo) {
        return SdpUtil.findAttr(sdpInfo.getCopiedAttributeFields(), o -> o.getValue().contains("telephone-event/"))
                .stream().map(AttributeField::getAttribute)
                .map(o -> Integer.parseInt(o.getValue().split("\\s")[0]))
                .collect(Collectors.toList());
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

    public static List<AttributeField> getFilteredAttributes(List<AttributeField> attributeFields, List<Integer> payloadIds) {
        return attributeFields.stream()
                .filter(attributeField -> {
                    String attrName = attributeField.getAttribute().getName();
                    if (!RTPMAP.equals(attrName) && !FMTP.equals(attrName)) return true;
                    int payloadId = Integer.parseInt(attributeField.getAttribute().getValue().split("\\s")[0]);
                    return payloadIds.contains(payloadId);
                })
                .collect(Collectors.toList());
    }

    public static Optional<Integer> findPayloadIdFromRtpMap(SdpInfo sdpInfo, Predicate<String> filter) {
        Set<Integer> mdFormats = SdpUtil.getPayloadIds(sdpInfo).stream().collect(Collectors.toSet());
        return sdpInfo.getCopiedAttributeFields().stream()
                .map(AttributeField::getAttribute)
                .filter(o -> o.getName().equals(RTPMAP))
                .filter(o -> filter.test(o.getValue()))
                .map(o -> Integer.parseInt(o.getValue().split("\\s")[0]))
                .filter(mdFormats::contains)
                .findAny();
    }
}
