package com.lvbby.codema.app.convert;

import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.JavaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 16/12/27.
 */
public class JavaMapStructConvertMachine extends JavaMachine {
    private JavaClassNameParser convertToClassNameParser;

    @Override
    public Result<JavaClass> codeEach(JavaClass cu) throws Exception {
        Validate.notNull(convertToClassNameParser, "convertToClassNameParser can't be null");
        return new JavaTemplateResult(this, $MapStruct_.class, cu);
    }

    /**
     * Getter method for property   convertToClassNameParser.
     *
     * @return property value of convertToClassNameParser
     */
    public JavaClassNameParser getConvertToClassNameParser() {
        return convertToClassNameParser;
    }

    /**
     * Setter method for property   convertToClassNameParser .
     *
     * @param convertToClassNameParser  value to be assigned to property convertToClassNameParser
     */
    public void setConvertToClassNameParser(JavaClassNameParser convertToClassNameParser) {
        this.convertToClassNameParser = convertToClassNameParser;
    }
}
