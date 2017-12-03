package com.lvbby.codema.java.tool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: JavaMerger.java, v 0.1 2017-09-18 下午1:04 dushang.lp Exp $
 */
public class JavaMerger {

    private final CompilationUnit dest;
    private final CompilationUnit src;
    private boolean mergeExisted=true;

    public JavaMerger(String destSrc, CompilationUnit src) {
        this.dest = JavaLexer.read(destSrc);
        this.src = src;
    }

    public JavaMerger setMergeExisted(boolean mergeExisted) {
        this.mergeExisted = mergeExisted;
        return this;
    }

    public CompilationUnit merge() {
        return merge(dest, src);
    }
    private CompilationUnit merge(CompilationUnit dest, CompilationUnit src) {
        ClassOrInterfaceDeclaration srcClass = JavaLexer.getClass(src).orElse(null);
        ClassOrInterfaceDeclaration destClass = JavaLexer.getClass(dest).orElse(null);
        if (srcClass == null || destClass == null) {
            return dest;
        }
        mergeImport(src, dest);
        mergeField(srcClass, destClass);
        mergeMethod(srcClass, destClass);
        return dest;
    }

    private void mergeMethod(ClassOrInterfaceDeclaration srcClass,
                             ClassOrInterfaceDeclaration destClass) {
        //方法签名
        Function<MethodDeclaration, String> methodIdFunc = methodDeclaration -> {
            String result = methodDeclaration.getNameAsString();
            if(methodDeclaration.getParameters()!=null && methodDeclaration.getParameters().isNonEmpty()){
                String ps =methodDeclaration.getParameters().stream().map(parameter -> parameter.getType().asString()).collect(
                        Collectors.joining(","));
                result= String.format("%s.(%s)", result,ps);
            }
            return result;
        };
        for (MethodDeclaration mSrc : srcClass.getMethods()) {
            String nameAsString = methodIdFunc.apply(mSrc);
            MethodDeclaration existed = destClass.getMethods().stream()
                .filter(
                    methodDeclaration -> methodIdFunc.apply(methodDeclaration).equals(nameAsString))
                .findAny().orElse(null);
            if (existed != null) {
                if(!mergeExisted){
                    continue;
                }
                //删除改节点，覆写
                destClass.getMembers().remove(existed);
            }
            destClass.addMember(mSrc.clone());
        }
    }

    private void mergeField(ClassOrInterfaceDeclaration srcClass,
                            ClassOrInterfaceDeclaration destClass) {
        for (FieldDeclaration f : srcClass.getFields()) {
            String nameAsString = f.getVariable(0).getNameAsString();
            if (!destClass.getFields().stream().anyMatch(fieldDeclaration -> fieldDeclaration
                .getVariable(0).getNameAsString().equals(nameAsString))) {
                destClass.addMember(f.clone());
            }
        }
    }

    private void mergeImport(CompilationUnit src, CompilationUnit dest) {
        for (ImportDeclaration importDeclaration : src.getImports()) {
            String i = importDeclaration.getNameAsString();
            if (!dest.getImports().stream().anyMatch(
                importDeclaration1 -> importDeclaration1.getNameAsString().equalsIgnoreCase(i))) {
                dest.addImport(i);
            }
        }
    }

}