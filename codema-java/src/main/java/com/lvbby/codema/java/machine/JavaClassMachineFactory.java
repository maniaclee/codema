package com.lvbby.codema.java.machine;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.AbstractBaseCodemaMachine;
import com.lvbby.codema.core.CodemaMachine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.CodemaMachineUtils;
import com.lvbby.codema.core.utils.FunctionAdaptor;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.io.File;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaClassMachineFactory {

    /***
     * 从Class解析
     * @return
     * @throws Exception
     */
    public static CodemaMachine<Class, JavaClass> fromClass() throws Exception {
        return buildJavaMachine(s -> JavaClass.from(s));
    }

    /***
     * 从文件解析
     * @return
     * @throws Exception
     */
    public static CodemaMachine<File, JavaClass> fromFile()  {
        return buildJavaMachine(s -> JavaClassUtils.convert(JavaLexer.read(s)));
    }

    /***
     * 从一段java代码解析
     */
    public static CodemaMachine<String, JavaClass> fromSrc()  {
        return buildJavaMachine(s -> JavaClassUtils.convert(JavaLexer.read(s)));
    }

    public static CodemaMachine<String, JavaClass> fromClassFullName()  {
        return buildJavaMachine(
                s -> JavaClassUtils.convert(JavaSrcLoader.getJavaSrcCompilationUnit(s)));
    }
    public static CodemaMachine<SqlTable, JavaClass> fromSqlTable()  {
        return buildJavaMachine(
                s -> JavaClassUtils.convert(s));
    }

    public static CodemaMachine<CompilationUnit, JavaClass> fromUnit(){
        return buildJavaMachine(s -> JavaClassUtils.convert(s));
    }

    /***
     * 简单的构造出参为javaClass的machine
     * @param functionAdaptor
     * @param <S>
     * @return
     */
    private static <S> CodemaMachine<S, JavaClass> buildJavaMachine(
            FunctionAdaptor<S, JavaClass> functionAdaptor) {
        return CodemaMachineUtils
                .build(source -> BasicResult.instance(functionAdaptor.apply(source)));
    }
}