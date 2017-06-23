package com.lvbby.codema.app.jdbc;

import com.lvbby.codema.app.repository.$Repository_;
import com.lvbby.codema.app.repository.JavaRepositoryCodemaConfig;
import com.lvbby.codema.app.repository.JavaRepositoryCodemaMachine;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.JavaClassJdbcTableFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dushang.lp on 2017/6/23.
 */
public class SqlSchemaCodemaMachine implements CodemaInjectable {
    @ConfigBind(SqlSchemaCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull SqlSchemaCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        BasicResult result = new BasicResult();
        Class type = javaClass.getType();
        if (type != null) {
            List<SqlTable> tables = JavaClassJdbcTableFactory.of(type).getTables();
        }
        config.handle(codemaContext, config, result);
    }


}
