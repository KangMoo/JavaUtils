/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * SIP SDP Info
 *
 * @file SdpInfo.java
 * @author Tony Lim
 */

package com.github.kangmoo.utils.sdp;

import gov.nist.javax.sdp.SessionDescriptionImpl;
import gov.nist.javax.sdp.fields.AttributeField;
import gov.nist.javax.sdp.parser.SDPAnnounceParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sdp.MediaDescription;
import javax.sdp.SdpException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Slf4j
public class AudioSdpInfo {

    private SessionDescriptionImpl sdp;
    private MediaDescription md;
    private String ip;
    private int port;

    @SuppressWarnings("unchecked")
    public AudioSdpInfo(String sdpMsg) throws ParseException, SdpException {
        if (sdpMsg == null || sdpMsg.isEmpty()) throw new NullPointerException("SDP Message is blank");
        this.sdp = new SDPAnnounceParser(sdpMsg).parse();
        for (MediaDescription mediaDescription : ((List<MediaDescription>) sdp.getMediaDescriptions(false))) {
            if (mediaDescription.getMedia().getMediaType().equals("audio")) {
                this.md = mediaDescription;
                break;
            }
        }
        if (this.md == null) {
            throw new SdpException("Wrong Sdp");
        }

        this.ip = Optional.ofNullable(md.getConnection()).orElse(sdp.getConnection()).getAddress();
        this.port = md.getMedia().getMediaPort();
    }

    public AudioSdpInfo setSdp(SessionDescriptionImpl sdp) {
        this.sdp = sdp;
        return this;
    }

    public AudioSdpInfo setMd(MediaDescription md) {
        this.md = md;
        return this;
    }

    public AudioSdpInfo setIp(String ip) {
        this.ip = ip;
        try {
            sdp.getOrigin().setAddress(ip);
            Optional.ofNullable(this.md.getConnection()).orElse(this.sdp.getConnection()).setAddress(ip);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public AudioSdpInfo setPort(int port) {
        this.port = port;
        try {
            this.md.getMedia().setMediaPort(port);
        } catch (SdpException e) {
            log.warn("Err Occurs while setting Port", e);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public List<AttributeField> getAttributeFields() {
        return md.getAttributes(true);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<AttributeField> getCopiedAttributeFields() {
        return new ArrayList<>(md.getAttributes(true));
    }

    @Override
    public String toString() {
        return sdp.toString();
    }
}
