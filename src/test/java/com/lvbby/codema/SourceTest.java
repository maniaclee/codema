package com.lvbby.codema;

import com.lvbby.codema.coder.CoderExecutor;
import com.lvbby.codema.tool.coder.JavaClassCoder;
import com.lvbby.codema.tool.coder.JavaInitCoder;
import com.lvbby.codema.tool.coder.source.JavaSourceCoderRequest;
import com.lvbby.codema.tool.coder.source.coder.JavaSourceClassNameCoder;
import com.lvbby.codema.tool.coder.source.coder.JavaSourceParserCoder;
import com.lvbby.codema.tool.coder.source.coder.JavaSpringBeanTestCoder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by lipeng on 2016/12/20.
 */
public class SourceTest {

    @Test
    public void sdfs() throws Exception {
        JavaSourceCoderRequest request = new JavaSourceCoderRequest();
        request.setAuthor("lipeng");
        request.setSource(IOUtils.toString(new FileInputStream("/Users/psyco/workspace/dp/ssp-search-service/ssp-es-admin-api/src/main/java/com/dianping/ssp/search/es/admin/api/EsAdminService.java")));
        new CoderExecutor().exec(request,
                new JavaInitCoder(),
                new JavaSourceParserCoder(),
                new JavaSourceClassNameCoder("Test"),
                new JavaClassCoder(),
                new JavaSpringBeanTestCoder()
        );
        System.out.println(request.getResult());
    }
}
