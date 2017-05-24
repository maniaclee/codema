package com.lvbby.codema.java.mock;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.$GenericTypeArg_;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.lvbby.codema.java.template.$GenericTypeArg_.$GenericTypeArgInstance_;

/**
 * Created by dushang.lp on 2017/5/24.
 */
public class $Class_Test {

    @Mock
    private $Class1_ $class1_;
    @InjectMocks
    private $Class_ $class_;

    @Before
    public void init() {
        $class_.setClass1_($class1_);
    }

    @Test
    public void test$invoke() throws Exception {
        $GenericTypeArg_ value = new $GenericTypeArg_();
        Mockito.when(
                $class1_.$Method_(Mockito.any($GenericTypeArg_.class)))
                .thenReturn(value);

        $GenericTypeArg_ re = $class_.$Method_($GenericTypeArgInstance_);
        success(re, null);
    }

    private void success(Object response, Object expect) {
        Assert.assertEquals(response, expect);
    }
}
