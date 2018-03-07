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
@AppTemplateResource($Convert_.class)
public class JavaConvertMachine extends AppMachine {
    @Getter
    @Setter
    @NotNull
    private JavaClassNameParser convertToClassNameParser;

}

