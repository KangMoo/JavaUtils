package gcp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.kangmoo.utils.gcp.stt.SttRecognizer;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author kangmoo Heo
 */
public class SttTest {
    public static void main(String[] args) throws Exception {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("io.grpc.netty").setLevel(Level.INFO);
        // GOOGLE_APPLICATION_CREDENTIALS 환경변수 설정 필요
        // https://cloud.google.com/docs/authentication/getting-started?hl=ko
        SttRecognizer sttRecognizer = SttRecognizer.newBuilder()
                .setEncoding(AudioEncoding.LINEAR16).setSampleRateHertz(8000).setLanguageCode("ko-KR")
                .setOnResponse(o -> System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("[mm:ss.SSS]")) + " : " +o))
                .build();
        sttRecognizer.start();

        new Thread(() -> mike2gcp(sttRecognizer, 5000)).start();

        Thread.sleep(5500);

        sttRecognizer.stop();
    }

    public static void mike2gcp(SttRecognizer sttRecognizer, int timeMs) {
        try {
            AudioFormat audioFormat = new AudioFormat(8000, 16, 1, true, false);
            Info targetInfo = new Info(TargetDataLine.class, audioFormat);

            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            long startTime = System.currentTimeMillis();
            System.out.println("Start speaking.");
            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[6400];
                audio.read(data);
                sttRecognizer.inputData(data);
                if (estimatedTime > timeMs) { // 5 seconds
                    System.out.println("Stop speaking.");
                    break;
                }
            }
            targetDataLine.stop();
            targetDataLine.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
