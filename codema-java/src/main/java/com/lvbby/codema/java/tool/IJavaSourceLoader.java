package com.lvbby.codema.java.tool;

/**
 *
 * @author dushang.lp
 * @version $Id: IJavaSourceLoader.java, v 0.1 2018年03月13日 下午2:32 dushang.lp Exp $
 */
public interface IJavaSourceLoader {
    String loadJavaSource(String classFullName) throws Exception;
}