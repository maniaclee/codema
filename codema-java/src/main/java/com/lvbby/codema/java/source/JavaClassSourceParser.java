package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.SourceParser;
import com.lvbby.codema.core.error.CodemaException;
import com.lvbby.codema.core.utils.CodemaUtils;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;

import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceParser<JavaSourceParam> {
    public static final String schema = "java://class/";

    @Override
    public String getSupportedUriScheme() {
        return schema;
    }

    public static String toURI(Class clz) {
        return schema + clz.getName();
    }

    @Override
    public JavaSourceParam parse(URI from) throws Exception {
        List<JavaClass> re = Lists.newLinkedList();
        Collection<Class<?>> allClasses = ReflectionUtils.loadClasses(CodemaUtils.getPathPart(from));
        if (CollectionUtils.isEmpty(allClasses))
            throw new CodemaException("no class found in " + from);
        for (Class<?> clz : allClasses)
            re.add(JavaClass.from(clz));
        return new JavaSourceParam(re);
    }


}
