package com.lvbby.codema.java.machine;

import com.github.javaparser.ast.CompilationUnit;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.CodemaMachineUtils;
import com.lvbby.codema.core.utils.FunctionAdaptor;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.impl.JavaSourceFromClassFullNameMachine;
import com.lvbby.codema.java.machine.impl.JavaSourceFromClassMachine;
import com.lvbby.codema.java.machine.impl.JavaSourceFromSqlTableMachine;
import com.lvbby.codema.java.machine.impl.JavaSourceFromSrcMachine;
import com.lvbby.codema.java.tool.JavaClassUtils;
import com.lvbby.codema.java.tool.JavaLexer;
import com.lvbby.codema.java.tool.JavaSrcLoader;

import java.io.File;

/**
 * Created by lipeng on 17/1/9.
 */
public class JavaSourceMachineFactory {

    /***
     * 从Class解析
     * @return
     * @throws Exception
     */
    public static Machine<Class, JavaClass> fromClass()  {
        return new JavaSourceFromClassMachine();
        //                return buildJavaMachine(s -> JavaClassUtils.fromClass(s));
    }

    /***
     * 从文件解析
     * @return
     * @throws Exception
     */
    public static Machine<File, JavaClass> fromFile()  {
        return buildJavaMachine(s -> JavaClassUtils.convert(JavaLexer.read(s)));
    }

    /***
     * 从一段java代码解析
     */
    public static Machine<String, JavaClass> fromSrc()  {
        return new JavaSourceFromSrcMachine();
        //        return buildJavaMachine(s -> JavaClassUtils.convert(JavaLexer.read(s)));
    }

    public static Machine<String, JavaClass> fromClassFullName()  {
        return new JavaSourceFromClassFullNameMachine();
        //        return buildJavaMachine(
//                s -> JavaClassUtils.convert(JavaSrcLoader.getJavaSrcCompilationUnit(s)));
    }
    public static Machine<SqlTable, JavaClass> fromSqlTable()  {
        return new JavaSourceFromSqlTableMachine();
//        return buildJavaMachine(
//                s -> JavaClassUtils.convert(s));
    }

    public static Machine<CompilationUnit, JavaClass> fromUnit(){
        return buildJavaMachine(s -> JavaClassUtils.convert(s));
    }

    /***
     * 简单的构造出参为javaClass的machine
     * @param functionAdaptor
     * @param <S>
     * @return
     */
    private static <S> Machine<S, JavaClass> buildJavaMachine(
            FunctionAdaptor<S, JavaClass> functionAdaptor) {
        return CodemaMachineUtils
                .build(source -> BasicResult.instance(functionAdaptor.apply(source)));
    }
}