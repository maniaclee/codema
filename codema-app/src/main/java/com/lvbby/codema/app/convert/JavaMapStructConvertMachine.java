package com.lvbby.codema.app.convert;

import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.core.config.NotNull;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lipeng on 16/12/27.
 */
@AppTemplateResource($MapStruct_.class)
public class JavaMapStructConvertMachine extends AppMachine {
    @NotNull
    @Getter
    @Setter
    private JavaClassNameParser convertToClass;
}
