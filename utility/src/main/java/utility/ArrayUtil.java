package utility;

/**
 * @author kangmoo Heo
 */
public class ArrayUtil {
    public static byte[] getFilledByteArray(int size, byte ... bytes){
        byte[] ret = new byte[size];
        for(int i =0; i < size; i++){
            ret[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return ret;
    }

    public static int[] getFilledIntArray(int size, int ... bytes){
        int[] ret = new int[size];
        for(int i =0; i < size; i++){
            ret[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return ret;
    }

    public static String[] getFilledStringArray(int size, String ... bytes){
        String[] ret = new String[size];
        for(int i =0; i < size; i++){
            ret[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return ret;
    }
}
