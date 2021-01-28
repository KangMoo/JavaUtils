package utility;

import java.io.*;

/**
 * 파일을 읽고 String으로 변환해주는 유틸
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 파일 경로를 문자열로 받고, 경로에 해당하는 파일 읽은 후 내용을 문자열로 전달
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String filepathToString(String path) throws IOException {
        return fileToString(new File(path));
    }

    /**
     * 파일 읽은 후 내용을 문자열로 전달
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String fileToString(File file) throws IOException {
        return inputStreamToString(new FileInputStream(file));
    }

    /**
     * Stream -> String변환 함수
     *
     * @param is InputStream
     * @return String으로 캐스팅된 InputStream
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static File createFileWithDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if(file.getParentFile().mkdirs() && file.createNewFile()) return file;
        return null;
    }


    /**
     * 파일에 문자열을 덧붙인다.
     *
     * @param filePath 파일 경로
     * @param data     덧붙일 byte
     * @return
     */
    public static void fileWrite(String filePath, byte[] data, boolean append) throws IOException {
        File f = new File(filePath);
        // 파일이 없으면 경로 및 파일 생성
        if (!f.exists()) {
            FileUtil.createFileWithDirectory(filePath);
        }

        try(FileOutputStream writer = new FileOutputStream(f, append)){
            writer.write(data);
        }
    }


    public static void copy(File srcFile, File destFile) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(srcFile));

        try {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destFile));

            try {
                int c;
                while((c = input.read()) != -1) {
                    output.write(c);
                }

                output.flush();
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }

    }
}
