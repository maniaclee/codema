package com.lvbby.codema.java.template;

/**
 * Created by lipeng on 17/1/5.
 */
public class $IfHandler extends DefaultJavaTemplateAnnotationHandler<$If> {
    @Override
    public String evalString($If $Foreach) {
        return $Foreach.value() + "{";
    }
}
