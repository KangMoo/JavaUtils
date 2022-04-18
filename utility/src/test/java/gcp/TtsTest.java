package gcp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.kangmoo.utils.gcp.tts.TtsConverter;
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

        // 라이브러리를 사용하여 TTS 인스턴스 생성
        TtsConverter ttsConverter = TtsConverter.newBuilder()
                // 코덱 선택
                .setAudioEncoding(AudioEncoding.MP3)
                // 샘플링 레이트 설정
                .setSampleRateHertz(16000)
                // 언어 선택
                .setLanguageCode("ko-KR")
                // 목소리 성별 선택
                .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                .build();

        // TTS 명령을 호출하여 결과를 byte array로 저장
        byte[] result = ttsConverter.convertText("ABCD").toByteArray();

        // 결과를 파일로 저장하여 청취
        File outputFile = new File("output.mp3");
        try (OutputStream out = new FileOutputStream(outputFile)) {
            out.write(result);
            System.out.println("Audio content written to file");
            System.out.println(outputFile.getAbsolutePath());
        }
    }
}
