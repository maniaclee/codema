package com.lvbby.codema.core.result;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractFileResult.java, v 0.1 2017-11-03  dushang.lp Exp $
 */
public abstract class AbstractFileResult<T>extends AbstractResult<T> implements FileResult<T> {
    private WriteMode writeMode;

    @Override public WriteMode getWriteMode() {
        return writeMode;
    }

    public FileResult writeMode(WriteMode writeMode) {
        this.writeMode = writeMode;
        return this;
    }
}