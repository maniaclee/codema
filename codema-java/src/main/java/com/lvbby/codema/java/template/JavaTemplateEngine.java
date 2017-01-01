package com.lvbby.codema.java.template;

import com.lvbby.codema.core.render.BeetlTemplateEngine;

/**
 * Created by lipeng on 2017/1/2.
 */
public class JavaTemplateEngine extends BeetlTemplateEngine {
    public JavaTemplateEngine(String context) {
        super(format(context.replaceAll("//", ""))); //remove all the comment
    }

    public JavaTemplateEngine bind(Class clz, Object obj) {
        bind(clz.getSimpleName().replaceAll("[$|_]", ""), obj);
        return this;
    }

    @Override
    public String render() {
        return super.render();
    }

    private static String format(String s) {
        return s.replace('_', '}').replaceAll("$", "${");
    }
}
