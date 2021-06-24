import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 *
 * @author kangmoo Heo
 */
public class LogFinder {
    private static final Pattern LOG_PATTERN = Pattern.compile("^\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}](.|\\n)*");
    private static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            readLineTest(args[0], "");
        } else if (args.length == 2) {
            readLineTest(args[0], args[1]);
        } else{
            System.out.println("사용법: args= 파일 [찾을 문자열]");
        }
    }

    public static void readLineTest(String filePath, String str) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String sLine;
                while ((sLine = inFile.readLine()) != null) {
                    if (LOG_PATTERN.matcher(sLine.trim()).matches()) {
                        if (sb.toString().contains(str)) System.out.println(sb.toString());
                        sb.setLength(0);
                    }
                    sb.append(sLine).append("\n");
                }
                if (sb.toString().contains(str)) System.out.println(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
