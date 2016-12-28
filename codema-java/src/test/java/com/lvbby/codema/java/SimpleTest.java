package com.lvbby.codema.java;

import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.java.engine.JavaEngineContext;
import org.junit.Test;

import java.net.URI;

/**
 * Created by lipeng on 2016/12/24.
 */
public class SimpleTest {

    @Test
    public void uri() {
        URI uri = URI.create("asdf:///root/leaf?path=sdf&&sub=sdf");
        System.out.println(uri.getAuthority());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri);
    }

    @Test
    public void name() throws Exception {
        JavaEngineContext parameter = new JavaEngineContext();
        parameter.setFromClassName("shitDto");
        //        String eval = ScriptEngineFactory.instance.eval("script://js/{match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}}", parameter);
        String eval = ScriptEngineFactory.instance.eval("script://js/  (function(){return {match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}})()", parameter);
        System.out.println(eval);

    }
}
