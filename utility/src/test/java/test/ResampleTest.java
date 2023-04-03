package test;

import com.github.kangmoo.utils.codec.pcm.WaveFile;
import com.github.kangmoo.utils.codec.pcm.audioresampler.audio.ResamplingFilter;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author kangmoo Heo
 */
public class ResampleTest {

    @Test
    public void test() throws Exception {
        String filePath = "/Users/heokangmoo/temp/lpcm_16k.wav";
        double wavSize = new File(filePath).length();
        try (FileInputStream fi = new FileInputStream(filePath);
             FileOutputStream fo = new FileOutputStream(filePath.replace(".wav", "_down_sampled.wav"));
             FileOutputStream fo2 = new FileOutputStream(filePath.replace(".wav", "_up_sampled.wav"))) {
            byte[] header = new byte[44];
            fi.read(header);

            fo.write(WaveFile.createWavHeader(8000, 1, 16, (int) wavSize - 44));
            fo2.write(WaveFile.createWavHeader(16000, 1, 16, (int) wavSize * 2 - 44));
            byte[] datas = new byte[640];
            ResamplingFilter downSampler = new ResamplingFilter(16000,8000);
            ResamplingFilter upSampler = new ResamplingFilter(8000, 16000);
            while (fi.read(datas) == 640) {
                // byte[] downSampled = resample16kTo8k(datas);

                byte[] downSampled = downSampler.process(datas);
                fo.write(downSampled);

                byte[] upSampled = upSampler.process(downSampled);
                fo2.write(upSampled);
            }
        }

    }
}
