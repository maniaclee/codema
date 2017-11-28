package com.lvbby.codema.java.result;

import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.Result;

/**
 * Created by lipeng on 2016/12/31.
 * 将结果注册到容器中
 */
@Deprecated
public class JavaRegisterResultHandler implements ResultHandler {
    @Override
    public void handle(Result re) {
        if (re==null||re.getResult() == null )
            return;
        Object result = re.getResult();

        //
    }
}
