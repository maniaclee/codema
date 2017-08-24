package com.lvbby.codema.core.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.List;

/**
 * Created by lipeng on 17/8/22.
 */
public class FileUtils {
    private static final String USER_HOME_DIRECTORY = System.getProperty("user.home");
    private static final List<File> FILE_ROOTS = Lists.newArrayList(File.listRoots());

    public static boolean isRelativeFilePath(String file) {
        return isRelativeFilePath(new File(file));
    }

    public static boolean isRelativeFilePath(File file) {
        while (file.getParentFile() != null) {
            file = file.getParentFile();
        }
        return !FILE_ROOTS.contains(file);
    }

    /***
     * 如果sub是绝对路径就返回sub
     * @param parent
     * @param sub
     * @return
     */
    public static String parseFileWithParentAsString(String parent, String sub) {
        File file = parseFileWithParent(parent == null ? null : new File(parent), sub);
        return file == null ? null : file.getAbsolutePath();
    }

    public static File parseFileWithParent(File parent, String sub) {
        if (StringUtils.isBlank(sub))
            return null;
        if (!isRelativeFilePath(sub)) {
            return new File(sub);
        }
        if (parent != null) {
            return parseFile(parent, sub);
        }
        return null;
    }

    /***
     * merge file ， sub路径支持./和../
     * @param file
     * @param sub
     * @return
     */
    public static File parseFile(File file, String sub) {
        Validate.notNull(file, "file can't be null");
        if (!StringUtils.isBlank(sub)) {
            for (String s : sub.trim().split("/")) {
                switch (s) {
                    case ".":
                        continue;
                    case "..":
                        file = file.getParentFile();
                        break;
                    default:
                        file = new File(file, s);
                }
            }
        }
        return file;
    }

    /***
     * 解析路径，支持~ ../ ./
     * @param filePath
     * @return
     */
    public static File parseFileRoot(String filePath) {
        String path = filePath;
        if (path.startsWith("~")) {
            if (path.length() == 1) {
                return new File(USER_HOME_DIRECTORY);
            }
            path = USER_HOME_DIRECTORY + path.substring(1);
        }
        String[] split = path.split("/");
        File file = new File(split[0]);
        for (int i = 1; i < split.length; i++) {
            String s = split[i];
            switch (s) {
                case ".":
                    continue;
                case "..":
                    file = file.getParentFile();
                    break;
                default:
                    file = new File(file, s);
            }
        }
        return file;
    }

    public static File parseFile(String... files) {
        if (files == null || files.length == 0) {
            return null;
        }
        File file = parseFileRoot(files[0]);
        if (files.length > 1) {
            for (int i = 1; i < files.length; i++) {
                file = parseFile(file, files[i]);
            }
        }
        return file;
    }

    public static File buildFile(String... subDirs) {
        if (subDirs != null && subDirs.length > 0) {
            for (String subDir : subDirs) {
                if (StringUtils.isBlank(subDir)) {
                    return null;
                }
            }
            File result = new File(subDirs[0]);
            for (int i = 1; i < subDirs.length; i++) {
                result = new File(result, subDirs[i]);
            }
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(isRelativeFilePath(new File("/")));
        System.out.println(isRelativeFilePath(new File("/a/b")));
        System.out.println(isRelativeFilePath(new File("./a/b")));
        System.out.println(isRelativeFilePath(new File(".")));
        System.out.println(isRelativeFilePath(new File("./")));
        System.out.println(isRelativeFilePath(new File("../")));
        System.out.println(isRelativeFilePath(new File("../a")));
        System.out.println(isRelativeFilePath(new File("ab/sd")));
        System.out.println(isRelativeFilePath(new File("ab")));
    }
}
