package com.github.kangmoo.utils.pcap;

import io.kaitai.struct.ByteBufferKaitaiStream;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author kangmoo Heo
 */
public class PcapUtil {
    public static List<RtpPacket> filterRtpFromPcap(String pcapFilePath) throws IOException {
        return Pcap.fromFile(pcapFilePath).packets().stream()
                .map(packet -> {
                    try {
                        int pos;
                        byte[] packetData;
                        if (packet.body() instanceof EthernetFrame || packet.body() instanceof PacketPpi) {
                            pos = 42;
                            packetData = packet._raw_body();
                        } else {
                            pos = 44;
                            packetData = (byte[]) packet.body();
                        }
                        byte[] rtp = new byte[packetData.length - pos];
                        System.arraycopy(packetData, pos, rtp, 0, packetData.length - pos);
                        return new RtpPacket(new ByteBufferKaitaiStream(rtp));
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(rtpPacket -> rtpPacket.version() == 2)
                .filter(rtpPacket -> rtpPacket.data() != null)
                .collect(Collectors.toList());
    }
}
