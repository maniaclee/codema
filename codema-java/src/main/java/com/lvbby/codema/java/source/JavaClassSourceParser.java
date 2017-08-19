package com.lvbby.codema.java.source;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.source.SourceLoader;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassSourceParser implements SourceLoader<JavaSourceParam> {

    List<Class> allClasses = Lists.newLinkedList();

    private JavaClassSourceParser(List<Class> allClasses) {
        if (CollectionUtils.isEmpty(allClasses))
            throw new IllegalArgumentException("no class found");
        this.allClasses = allClasses;
    }

    public static JavaClassSourceParser fromPackage(String pack) throws Exception {
        return new JavaClassSourceParser(ReflectionUtils.loadClasses(pack));
    }

    public static JavaClassSourceParser fromClass(Class clz) throws Exception {
        return new JavaClassSourceParser(Lists.newArrayList(clz));
    }

    public static JavaClassSourceParser fromClass(List<Class> clz) throws Exception {
        return new JavaClassSourceParser(clz);
    }


    @Override
    public JavaSourceParam loadSource() throws Exception {
        List<JavaClass> re = Lists.newLinkedList();
        for (Class<?> clz : allClasses)
            re.add(JavaClass.from(clz));
        return new JavaSourceParam(re);
    }
}
