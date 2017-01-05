package com.lvbby.codema.java.template;

/**
 * Created by lipeng on 17/1/5.
 */
public class $ExprHandler extends DefaultJavaTemplateAnnotationHandler<$Foreach> {
    @Override
    public String evalString($Foreach $Foreach) {
        return $Foreach.value() + "{";
    }
}
