package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.result.JavaXmlTemplateResult;
import com.lvbby.codema.java.template.TemplateContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 * 产生dao和mapper.xml
 */
public class JavaMybatisCodemaMachine extends AbstractJavaCodemaMachine<JavaMybatisCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaMybatisCodemaConfig config, JavaClass cu) throws Exception {
        SqlTable sqlTable = getSqlTable((JavaClass) codemaContext.getSource(), config.getIdQuery());
        validate(sqlTable);

        TemplateEngineResult daoTemplateResult = new JavaTemplateResult(config, $Dao_.class, cu)
                .bind("table", sqlTable);
        config.handle(codemaContext, daoTemplateResult);

        String xml = IOUtils.toString(JavaMybatisCodemaMachine.class.getResourceAsStream("mybatis_dao.xml"));
        config.handle(codemaContext,
                JavaXmlTemplateResult.ofResource(config, xml, cu)
                        .bind("table", sqlTable)
                        .bind("dao", daoTemplateResult.getResult())
                        .filePath(config.getDestResourceRoot(), config.getMapperDir(), String.format("%sMapper.xml", ((JavaClass)codemaContext.getSource()).getName()))
        );
    }

    public void preCode(CodemaContext codemaContext, JavaMybatisCodemaConfig config) throws Exception {
        /**mybatis config*/

        config.handle(codemaContext,
                new BasicResult().result(loadResourceAsString("mybatis.xml"))
                        .filePath(config.getDestResourceRoot(), "mybatis.xml")
                .writeMode(WriteMode.writeIfNoExist)
        );

        /** dal config */
        if (config.isNeedConfigClass()) {
            config.handle(codemaContext, new JavaTemplateResult(
                    new TemplateContext(DalConfig.class, config).pack(config.getConfigPackage()))
                    .writeMode(WriteMode.writeIfNoExist));
        }
    }

    private SqlTable getSqlTable(JavaClass cu, Function<JavaClass, JavaField> idQuery) {
        if (cu.getFrom() != null && cu.getFrom() instanceof SqlTable)
            return (SqlTable) cu.getFrom();
        return guessFromJavaClass(cu, idQuery);
    }

    private SqlTable guessFromJavaClass(JavaClass javaClass, Function<JavaClass, JavaField> idQuery) {
        SqlTable re =  SqlTable.instance(javaClass.getName());
        re.setFields(javaClass.getFields().stream().map(javaField -> {
            SqlColumn sqlColumn =SqlColumn.instance(javaField.getName());
            sqlColumn.setJavaType(javaField.getType().getJavaType());
            sqlColumn.setJavaTypeName(javaField.getType().getName());
            return sqlColumn;
        }).collect(Collectors.toList()));
        //id field
        if (idQuery != null) {
            JavaField idField = idQuery.apply(javaClass);
            if (idField != null) {
                List<SqlColumn> idColumns = re.getFields().stream().filter(sqlColumn -> sqlColumn.getNameCamel().equalsIgnoreCase(idField.getName())).collect(Collectors.toList());
                if (idColumns.size() > 1)
                    throw new IllegalArgumentException(String.format("multi id columns found for %s", javaClass.getName()));
                if (idColumns.size() == 1) {
                    idColumns.get(0).setPrimaryKey(true);
                }
            }
        }
        if(re.getPrimaryKeyField()==null){
            re.buildPrimaryKeyField("id");
        }

        re.setPrimaryKeyField(re.getFields().stream().filter(SqlColumn::isPrimaryKey).findFirst().orElse(null));
        return re;
    }

    private void validate(SqlTable sqlTable) {
        Validate.notNull(sqlTable.getPrimaryKeyField(), "no primary key found for table : " + sqlTable.getNameInDb());
    }

}
