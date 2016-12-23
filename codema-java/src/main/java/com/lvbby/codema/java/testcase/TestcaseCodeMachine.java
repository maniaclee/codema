package com.lvbby.codema.java.testcase;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.ConfigBind;

/**
 * Created by lipeng on 16/12/23.
 */
@ConfigBind(CoderJavaTestcaseConfig.class)
public class TestcaseCodeMachine implements CodemaMachine {
    @Override
    public void code(CodemaContext codemaContext) throws Exception {
        System.out.println("find " + getClass().getName());
    }
}
