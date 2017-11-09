package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.bean.CodemaBean;
import com.lvbby.codema.core.source.AbstractSourceLoader;
import com.lvbby.codema.core.source.SourceLoaderCallback;
import com.lvbby.codema.core.tool.mysql.SqlParser;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/1/7.
 */
public class JavaSqlSchemeSourceParser extends AbstractSourceLoader<JavaClass> implements
                                                                               SourceLoaderCallback<JavaClass>{
    private Map<JavaClass,SqlTable> map;
    public JavaSqlSchemeSourceParser(String sql) {
        Validate.notBlank(sql, "create sql can't be blank");
        map = SqlParser.fromSql(sql).stream()
                .collect(Collectors.toMap(o -> JavaDbSourceParser.convert(o), o -> o));
        setSource(Lists.newArrayList(map.keySet()));
    }

    @Override public void process(JavaClass source, CodemaContext context) {
        //把sqlTable注入到容器里
        context.getCodemaBeanFactory().register(new CodemaBean(map.get(source)));
    }
}
