package com.lvbby.codema.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by lipeng on 17/8/22.
 */
public class FileUtils {

    /***
     * 组装file，返回的file只是组装起来的，不一定存在
     * @param file
     * @param subDirs
     * @return
     */
    public static File buildFile(File file, String... subDirs) {
        if (file != null && subDirs != null && subDirs.length > 0) {
            for (String subDir : subDirs) {
                if (StringUtils.isBlank(subDir)) {
                    return null;
                }
                file = new File(file, subDir);
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
}
