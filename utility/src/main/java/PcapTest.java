import com.github.kangmoo.utils.pcap.EthernetFrame;
import com.github.kangmoo.utils.pcap.PacketPpi;
import com.github.kangmoo.utils.pcap.Pcap;
import com.github.kangmoo.utils.rtp.RtpPacket;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 *
 * @author kangmoo Heo
 */
public class PcapTest {
    public static void main(String[] args) {
        try {
            Pcap pcap = Pcap.fromFile(System.getProperty("user.dir") + "/pcma.pcap");
            List<byte[]> rtpG711Packets = pcap.packets().stream()
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
                            byte[] rtp = new byte[packetData.length - pos - 16];
                            System.arraycopy(packetData, pos, rtp, 0, packetData.length - pos - 16);
                            return new RtpPacket(rtp, rtp.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(rtpPacket -> rtpPacket.payload != null)
                    .filter(rtpPacket -> rtpPacket.version == 2)
                    .filter(rtpPacket -> rtpPacket.payloadType == 0 || rtpPacket.payloadType == 8 || rtpPacket.payloadType == 18)
                    .filter(rtpPacket -> rtpPacket.payloadSize == rtpPacket.payload.length)
                    .map(RtpPacket::getPacket)
                    .collect(Collectors.toList());
            System.out.println("Packets : " + pcap.packets().size());
            System.out.println("RTP Packets : " + rtpG711Packets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}