package com.lvbby.codema.core.handler;

import com.lvbby.codema.core.ResultHandler;

/**
 *
 * @author dushang.lp
 * @version $Id: ResultHandlerFactory.java, v 0.1 2017-12-03 下午9:30 dushang.lp Exp $
 */
public class ResultHandlerFactory {

    public static final ResultHandler printResultHandler     = new PrintResultHandler();
    public static final ResultHandler fileResultHandler      = new FileWriterResultHandler();
    public static final ResultHandler clipBoardResultHandler = new ClipBoardResultHandler();
}