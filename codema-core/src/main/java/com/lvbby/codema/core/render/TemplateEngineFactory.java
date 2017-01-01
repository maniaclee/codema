package com.lvbby.codema.core.render;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Created by peng on 16/8/3.
 */
public class TemplateEngineFactory {


    public static TemplateEngine create(Class<? extends TemplateEngine> clz, File src) {
        try {
            return create(clz, IOUtils.toString(new FileInputStream(src)));
        } catch (IOException e) {
            throw new RuntimeException("failed to read file :" + src, e);
        }
    }

    public static TemplateEngine create(Class<? extends TemplateEngine> clz, InputStream inputStream) {
        try {
            return create(clz, IOUtils.toString(inputStream));
        } catch (IOException e) {
            throw new RuntimeException("failed to read from stream :", e);
        }
    }

    public static <T extends TemplateEngine> T create(Class<T> clz, String src) {
        try {
            Constructor<T> constructor = clz.getConstructor(String.class);
            return constructor.newInstance(src);
        } catch (Exception e) {
            throw new RuntimeException("failed to create template engine : " + clz.getSimpleName(), e);
        }
    }

    public static TemplateEngine create(String src) {
        return create(BeetlTemplateEngine.class, src);
    }
}
