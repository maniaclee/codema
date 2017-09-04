package com.lvbby.codema.java.baisc;

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
        return (source, from) -> clzName;
    }

    /***
     * 后缀形式
     * from.name + suffix
     * @param suffix
     * @return
     */
    public static JavaClassNameParser fromSuffix(String suffix) {
        return (source, from) -> from.getName() + suffix;
    }
    /***
     * 后缀形式
     * bean.name + suffix
     * @param suffix
     * @return
     */
    public static JavaClassNameParser sourceSuffix(String suffix) {
        return (source, from) -> source.getName() + suffix;
    }

    /***
     * 直接使用source的名称
     * @return
     */
    public static JavaClassNameParser defaultInstance() {
        return (source, from) -> from.getName();
    }
}