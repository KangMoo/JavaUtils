package gcp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.kangmoo.utils.gcp.tts.TtsRecognizer;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author kangmoo Heo
 */
public class TtsTest {
    public static void main(String[] args) throws Exception {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("io.grpc.netty").setLevel(Level.INFO);
        // GOOGLE_APPLICATION_CREDENTIALS 환경변수 설정 필요
        // https://cloud.google.com/docs/authentication/getting-started?hl=ko
        TtsRecognizer ttsRecognizer = TtsRecognizer.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .setLanguageCode("ko-KR")
                .setSsmlGender(SsmlVoiceGender.NEUTRAL).build();


        byte[] result = ttsRecognizer.convertText("ABCD").toByteArray();


        File outputFile = new File("output.mp3");
        try (OutputStream out = new FileOutputStream(outputFile)) {
            out.write(result);
            System.out.println("Audio content written to file");
            System.out.println(outputFile.getAbsolutePath());
        }
    }
}
