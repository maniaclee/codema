package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Maps;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.CodemaContextHolder;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by lipeng on 17/2/10.
 */
public class ImportUtils {
    private Map<String, Class> map = Maps.newHashMap();

    {
        add(Date.class);
        add(List.class);
        add(Collection.class);
        add(Map.class);
        add(Set.class);
        add(Timestamp.class);
    }

    private void add(Class clz) {
        map.put(clz.getSimpleName(), clz);
    }

    public void tryAddImports(CompilationUnit cu) {
        JavaLexer.getClass(cu).ifPresent(classOrInterfaceDeclaration -> {
            //属性
            classOrInterfaceDeclaration.getFields().forEach(fieldDeclaration -> _tryAddImports(fieldDeclaration.getVariable(0).getType().toString(), cu));
            //方法引用的对象
            classOrInterfaceDeclaration.getMethods().forEach(methodDeclaration -> {
                _tryAddImports(methodDeclaration.getType().toString(), cu);
                methodDeclaration.getParameters().forEach(parameter -> _tryAddImports(parameter.getType().toString(), cu));
            });
            //接口
            classOrInterfaceDeclaration.getImplements().forEach(intf->_tryAddImports(intf.getNameAsString(),cu));
            //父类
            classOrInterfaceDeclaration.getExtends().forEach(ex->_tryAddImports(ex.getNameAsString(),cu));
        });
    }

    private void _tryAddImports(String type, CompilationUnit cu) {
        if (ReflectionUtils.isFullClassName(type)) {
            cu.addImport(type);
            return;
        }
        if(type.contains("<")){
            _tryAddImports(ReflectionUtils.findFirst(type,"([^<>]+)<",matcher -> matcher.group(1)),cu);
            _tryAddImports(ReflectionUtils.findFirst(type,"<([^<>]+)>",matcher -> matcher.group(1)),cu);
            return ;
        }
        if (map.containsKey(type)) {
            cu.addImport(map.get(type));
            return;
        }
        CodemaContext codemaContext = CodemaContextHolder.getCodemaContext();
        List<Object> beans = codemaContext.getCodema().getCodemaBeanFactory().getBeans(codemaBean -> ReflectionUtils.getSimpleClassName(codemaBean.getId()).equals(type), Object.class);
        if (CollectionUtils.isNotEmpty(beans)) {
            beans.forEach(o -> {
                if(o instanceof JavaClass)
                    cu.addImport(((JavaClass) o).classFullName());
            });
            return;
        }
        try {
            Class<?> clz = Class.forName("java.util." + type);
            if (clz != null) {
                cu.addImport(clz);
                return;
            }
        } catch (ClassNotFoundException e) {
        }
    }
}
