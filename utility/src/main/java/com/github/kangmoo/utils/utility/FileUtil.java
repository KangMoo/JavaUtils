package com.github.kangmoo.utils.utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.attribute.PosixFilePermission.*;

/**
 * 파일을 읽고 String으로 변환해주는 유틸
 */
public class FileUtil {

    private FileUtil() {
    }

    public static boolean setPermission(String target, int permissionNumber) throws IOException {
        return setPermission(Paths.get(target), permissionNumber);
    }

    /**
     * @param target target file path
     * @param permissionNumber integer representing a set of permissions. (ex. 766)
     * @return success or not
     */
    public static boolean setPermission(Path target, int permissionNumber) throws IOException {
        Set<PosixFilePermission> permissions = new HashSet<>();
        int perm = permissionNumber % 10;
        if ((perm & 1) > 0) permissions.add(OTHERS_EXECUTE);
        if ((perm & 2) > 0) permissions.add(OTHERS_WRITE);
        if ((perm & 4) > 0) permissions.add(OTHERS_READ);
        permissionNumber /= 10;
        perm = permissionNumber % 10;
        if ((perm & 1) > 0) permissions.add(GROUP_EXECUTE);
        if ((perm & 2) > 0) permissions.add(GROUP_WRITE);
        if ((perm & 4) > 0) permissions.add(GROUP_READ);
        permissionNumber /= 10;
        perm = permissionNumber % 10;
        if ((perm & 1) > 0) permissions.add(OWNER_EXECUTE);
        if ((perm & 2) > 0) permissions.add(OWNER_WRITE);
        if ((perm & 4) > 0) permissions.add(OWNER_READ);
        Files.setPosixFilePermissions(target, permissions);
        return true;
    }

    public static boolean setPermission(String target, String permissionStr) throws IOException {
        return setPermission(Paths.get(target), permissionStr);
    }

    /**
     * @param target target file path
     * @param permissionStr string representing a set of permissions. (ex. "rwxr-x---")
     * @return success or not
     */
    public static boolean setPermission(Path target, String permissionStr) throws IOException {
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(permissionStr);
        Files.setPosixFilePermissions(target, permissions);
        return true;
    }
}
