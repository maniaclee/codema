package com.lvbby.codema.core.render;

import com.lvbby.codema.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 2017/1/7.
 */
public class BeetlFn extends StringUtils{
    public String capital(String s) {
        return StringUtils.capitalize(s);
    }

    public String unCapital(String s) {
        return StringUtils.uncapitalize(s);
    }

    public String camel(String s) {
        return ReflectionUtils.camel(s);
    }

    public static String getJavaClassName(String className) {
        return className.replaceAll("[^.<>]+\\.", "");
    }

    public static String getPackage(String className) {
        if (!className.contains("."))
            return "";
        return className.replaceAll("(\\.[^.]+)$", "");
    }

    public static String format(String format,Object... args){
        return String.format(format,args);
    }

}
