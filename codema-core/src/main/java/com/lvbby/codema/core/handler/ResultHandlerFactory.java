package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultHandler;
import com.lvbby.codema.core.result.PrintableResult;

/**
 *
 * @author dushang.lp
 * @version $Id: ResultHandlerFactory.java, v 0.1 2017-12-03 下午9:30 dushang.lp Exp $
 */
public class ResultHandlerFactory {

    public static final ResultHandler print     = new PrintResultHandler();
    public static final ResultHandler fileWrite = new FileWriterResultHandler();
    public static final ResultHandler clipBoard = new ClipBoardResultHandler();
    public static final ResultHandler collect   = new CollectResultHandler();
}