package com.lvbby.codema.core.render;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lipeng on 2017/1/7.
 */
public class BeetlFn {
    public String capital(String s) {
        return StringUtils.capitalize(s);
    }

    public String unCapital(String s) {
        return StringUtils.uncapitalize(s);
    }
}
