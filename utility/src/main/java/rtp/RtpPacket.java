package rtp;

import java.util.Arrays;

/**
 *
 * @author kangmoo Heo
 */

public class RtpPacket {

    //size of the RTP header:
    static int HEADER_SIZE = 12;

    //Fields that compose the RTP header
    public int version;
    public int padding;
    public int extension;
    public int cc;
    public int marker;
    public int payloadType;
    public int sequenceNumber;
    public int timeStamp;
    public long ssrc;

    //Bitstream of the RTP header
    public byte[] header;

    //size of the RTP payload
    public int payloadSize;
    //Bitstream of the RTP payload
    public byte[] payload;

    //--------------------------
    //Constructor of an RTPpacket object from header fields and payload bitstream
    //--------------------------
    public RtpPacket(int pType, int seqNo, int timeStamp, byte[] data, int dataLength, long ssrc) {
        //fill by default header fields:
        this.version = 2;
        this.padding = 0;
        this.extension = 0;
        this.cc = 0;
        this.marker = 0;
        this.ssrc = ssrc;    // Identifies the server

        //fill changing header fields:
        this.sequenceNumber = seqNo;
        this.timeStamp = timeStamp;
        this.payloadType = pType;

        //build the header bistream:
        this.header = new byte[HEADER_SIZE];

        //fill the header array of byte with RTP header fields
        this.header[0] = (byte) (this.version << 6 | this.padding << 5 | this.extension << 4 | cc);
        this.header[1] = (byte) (this.marker << 7 | this.payloadType & 0x000000FF);
        this.header[2] = (byte) (this.sequenceNumber >> 8);
        this.header[3] = (byte) (this.sequenceNumber & 0xFF);
        this.header[4] = (byte) (this.timeStamp >> 24);
        this.header[5] = (byte) (this.timeStamp >> 16);
        this.header[6] = (byte) (this.timeStamp >> 8);
        this.header[7] = (byte) (this.timeStamp & 0xFF);
        this.header[8] = (byte) (this.ssrc >> 24);
        this.header[9] = (byte) (this.ssrc >> 16);
        this.header[10] = (byte) (this.ssrc >> 8);
        this.header[11] = (byte) (this.ssrc & 0xFF);

        //fill the payload bitstream:
        this.payloadSize = dataLength;
        this.payload = new byte[dataLength];

        //fill payload array of byte from data (given in parameter of the constructor)
        this.payload = Arrays.copyOf(data, this.payloadSize);
    }

    //--------------------------
    //Constructor of an RTPpacket object from the packet bistream
    //--------------------------
    public RtpPacket(byte[] packet, int packetSize) {
        //fill default fields:
        this.version = 2;
        this.padding = 0;
        this.extension = 0;
        this.cc = 0;
        this.marker = 0;
        this.ssrc = 0;

        //check if total packet size is lower than the header size
        if (packetSize >= HEADER_SIZE) {
            //get the header bitsream:
            this.header = new byte[HEADER_SIZE];
            this.header = Arrays.copyOf(packet,packet.length);

            //get the payload bitstream:
            payloadSize = packetSize - HEADER_SIZE;
            payload = new byte[payloadSize];
            for (int i = HEADER_SIZE; i < packetSize; i++)
                payload[i - HEADER_SIZE] = packet[i];

            //interpret the changing fields of the header:
            version = (header[0] & 0xFF) >>> 6;
            payloadType = header[1] & 0x7F;
            sequenceNumber = (header[3] & 0xFF) + ((header[2] & 0xFF) << 8);
            timeStamp = (header[7] & 0xFF) + ((header[6] & 0xFF) << 8) + ((header[5] & 0xFF) << 16) + ((header[4] & 0xFF) << 24);
            ssrc = ((long) (header[8] & 0xFF) << 24) |
                    ((long) (header[8+ 1] & 0xFF) << 16) |
                    ((long) (header[8+ 2] & 0xFF) << 8) |
                    ((long) (header[8+ 3] & 0xFF));
        }
    }

    //--------------------------
    //getpayload: return the payload bistream of the RTPpacket and its size
    //--------------------------
    public int getPayload(byte[] data) {

        for (int i = 0; i < payloadSize; i++)
            data[i] = payload[i];

        return (payloadSize);
    }

    public byte[] getPayload() {
        byte[] res = new byte[payloadSize];
        Arrays.copyOf(payload, payloadSize);
        System.arraycopy(payload, 0, res, 0, payloadSize);
        return res;
    }

    //--------------------------
    //getpayload_length: return the length of the payload
    //--------------------------
    public int getPayloadLength() {
        return (payloadSize);
    }

    //--------------------------
    //getlength: return the total length of the RTP packet
    //--------------------------
    public int getLength() {
        return (payloadSize + HEADER_SIZE);
    }

    //--------------------------
    //getpacket: returns the packet bitstream and its length
    //--------------------------
    public int getPacket(byte[] packet) {
        //construct the packet = header + payload
        for (int i = 0; i < HEADER_SIZE; i++)
            packet[i] = header[i];
        for (int i = 0; i < payloadSize; i++)
            packet[i + HEADER_SIZE] = payload[i];

        //return total size of the packet
        return (payloadSize + HEADER_SIZE);
    }

    public byte[] getPacket() {
        byte[] res = new byte[HEADER_SIZE + payloadSize];
        System.arraycopy(header, 0, res, 0, HEADER_SIZE);
        System.arraycopy(payload, 0, res, HEADER_SIZE, payloadSize);
        return res;
    }

    //--------------------------
    //gettimestamp
    //--------------------------

    public int getTimeStamp() {
        return (timeStamp);
    }

    //--------------------------
    //getsequencenumber
    //--------------------------
    public int getSequenceNumber() {
        return (sequenceNumber);
    }

    //--------------------------
    //getpayloadtype
    //--------------------------
    public int getPayloadType() {
        return (payloadType);
    }

    public long getSsrc() {
        return ssrc;
    }

    public String toString() {
        return "Version: " + version
                + ", Padding: " + padding
                + ", Extension: " + extension
                + ", CC: " + cc
                + ", Marker: " + marker
                + ", PayloadType: " + payloadType
                + ", SequenceNumber: " + sequenceNumber
                + ", TimeStamp: " + timeStamp
                + ", SSRC: " + ssrc;
    }
}
