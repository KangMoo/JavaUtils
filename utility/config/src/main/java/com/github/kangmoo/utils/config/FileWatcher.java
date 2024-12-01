package com.github.kangmoo.utils.config;

import com.github.kangmoo.utils.config.yaml.YamlConfigInjector;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author kangmoo Heo
 */

@Slf4j
public class FileWatcher {
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Path filePath;
    private final Path directoryPath;
    private ScheduledFuture<?> schedule;

    // 감시할 파일 경로 설정
    public FileWatcher(Path filePath) {
        this.filePath = filePath;
        this.directoryPath = filePath.getParent();
    }

    public void startWatch(Runnable onChanged) {
        // WatchService 인스턴스 생성
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            // 디렉토리 경로에 WatchService를 등록하고, 파일 생성, 수정, 삭제 이벤트를 감지
            this.directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            log.info("Watching File:{}", filePath);

            // 일정 주기마다 파일 감시 작업 실행
            schedule = executorService.scheduleAtFixedRate(() -> {
                try {
                    // 이벤트가 발생할 때까지 대기하지 않고, 이벤트가 없으면 null 반환
                    WatchKey watchKey = watchService.poll();

                    if (watchKey != null) {
                        // 발생한 이벤트 처리
                        for (WatchEvent<?> event : watchKey.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            Path eventPath = directoryPath.resolve((Path) event.context());

                            // 이벤트와 관련된 파일이 원하는 파일인지 확인
                            if (eventPath.equals(filePath)) {
                                log.info("File changed. Event:{}, File:{}", kind, eventPath);
                                onChanged.run();
                            }
                        }

                        // 키를 리셋하여 다음 이벤트를 준비
                        watchKey.reset();
                    }
                } catch (Exception e) {
                    log.warn("Err Occurs while watching file", e);
                }
            }, 0, 1, TimeUnit.SECONDS);// 1초 간격으로 파일 감시 작업 실행

        } catch (IOException e) {
            log.warn("Err Occurs while watching file", e);
        }
    }

    public static void startWatchConfig(Object configClass, String filePath) throws IOException, NoSuchFieldException {
        YamlConfigInjector.inject(configClass, filePath);
        new FileWatcher(Path.of(filePath)).startWatch(() -> {
            try {
                YamlConfigInjector.inject(configClass, filePath);
            } catch (Exception e) {
                log.warn("Err Occrus", e);
            }
        });
    }

    public void stopWatching(){
        if(schedule == null) return;
        schedule.cancel(false);
    }
}