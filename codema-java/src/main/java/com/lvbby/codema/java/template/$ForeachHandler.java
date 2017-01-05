package com.lvbby.codema.java.template;

/**
 * Created by lipeng on 17/1/5.
 */
public class $ForeachHandler extends DefaultJavaTemplateAnnotationHandler<$Expr> {
    @Override
    public String evalString($Expr $Foreach) {
        return $Foreach.value();
    }
}
