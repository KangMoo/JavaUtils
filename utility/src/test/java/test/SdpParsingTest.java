package test;

import murillo.sdp.*;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kangmoo Heo
 */
public class SdpParsingTest {
    @Test
    public void test(){
        SessionDescription sdp = new SessionDescription();
        Origin origin = new Origin("-", 0L, 0L, "IN", "IP4", "127.0.0.1");
        sdp.setOrigin(origin);
        sdp.setSessionName("test");
        sdp.addMedia(new MediaDescription("aduio", 0, "UDP/AVP"));
        SessionDescription cloned = sdp.clone();
        System.out.println(sdp.toString());
        System.out.println(cloned.toString());

        origin.setSessId(1);
        System.out.println(sdp.toString());
        System.out.println(cloned.toString());

        try {
            sdp = SessionDescription.Parse("v=0\r\n" +
                    "o=- 1620309702 1 IN IP4 172.22.30.21\r\n" +
                    "s=-\r\n" +
                    "c=IN IP4 172.22.30.39\r\n" +
                    "b=AS:64\r\n" +
                    "t=0 0\r\n" +
                    "a=csup:avf-v0\r\n" +
                    "a=avf:avc=n prio=n\r\n" +
                    "m=audio 61308 RTP/AVP 0 101\r\n" +
                    "a=sendrecv\r\n" +
                    "a=rtpmap:101 telephone-event/8000\r\n" +
                    "a=ptime:20\r\n");
            MediaDescription datachannel = sdp.getMedias().get(0);
            SCTPMapAttribute sctpmap = (SCTPMapAttribute)datachannel.getAttributes("sctpmap").get(0);
            System.out.println(sctpmap.toString());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SessionDescription.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserException ex) {
            Logger.getLogger(SessionDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
