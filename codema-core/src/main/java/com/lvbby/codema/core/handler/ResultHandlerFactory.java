package com.lvbby.codema.core.handler;

import com.alibaba.fastjson.JSON;
import com.lvbby.codema.core.ResultHandler;

/**
 *
 * @author dushang.lp
 * @version $Id: ResultHandlerFactory.java, v 0.1 2017-12-03 下午9:30 dushang.lp Exp $
 */
public class ResultHandlerFactory {

    public static final ResultHandler print           = resultContext -> System.out.println(resultContext.getResult());
    public static final ResultHandler printJson       = resultContext -> System.out.println(JSON.toJSONString(resultContext.getResult()));
    public static final ResultHandler printJsonPretty = resultContext -> System.out.println(JSON.toJSONString(resultContext.getResult(),true));
    public static final ResultHandler fileWrite       = new FileWriterResultHandler();
    public static final ResultHandler clipBoard       = new ClipBoardResultHandler();
}