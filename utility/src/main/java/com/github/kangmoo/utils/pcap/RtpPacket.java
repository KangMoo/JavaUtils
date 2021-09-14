package com.github.kangmoo.utils.pcap;

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStream;
import io.kaitai.struct.KaitaiStruct;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kangmoo Heo
 */
public class RtpPacket extends KaitaiStruct {
    public static RtpPacket fromFile(String fileName) throws IOException {
        return new RtpPacket(new ByteBufferKaitaiStream(fileName));
    }

    public enum PayloadTypeEnum {
        PCMU(0),
        RESERVED1(1),
        RESERVED2(2),
        GSM(3),
        G723(4),
        DVI4_1(5),
        DVI4_2(6),
        LPC(7),
        PAMA(8),
        G722(9),
        L16_1(10),
        L16_2(11),
        QCELP(12),
        CN(13),
        MPA(14),
        G728(15),
        DVI4_3(16),
        DVI4_4(17),
        G729(18),
        RESERVED19(19),
        UNASSIGNED20(20),
        UNASSIGNED21(21),
        UNASSIGNED22(22),
        UNASSIGNED23(23),
        UNASSIGNED24(24),
        CELB(25),
        JPEG(26),
        UNASSIGNED27(27),
        NV(28),
        UNASSIGNED29(29),
        UNASSIGNED30(30),
        H261(31),
        MPV(32),
        MP2T(33),
        H263(34),
        MPEG_PS(96),
        DYNAMIC_96(96),
        DYNAMIC_97(97),
        DYNAMIC_98(98),
        DYNAMIC_99(99),
        DYNAMIC_100(100),
        DYNAMIC_101(101),
        DYNAMIC_102(102),
        DYNAMIC_103(103),
        DYNAMIC_104(104),
        DYNAMIC_105(105),
        DYNAMIC_106(106),
        DYNAMIC_107(107),
        DYNAMIC_108(108),
        DYNAMIC_109(109),
        DYNAMIC_110(110),
        DYNAMIC_111(111),
        DYNAMIC_112(112),
        DYNAMIC_113(113),
        DYNAMIC_114(114),
        DYNAMIC_115(115),
        DYNAMIC_116(116),
        DYNAMIC_117(117),
        DYNAMIC_118(118),
        DYNAMIC_119(119),
        DYNAMIC_120(120),
        DYNAMIC_121(121),
        DYNAMIC_122(122),
        DYNAMIC_123(123),
        DYNAMIC_124(124),
        DYNAMIC_125(125),
        DYNAMIC_126(126),
        DYNAMIC_127(127);

        private final long id;
        PayloadTypeEnum(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, PayloadTypeEnum> byId = new HashMap<Long, PayloadTypeEnum>(36);
        static {
            for (PayloadTypeEnum e : PayloadTypeEnum.values())
                byId.put(e.id(), e);
        }
        public static PayloadTypeEnum byId(long id) { return byId.get(id); }
    }

    public RtpPacket(KaitaiStream _io) {
        this(_io, null, null);
    }

    public RtpPacket(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public RtpPacket(KaitaiStream _io, KaitaiStruct _parent, RtpPacket _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
        this.version = this._io.readBitsIntBe(2);
        this.hasPadding = this._io.readBitsIntBe(1) != 0;
        this.hasExtension = this._io.readBitsIntBe(1) != 0;
        this.csrcCount = this._io.readBitsIntBe(4);
        this.marker = this._io.readBitsIntBe(1) != 0;
        this.payloadType = PayloadTypeEnum.byId(this._io.readBitsIntBe(7));
        this.payloadTypeNum = this._io.readBitsIntBe(7);
        this._io.alignToByte();
        this.sequenceNumber = this._io.readU2be();
        this.timestamp = this._io.readU4be();
        this.ssrc = this._io.readU4be();
        if (hasExtension()) {
            this.headerExtension = new HeaderExtention(this._io, this, _root);
        }
        this.data = this._io.readBytes(((_io().size() - _io().pos()) - lenPadding()));
        this.padding = this._io.readBytes(lenPadding());
    }
    public static class HeaderExtention extends KaitaiStruct {
        public static HeaderExtention fromFile(String fileName) throws IOException {
            return new HeaderExtention(new ByteBufferKaitaiStream(fileName));
        }

        public HeaderExtention(KaitaiStream _io) {
            this(_io, null, null);
        }

        public HeaderExtention(KaitaiStream _io, RtpPacket _parent) {
            this(_io, _parent, null);
        }

        public HeaderExtention(KaitaiStream _io, RtpPacket _parent, RtpPacket _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.id = this._io.readU2be();
            this.length = this._io.readU2be();
        }
        private int id;
        private int length;
        private RtpPacket _root;
        private RtpPacket _parent;
        public int id() { return id; }
        public int length() { return length; }
        public RtpPacket _root() { return _root; }
        public RtpPacket _parent() { return _parent; }
    }
    private Integer lenPaddingIfExists;

    /**
     * If padding bit is enabled, last byte of data contains number of
     * bytes appended to the payload as padding.
     */
    public Integer lenPaddingIfExists() {
        if (this.lenPaddingIfExists != null)
            return this.lenPaddingIfExists;
        if (hasPadding()) {
            long _pos = this._io.pos();
            this._io.seek((_io().size() - 1));
            this.lenPaddingIfExists = this._io.readU1();
            this._io.seek(_pos);
        }
        return this.lenPaddingIfExists;
    }
    private Integer lenPadding;

    /**
     * Always returns number of padding bytes to in the payload.
     */
    public Integer lenPadding() {
        if (this.lenPadding != null)
            return this.lenPadding;
        int _tmp = (int) ((hasPadding() ? lenPaddingIfExists() : 0));
        this.lenPadding = _tmp;
        return this.lenPadding;
    }
    private long version;
    private boolean hasPadding;
    private boolean hasExtension;
    private long csrcCount;
    private boolean marker;
    private PayloadTypeEnum payloadType;
    private long payloadTypeNum;
    private int sequenceNumber;
    private long timestamp;
    private long ssrc;
    private HeaderExtention headerExtension;
    private byte[] data;
    private byte[] padding;
    private RtpPacket _root;
    private KaitaiStruct _parent;
    public long version() { return version; }
    public boolean hasPadding() { return hasPadding; }
    public boolean hasExtension() { return hasExtension; }
    public long csrcCount() { return csrcCount; }
    public boolean marker() { return marker; }
    public PayloadTypeEnum payloadType() { return payloadType; }
    public long getPayloadTypeNum() { return payloadTypeNum; }
    public int sequenceNumber() { return sequenceNumber; }
    public long timestamp() { return timestamp; }
    public long ssrc() { return ssrc; }
    public HeaderExtention headerExtension() { return headerExtension; }

    /**
     * Payload without padding.
     */
    public byte[] data() { return data; }
    public byte[] padding() { return padding; }
    public RtpPacket _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
