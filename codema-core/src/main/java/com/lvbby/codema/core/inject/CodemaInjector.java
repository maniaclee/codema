package com.lvbby.codema.core.inject;

/**
 * Created by lipeng on 16/12/27.
 */
public interface CodemaInjector {
    void process(CodemaInjectContext context) throws Exception;
}
