package com.lvbby.codema.tool.coder.source;

import com.lvbby.codema.coder.CoderExecutor;
import com.lvbby.codema.tool.coder.JavaSourceCoderRequest;

/**
 * Created by lipeng on 2016/12/20.
 */
public class SourceTest {

    public void sdfs() throws Exception {
        JavaSourceCoderRequest request = new JavaSourceCoderRequest();
        new CoderExecutor().execClass(request, JavaSourceParserCoder.class);
    }
}
