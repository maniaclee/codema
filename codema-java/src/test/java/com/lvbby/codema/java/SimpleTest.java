package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.java.engine.JavaEngineContext;
import com.lvbby.codema.java.tool.JavaLexer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

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

    @Test
    public void yaml() throws Exception {
        Yaml yaml = new Yaml();
        A a = new A();
        a.setName("sdf");
        A aa = new A();
        aa.setName("aa");
        a.setA(Lists.newArrayList(aa));
        System.out.println(yaml.dump(a));
    }

    class A{
        private List<A> a;
        private String name;


        public List<A> getA() {
            return a;
        }

        public void setA(List<A> a) {
            this.a = a;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



    @Test
    public void java() throws Exception {
        System.out.println(getClass().getResource("/"));
        System.out.println(getClass().getResource("/").getFile().toString());
        System.out.println(getJavaSource(JavaLexer.class));
    }

    public String getJavaSource(Class clz) throws IOException {
        String path = "/" + clz.getName().replace('.', '/')+".java";
        System.out.println(path);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
        return IOUtils.toString(resourceAsStream);
    }
}
