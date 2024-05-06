package com.github.kangmoo.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileLock {

    private static void processLock(String name) {
        Path lockFilePath = Path.of(System.getProperty("java.io.tmpdir")).resolve(name);

        try {
            Files.createFile(lockFilePath);
            lockFilePath.toFile().deleteOnExit();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.deleteIfExists(lockFilePath);
                } catch (IOException e) {
                    log.warn("Lock File Delete Error", e);
                }
            }));
            log.info("[{}] process start", name);
        } catch (IOException e) {
            log.error("[{}] process already running", name, e);
            System.exit(2);
        }
    }
}
