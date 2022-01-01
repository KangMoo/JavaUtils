package com.github.kangmoo.utils.wget;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author kangmoo Heo
 */
public class Wget {

    public static WgetStatus wGet(String saveAsFile, String urlOfFile) {
        try (InputStream httpIn = new BufferedInputStream(new URL(urlOfFile).openStream());
             OutputStream fileOutput = new FileOutputStream(saveAsFile);
             OutputStream bufferedOut = new BufferedOutputStream(fileOutput, 1024)) {

            byte[] data = new byte[1024];
            boolean fileComplete = false;
            int count;
            while (!fileComplete) {
                count = httpIn.read(data, 0, 1024);
                if (count <= 0) {
                    fileComplete = true;
                } else {
                    bufferedOut.write(data, 0, count);
                }
            }
        } catch (MalformedURLException e) {
            return WgetStatus.MalformedUrl;
        } catch (IOException e) {
            return WgetStatus.IoException;
        }
        return WgetStatus.Success;
    }

}