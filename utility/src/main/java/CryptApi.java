public class CryptApi {
    private static final String IPP_LIBRARY_NAME = "ippcp";
    private static final String CRYPT_LIBRARY_NAME = "icrypt";

    static {
        System.loadLibrary(IPP_LIBRARY_NAME);
        System.loadLibrary(CRYPT_LIBRARY_NAME);
    }

    public static native void init();
    public static native int encrypt(String srcFilename, String dstFilename, String key);
    public static native int decrypt(String srcFilename, String dstFilename, String key);
}