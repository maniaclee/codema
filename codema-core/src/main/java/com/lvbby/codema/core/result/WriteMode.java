package com.lvbby.codema.core.result;

/**
 *
 * @author dushang.lp
 * @version $Id: WriteMode.java, v 0.1 2017-11-03 下午6:31 dushang.lp Exp $
 */
public enum WriteMode {
    /**覆盖*/flush, /**追加*/append, /**文件不存在时写*/writeIfNoExist, /**按具体文件格式合并*/merge
}