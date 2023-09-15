package com.github.kangmoo.utils.pcap;

import io.kaitai.struct.ByteBufferKaitaiStream;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kangmoo Heo
 */
public class PcapUtil {
    public static List<EthernetFrame> getEthernetHeaders(String pcapFilePath) throws IOException {
        return Pcap.fromFile(pcapFilePath).packets().stream()
                .map(packet -> {
                    try {
                        int pos;
                        byte[] packetData;
                        if (packet.body() instanceof EthernetFrame || packet.body() instanceof PacketPpi) {
                            pos = 0;
                            packetData = packet._raw_body();
                        } else {
                            pos = 2;
                            packetData = (byte[]) packet.body();
                        }
                        return new EthernetFrame(new ByteBufferKaitaiStream(subarray(packetData, pos, packetData.length)));
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<Ipv4Packet> getIpv4Packets(String pcapFilePath) throws IOException {
        return getEthernetHeaders(pcapFilePath).stream()
                .filter(ethernetFrame -> ethernetFrame._raw_body() != null)
                .map(e -> new Ipv4Packet(new ByteBufferKaitaiStream(e._raw_body()), e))
                .collect(Collectors.toList());
    }

    public static List<UdpDatagram> getUdpDatagrams(String pcapFilePath) throws IOException {
        return getEthernetHeaders(pcapFilePath).stream()
                .filter(ethernetFrame -> ethernetFrame._raw_body() != null)
                .map(e -> new Ipv4Packet(new ByteBufferKaitaiStream(e._raw_body()), e))
                .map(e -> new UdpDatagram(new ByteBufferKaitaiStream(e._raw_body()), e))
                .collect(Collectors.toList());
    }

    public static List<RtpPacket> getRtpPackets(String pcapFilePath) throws IOException {
        return getEthernetHeaders(pcapFilePath).stream()
                .filter(ethernetFrame -> ethernetFrame._raw_body() != null)
                .map(e -> new Ipv4Packet(new ByteBufferKaitaiStream(e._raw_body()), e))
                .map(e -> new UdpDatagram(new ByteBufferKaitaiStream(e._raw_body()), e))
                .map(e -> new RtpPacket(new ByteBufferKaitaiStream(e.body()), e))
                .collect(Collectors.toList());
    }

    private static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        } else {
            if (startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }

            if (endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }

            int newSize = endIndexExclusive - startIndexInclusive;
            if (newSize <= 0) {
                return new byte[0];
            } else {
                byte[] subarray = new byte[newSize];
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }
}
