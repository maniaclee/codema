package com.lvbby.codema.java.testcase;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.java.baisc.JavaSourceParam;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaTestcaseCodemaMachine implements CodemaInjectable {

    @CodemaRunner
    public void code(CodemaContext codemaContext, JavaTestcaseCodemaConfig config, JavaSourceParam source) {
        System.out.println("find testcase");
    }
}
