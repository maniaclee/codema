package com.lvbby.codema;

import com.lvbby.codema.coder.CoderExecutor;
import com.lvbby.codema.coder.java.JavaClassCoder;
import com.lvbby.codema.coder.java.JavaDelegateCoder;
import com.lvbby.codema.coder.java.JavaInitCoder;
import com.lvbby.codema.coder.java.source.JavaSourceCoderRequest;
import com.lvbby.codema.coder.java.source.handler.JavaSourceClassNameCoder;
import com.lvbby.codema.coder.java.source.handler.JavaSourceParserCoder;
import com.lvbby.codema.coder.java.source.handler.JavaSourceSpringBeanTestCoder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by lipeng on 2016/12/20.
 */
public class SourceTest {

    @Test
    public void springTest() throws Exception {
        JavaSourceCoderRequest request = new JavaSourceCoderRequest();
        request.setAuthor("lipeng");
        request.setSource(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/dp/ssp-search-service/ssp-es-admin-api/src/main/java/com/dianping/ssp/search/es/admin/api/EsAdminService.java")));
        new CoderExecutor().exec(request,
                new JavaInitCoder(),
                new JavaSourceParserCoder(),
                new JavaSourceClassNameCoder("Test"),
                new JavaClassCoder(),
                new JavaSourceSpringBeanTestCoder()
        );
        System.out.println(request.getResult());
    }

    @Test
    public void delegate() throws Exception {
        JavaSourceCoderRequest request = new JavaSourceCoderRequest();
        request.setAuthor("lipeng");
        request.setSource(IOUtils.toString(new FileInputStream("/Users/lipeng/workspace/bridge/bridge-api/src/main/java/com/lvbby/bridge/gateway/ApiGateWay.java")));
        new CoderExecutor().exec(request,
                new JavaInitCoder(),
                new JavaSourceParserCoder(),
                new JavaSourceClassNameCoder("Service"),
                new JavaClassCoder(),
                new JavaDelegateCoder()
        );
        System.out.println(request.getResult());
    }
}
