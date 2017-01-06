package com.lvbby.codema.java.deprecated.jd;

import com.lvbby.codema.java.tool.JavaLexer;
import jd.core.loader.Loader;
import jd.core.loader.LoaderException;
import jd.core.preferences.Preferences;
import jd.core.process.DecompilerImpl;

import java.io.*;
import java.net.URL;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaClassDecompiler {


    public String getMethods(Class clz) throws LoaderException {
        JavaMethodExtractor printer = new JavaMethodExtractor();
        new DecompilerImpl().decompile(new Preferences(), getLoader(clz), printer, null);
        return printer.getResult();
    }

    private Loader getLoader(Class clz) {
        return new Loader() {
            @Override
            public DataInputStream load(String s) throws LoaderException {
                return new DataInputStream(getJavaSrc(clz));
            }

            @Override
            public boolean canLoad(String s) {
                return true;
            }
        };
    }

    public InputStream getJavaSrc(Class clz) {
        try {
            URL resource = JavaClassDecompiler.class.getResource("/");
            if (resource.getProtocol().equalsIgnoreCase("file")) {
                File file = new File(resource.getFile(), clz.getName().replace('.', '/') + ".class");
                return new FileInputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws LoaderException {
        String methods = new JavaClassDecompiler().getMethods(JavaLexer.class);
        System.out.println(methods);
        System.out.println(methods.length());
    }
}
