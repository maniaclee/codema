package com.lvbby.codema.render.beetl;

import com.lvbby.codema.render.TemplateEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;

/**
 * Created by lipeng on 16/7/28.
 */
public class BeetlTemplateEngine implements TemplateEngine {
    Template t;
    GroupTemplate gt;


    public BeetlTemplateEngine(String context) {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            gt = new GroupTemplate(resourceLoader, cfg);
            t = gt.getTemplate(context);
        } catch (IOException e) {
            throw new RuntimeException("error create template ", e);
        }
    }

    public BeetlTemplateEngine registFunction(String pack, Class clz) {
        gt.registerFunctionPackage(pack, clz);
        return this;
    }

    public GroupTemplate getGroupTemplate() {
        return gt;
    }

    @Override
    public void bind(String key, Object obj) {
        t.binding(key, obj);
    }

    @Override
    public String render() {
        return t.render();
    }


}
