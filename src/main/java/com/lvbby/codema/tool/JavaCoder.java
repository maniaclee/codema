package com.lvbby.codema.tool;

/**
 * Created by lipeng on 16/12/16.
 */
public interface JavaCoder<C, R> {
    R gen(String code, C context);
}
