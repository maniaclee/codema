package com.lvbby.codema.core.render;

/**
 * Created by lipeng on 16/7/28.
 */
public interface TemplateEngine {
    <T extends TemplateEngine> BeetlTemplateEngine bind(String key, Object obj);

    String render();
}
