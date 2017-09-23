package com.lvbby.codema.app.convert;

import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaTemplateResource;

/**
 * Created by lipeng on 16/12/27.
 */
@JavaTemplateResource($Convert_.class)
public class JavaConvertCodemaConfig extends JavaBasicCodemaConfig {
    private JavaClassNameParser convertToClassNameParser;

    public JavaClassNameParser getConvertToClassNameParser() {
        return convertToClassNameParser;
    }

    public void setConvertToClassNameParser(JavaClassNameParser convertToClassNameParser) {
        this.convertToClassNameParser = convertToClassNameParser;
    }
}
