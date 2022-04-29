package com.github.kangmoo.utils.utility;

import org.slf4j.Logger;

import java.io.*;
import java.net.URL;

/**
 * @author kangmoo Heo
 */
public class Wget {
    public void downloadAsFile(String saveAsFile, String urlOfFile) throws IOException {
        try (InputStream httpIn = new BufferedInputStream(new URL(urlOfFile).openStream());
             OutputStream fileOutput = new FileOutputStream(saveAsFile);
             OutputStream bufferedOut = new BufferedOutputStream(fileOutput, 1024)) {

            byte[] data = new byte[1024];
            boolean fileComplete = false;
            int count = 0;
            while (!fileComplete) {
                count = httpIn.read(data, 0, 1024);
                if (count <= 0) {
                    fileComplete = true;
                } else {
                    bufferedOut.write(data, 0, count);
                }
            }
        }
    }
}