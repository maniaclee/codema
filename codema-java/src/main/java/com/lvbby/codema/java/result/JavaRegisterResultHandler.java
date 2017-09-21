package com.lvbby.codema.java.result;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.resource.JavaClassResource;

/**
 * Created by lipeng on 2016/12/31.
 * 将结果注册到容器中
 */
public class JavaRegisterResultHandler implements ResultHandler {
    @Override
    public void handle(ResultContext resultContext) {
        if (resultContext.getResult() == null || resultContext.getResult().getResult() == null)
            return;
        Object result = resultContext.getResult().getResult();
        if (result instanceof JavaClass)
            resultContext.getCodemaContext().getCodemaBeanFactory().register(
                    new JavaClassResource((JavaClass) resultContext.getResult().getResult())
                            .bindConfig(resultContext.getConfig()));
    }
}
