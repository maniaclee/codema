package com.lvbby.codema.java.app.mybatis;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaMybatisCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaMybatisCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaMybatisCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass cu) throws Exception {
        validate(cu);
        config.handle(codemaContext, config, new JavaTemplateResult(config, $src__name_Dao.class, cu));
    }

    private void validate(JavaClass cu) {
        Validate.isTrue(cu.getFrom() instanceof SqlTable, "no sql table found.");
        SqlTable sqlTable = (SqlTable) cu.getFrom();
        Validate.notNull(sqlTable.getPrimaryKeyField(), "no primary key found for table : " + sqlTable.getNameInDb());
    }
}
