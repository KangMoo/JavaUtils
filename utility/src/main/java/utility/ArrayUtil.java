package utility;

/**
 * @author kangmoo Heo
 */
public class ArrayUtil {
    public static byte[] getFilledByteArray(int size, byte ... bytes){
        byte[] temp = new byte[size];
        for(int i =0; i < size; i++){
            temp[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return temp;
    }

    public static int[] getFilledIntArray(int size, int ... bytes){
        int[] temp = new int[size];
        for(int i =0; i < size; i++){
            temp[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return temp;
    }

    public static String[] getFilledStringArray(int size, String ... bytes){
        String[] temp = new String[size];
        for(int i =0; i < size; i++){
            temp[i] = i<bytes.length ? bytes[i] : bytes[bytes.length-1];
        }
        return temp;
    }
}
