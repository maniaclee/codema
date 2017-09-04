package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.javaMdDoc.JavaMdDocCodemaConfig;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 *
 * @author dushang.lp
 * @version $Id: MdJavaDocTest.java, v 0.1 2017-09-03 下午5:48 dushang.lp Exp $
 */
public class MdJavaDocTest extends BaseTest {
    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(
            Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")));
    }

    public void mdJavaDoc(String reference) throws Exception {
        String[] split = reference.split("[#,]");
        String service = split[0];
        String method = split[1];

        JavaMdDocCodemaConfig md = new JavaMdDocCodemaConfig();
        md.setAuthor("lee");
        md.addResultHandler(PrintResultHandler.class);
        md.setMethod(method);

        new Codema()
                .source(JavaClassSourceParser.fromClassFullName(service))
                .bind(md)
                .run();
    }

    @Test public void testMdJavaDoc() throws Exception {
        mdJavaDoc("com.lvbby.codema.core.result.BasicResult#config");
    }
}