package com.lvbby.codema.java.tool;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lipeng on 2017/8/20.
 */
public class MavenDirectoryScanner {

    //maven项目根路径
    private List<File> mavenDirectories = Lists.newLinkedList();
    //maven源码根路径
    private List<File> mavenSrcDirectories = Lists.newLinkedList();

    public MavenDirectoryScanner(List<File> roots, int maxDepth) {
        if (CollectionUtils.isNotEmpty(roots)) {
            for (File root : roots) {
                scanMaven(root, mavenDirectories, 1, maxDepth);
            }
            for (File mavenDirectory : mavenDirectories) {
                File file = new File(mavenDirectory, "src/main/java");
                if (file.isDirectory() && file.exists()) {
                    mavenSrcDirectories.add(file);
                }
            }
        }
    }

    public MavenDirectoryScanner(List<File> roots) {
        this(roots, 10);
    }

    public static boolean isMavenDirectory(File file) {
        return file != null && file.isDirectory() && file.exists() && new File(file, "pom.xml").exists();
    }


    public static List<File> scanMaven(File dir) {
        return scanMaven(dir, -1);
    }

    /***
     * 扫描maven项目
     * @param dir
     * @param maxDepth <0:不限制层数
     * @return
     */
    public static List<File> scanMaven(File dir, int maxDepth) {
        List<File> files = Lists.newLinkedList();
        scanMaven(dir, files, 1, maxDepth);
        return files;
    }

    public static void scanMaven(File dir, List<File> result, int depth, int maxDepth) {
        if (maxDepth < 0 || depth <= maxDepth) {
            if (isMavenDirectory(dir)) {
                result.add(dir);
            }
            if (depth < maxDepth) {
                File[] files = dir.listFiles();
                if (files != null) {
                    Arrays.stream(files).filter(file -> file.isDirectory()).forEach(file -> scanMaven(file, result, depth + 1, maxDepth));
                }
            }
        }
    }

    public List<File> getMavenDirectories() {
        return mavenDirectories;
    }

    public List<File> getMavenSrcDirectories() {
        return mavenSrcDirectories;
    }
}
