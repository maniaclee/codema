package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.core.AbstractTemplateMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.config.NotNull;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 * Created by lipeng on 2018/3/11.
 */

public class MybatisMapperXmlMachine extends AbstractTemplateMachine<JavaClass, String> {
    @Getter
    @Setter
    @NotNull
    /** mapper全类名 */
    private Function<SqlTable, String> mapperName;

    @Getter
    @Setter
    private String mapperDir;

    @Getter
    @Setter
    @NotNull
    /** do machine */
    private Machine<?, SqlTable> sqlTableMachine;

    @Override
    protected void doCode() throws Exception {
        SqlTable sqlTable = sqlTableMachine.getResult().getResult();
        String mapper = mapperName.apply(sqlTable);
        Validate.notNull(sqlTable.getPrimaryKey(), "no primary key found for table : " + sqlTable.getNameInDb());

        /**根据mapper xml */
        BasicResult mapperXml = new XmlTemplateResult(loadResourceAsString("mapper.xml"))
                .bind("mapper", mapper)
                .bind("entity", source)
                .bind("table", sqlTable)
                .filePath(getMapperDir(), String.format("%s.xml", sqlTable.getName()));
        handle(mapperXml);

    }
}
