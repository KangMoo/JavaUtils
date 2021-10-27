
import org.junit.Test;
import org.restcomm.media.sdp.SdpException;
import org.restcomm.media.sdp.SessionDescription;
import org.restcomm.media.sdp.SessionDescriptionParser;
import org.restcomm.media.sdp.attributes.RtpMapAttribute;
import org.restcomm.media.sdp.fields.MediaDescriptionField;
import org.restcomm.media.sdp.format.AVProfile;
import org.restcomm.media.sdp.format.RTPFormat;
import org.restcomm.media.sdp.format.RTPFormats;
import org.restcomm.media.spi.format.FormatFactory;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author kangmoo Heo
 */
public class SdpNegoTest {
    private static final Logger logger = getLogger(SdpNegoTest.class);

    @Test
    public void test() throws SdpException {
        SessionDescription sdp = SessionDescriptionParser.parse(SDP_PCMU);
        logger.debug("Before Nego\n{}", sdp);

        RTPFormats supportedFormats = new RTPFormats(){{
            add(new RTPFormat(0, FormatFactory.createAudioFormat("pcmu", 8000, 8, 1), 8000));
            add(new RTPFormat(101, FormatFactory.createAudioFormat("telephone-event", 8000, 8, 1), 8000));
        }};
        RTPFormats negotiatedFormats = negotiateFormats(supportedFormats, sdp.getMediaDescription("audio"));

        RtpMapAttribute[] attributes = Arrays.stream(negotiatedFormats.toArray())
                .filter(rtpformat -> sdp.getMediaDescription("audio").containsFormat(rtpformat.getID()))
                .map(rtpFormat ->  new RtpMapAttribute(rtpFormat.getID(), rtpFormat.getFormat().getName().toString(), rtpFormat.getClockRate(), 1))
                .toArray(RtpMapAttribute[]::new);

        for(RtpMapAttribute tt : attributes){
            logger.debug("TTTT {} ", tt.toString());
        }

        int[] payloads = Arrays.stream(negotiatedFormats.toArray())
                .map(RTPFormat::getID)
                .filter(id -> sdp.getMediaDescription("audio").containsPayloadType(id))
                .mapToInt(Integer::intValue)
                .toArray();

        sdp.getMediaDescription("audio").setFormats(attributes);
        sdp.getMediaDescription("audio").setPayloadTypes(payloads);

        logger.debug("After Nego\n{}", sdp);
    }

    public static final String SDP_PCMU = "v=0\r\n" +
            "o=- 1620309702 1 IN IP4 127.0.0.1\r\n" +
            "s=-\r\n" +
            "c=IN IP4 127.0.0.1\r\n" +
            "b=AS:64\r\n" +
            "t=0 0\r\n" +
            "a=csup:avf-v0\r\n" +
            "a=avf:avc=n prio=n\r\n" +
            "m=audio 5060 RTP/AVP 0 8 101\r\n" +
            "a=sendrecv\r\n" +
            "a=rtpmap:101 telephone-event/8000\r\n" +
            "a=ptime:20";



    public RTPFormats negotiateFormats(RTPFormats supportedFormats, MediaDescriptionField media) {
        // Clean currently offered formats
        RTPFormats offeredFormats = new RTPFormats();
        RTPFormats negotiatedFormats = new RTPFormats();

        // Map payload types to RTP Format
        for (int payloadType : media.getPayloadTypes()) {
            RTPFormat format;
            try {
                if(payloadType < 96 || payloadType > 127) {
                    // static payload type
                    format = AVProfile.getFormat(payloadType, AVProfile.AUDIO);
                } else {
                    // dynamic payload type
                    final RtpMapAttribute codecSdp = media.getFormat(payloadType);
                    final RTPFormat staticFormat = AVProfile.getFormat(codecSdp.getPayloadType());

                    // Check if code is supported
                    final boolean supported = staticFormat != null && staticFormat.getClockRate() == codecSdp.getClockRate();
                    if(supported) {
                        format = new RTPFormat(payloadType, staticFormat.getFormat(), staticFormat.getClockRate());
                    } else {
                        format = null;
                    }
                }
            } catch (NumberFormatException e) {
                format = null;
            }

            if(format != null) {
                offeredFormats.add(format);
            }
        }

        // Negotiate the formats and store intersection
        negotiatedFormats.clean();
        supportedFormats.intersection(offeredFormats, negotiatedFormats);

        // Apply formats
        logger.debug("Support formats : {}", supportedFormats);
        logger.debug("Offered formats : {}", offeredFormats);
        logger.debug("negotiated formats : {}", negotiatedFormats);
        return negotiatedFormats;
    }
}