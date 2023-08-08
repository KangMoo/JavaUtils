package com.github.kangmoo.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.attribute.PosixFilePermission.*;

/**
 *
 * @author kangmoo Heo
 */
@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    public static void removeFileWithLog(Path filePath) {
        removeFileWithLog(filePath.toFile());
    }

    public static void removeFileWithLog(String file) {
        removeFileWithLog(new File(file));
    }

    public static void removeFileWithLog(File file) {
        try {
            Files.delete(file.toPath());
            log.debug("Success to remove. (file={})", file.getAbsolutePath());
        } catch (NoSuchFileException e) {
            log.info("File not exist (file={})", file.getAbsolutePath(), e);
        } catch (Exception e) {
            log.warn("Fail to remove file. (file={})", file.getAbsolutePath(), e);
        }
    }

    public static int removeDirWithLog(Path path) {
        AtomicInteger count = new AtomicInteger();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    count.incrementAndGet();
                    removeFileWithLog(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    log.warn("Fail to visit file. [{}]", file, exc);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    count.incrementAndGet();
                    removeFileWithLog(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.warn("Fail to remove file. [{}]", path, e);
        }
        return count.get();
    }

    public static void copyFileWithDirectories(String sourceFilePath, String destinationFilePath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        // 대상 경로가 존재하지 않을 경우 재귀적으로 경로를 생성
        if (!Files.exists(destinationPath.getParent())) {
            Files.createDirectories(destinationPath.getParent());
        }

        Files.copy(sourcePath, destinationPath);
    }

    public static boolean setPermission(String target, int permissionNumber) {
        return setPermission(Paths.get(target), permissionNumber);
    }

    /**
     * @param target target file path
     * @param permissionNumber integer representing a set of permissions. (ex. 766)
     * @return success or not
     */
    public static boolean setPermission(Path target, int permissionNumber) {
        try {
            Set<PosixFilePermission> permissions = new HashSet<>();
            int temp = permissionNumber;
            int perm = temp % 10;
            if ((perm & 1) > 0) permissions.add(OTHERS_EXECUTE);
            if ((perm & 2) > 0) permissions.add(OTHERS_WRITE);
            if ((perm & 4) > 0) permissions.add(OTHERS_READ);
            temp /= 10;
            perm = temp % 10;
            if ((perm & 1) > 0) permissions.add(GROUP_EXECUTE);
            if ((perm & 2) > 0) permissions.add(GROUP_WRITE);
            if ((perm & 4) > 0) permissions.add(GROUP_READ);
            temp /= 10;
            perm = temp % 10;
            if ((perm & 1) > 0) permissions.add(OWNER_EXECUTE);
            if ((perm & 2) > 0) permissions.add(OWNER_WRITE);
            if ((perm & 4) > 0) permissions.add(OWNER_READ);
            Files.setPosixFilePermissions(target, permissions);
            log.debug("() () () Success to change the file's permission to {}. (target={})", permissionNumber, target.toAbsolutePath());
            return true;
        } catch (Exception e) {
            log.warn("Exception Occurs while setting permission [{} : {}]", target.toAbsolutePath(), permissionNumber, e);
            return false;
        }
    }

    public static boolean setPermission(String target, String permissionStr) {
        return setPermission(Paths.get(target), permissionStr);
    }

    /**
     * @param target target file path
     * @param permissionStr string representing a set of permissions. (ex. "rwxr-x---")
     * @return success or not
     */
    public static boolean setPermission(Path target, String permissionStr) {
        try {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(permissionStr);
            Files.setPosixFilePermissions(target, permissions);
            log.debug("() () () Success to change the file's permission to {}. (target={})", permissionStr, target.toAbsolutePath());
            return true;
        } catch (Exception e) {
            log.warn("Exception Occurs while setting permission [{} : {}]", target.toAbsolutePath(), permissionStr, e);
            return false;
        }
    }

    public static boolean isFileOlderThan(Instant comparisonDateTime, Path path) {
        try {
            Instant fileCreateTime = Files.readAttributes(path, BasicFileAttributes.class).creationTime().toInstant();
            return comparisonDateTime.isAfter(fileCreateTime);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmptyDirectory(Path path) {
        if (!Files.isDirectory(path)) return false;
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
            return !dirStream.iterator().hasNext();
        } catch (IOException e) {
            return false;
        }
    }
}