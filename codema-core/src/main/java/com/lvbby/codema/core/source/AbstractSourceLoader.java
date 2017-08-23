/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lvbby.codema.core.source;

import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractSourceLoader.java, v 0.1 2017-08-23 обнГ8:08 dushang.lp Exp $
 */
public abstract class AbstractSourceLoader<T> implements SourceLoader<T>{

    protected InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}