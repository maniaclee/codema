package com.lvbby.codema.app.mybatis;

import com.google.common.base.CaseFormat;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.result.JavaXmlTemplateResult;
import com.lvbby.codema.java.template.TemplateContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 * 产生dao和mapper.xml
 */
public class JavaMybatisCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaMybatisCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaMybatisCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass cu) throws Exception {
        SqlTable sqlTable = getSqlTable(cu);
        validate(sqlTable);
        TemplateEngineResult daoTemplateResult = new JavaTemplateResult(config, $src__name_Dao.class, cu)
                .bind("table", sqlTable)
                .registerResult();
        config.handle(codemaContext, config, daoTemplateResult);

        String xml = IOUtils.toString(JavaMybatisCodemaMachine.class.getResourceAsStream("mybatis_dao.xml"));
        config.handle(codemaContext, config, JavaXmlTemplateResult.ofResource(config, xml, cu, new File(new File(config.getDestResourceRoot(), config.getMapperDir()), String.format("%sMapper.xml", cu.getName())))
                .bind("table", sqlTable)
                .bind("dao", daoTemplateResult.getResult()));
    }

    @ConfigBind(JavaMybatisCodemaConfig.class)
    @CodemaRunner
    public void global(CodemaContext codemaContext, @NotNull JavaMybatisCodemaConfig config) throws Exception {
        /**mybatis config*/
        config.handle(codemaContext, config, BasicResult.ofResource(JavaMybatisCodemaMachine.class, "mybatis.xml", config.getDestResourceRoot()));

        /** dal config */
        config.handle(codemaContext, config, new JavaTemplateResult(new TemplateContext(DalConfig.class, config)
                .pack(config.getConfigPackage()))
                .render());
    }

    private SqlTable getSqlTable(JavaClass cu) {
        if (cu.getFrom() != null && cu.getFrom() instanceof SqlTable)
            return (SqlTable) cu.getFrom();
        return guessFromJavaClass(cu);
    }

    private SqlTable guessFromJavaClass(JavaClass javaClass) {
        SqlTable re = new SqlTable();
        re.setName(javaClass.getName());
        re.setNameInDb(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaClass.getName()));
        re.setFields(javaClass.getFields().stream().map(javaField -> {
            SqlColumn sqlColumn = new SqlColumn();
            sqlColumn.setNameInDb(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaField.getName()));
            sqlColumn.setNameCamel(javaField.getName());
            sqlColumn.setJavaType(javaField.getType().getJavaType());
            sqlColumn.setJavaTypeName(javaField.getType().getName());
            sqlColumn.setPrimaryKey(javaField.getName().equalsIgnoreCase("id"));
            return sqlColumn;
        }).collect(Collectors.toList()));
        re.setPrimaryKeyField(re.getFields().stream().filter(SqlColumn::isPrimaryKey).findFirst().orElse(null));
        return re;
    }

    private void validate(SqlTable sqlTable) {
        Validate.notNull(sqlTable.getPrimaryKeyField(), "no primary key found for table : " + sqlTable.getNameInDb());
    }

}
