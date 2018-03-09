package com.lvbby.codema.java.baisc;

import com.lvbby.codema.java.entity.JavaClass;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaClassNameParserFactory.java, v 0.1 2017-09-02 11:52 dushang.lp Exp $
 */
public class JavaClassNameParserFactory {

    /***
     *使用固定的name
     * @param clzName
     * @return
     */
    public static JavaClassNameParser className(String clzName) {
        return (source) -> clzName;
    }

    /***
     * 后缀形式
     * bean.name + suffix
     * @param suffix
     * @return
     */
    public static JavaClassNameParser<JavaClass> suffix(String suffix) {
        return source -> source.getName() + suffix;
    }

}