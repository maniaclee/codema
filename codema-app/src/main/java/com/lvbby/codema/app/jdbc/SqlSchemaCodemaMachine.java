package com.lvbby.codema.app.jdbc;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.JavaClassJdbcTableFactory;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;

import java.util.List;

/**
 * Created by dushang.lp on 2017/6/23.
 */
public class SqlSchemaCodemaMachine extends AbstractJavaCodemaMachine<SqlSchemaCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, SqlSchemaCodemaConfig config, JavaClass javaClass) throws Exception {
        BasicResult result = new BasicResult();
        Class type = javaClass.getType();
        if (type != null) {
            List<SqlTable> tables = JavaClassJdbcTableFactory.of(type).getTables();
        }
        config.handle(codemaContext, result);
    }


}
