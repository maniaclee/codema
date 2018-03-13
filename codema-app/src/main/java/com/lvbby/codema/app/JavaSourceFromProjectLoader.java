package com.lvbby.codema.app;

import com.lvbby.codema.java.tool.IJavaSourceLoader;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaSourceFromProjectLoader.java, v 0.1 2018年03月13日 下午3:00 dushang.lp Exp $
 */
public class JavaSourceFromProjectLoader implements IJavaSourceLoader{
    @Override public String loadJavaSource(String className) throws Exception {
        //内部使用
        if(className.startsWith("com.lvbby.codema")) {
            try {
                Class<?> clz = Class.forName(className);
                InputStream input = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(String.format("%s.java", clz.getName().replace('.', '/')));
                if (input != null)
                    return IOUtils.toString(input);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}