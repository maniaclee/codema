package com.lvbby.codema.java.tool.jd;

/**
 * Created by lipeng on 2017/1/1.
 */
public class JavaMethodExtractor extends BasicJavaSrcExtractor {

    @Override
    public void printMethod(String s, String s1, String s2, String s3) {
        echo(s1);
        //        echo(Lists.newArrayList(s1).stream().collect(Collectors.joining("_________")));
    }

    @Override
    public void printMethodDeclaration(String s, String s1, String s2) {
        //        echo(Lists.newArrayList(s, s1, s2).stream().collect(Collectors.joining(" ")));
        echo(s1);
    }

    @Override
    public void printStaticMethod(String s, String s1, String s2, String s3) {
        //        echo(Lists.newArrayList(s, s1, s2, s3).stream().collect(Collectors.joining(" ______")));
        echo(s1);
    }

    @Override
    public void printStaticMethodDeclaration(String s, String s1, String s2) {
        //        echo(Lists.newArrayList(s, s1, s2).stream().collect(Collectors.joining(" ")));
        echo(s1);
    }

    @Override
    public void printType(String s, String s1, String s2) {
        echo(s1);
    }

    @Override
    public void printTypeDeclaration(String s, String s1) {
        echo(s1);
    }

    @Override
    public void printTypeImport(String s, String s1) {
        echo(s1);
    }

    @Override
    public void printField(String s, String s1, String s2, String s3) {
        echo(s1);

    }

    @Override
    public void printFieldDeclaration(String s, String s1, String s2) {
        echo(s1);

    }

    @Override
    public void printStaticField(String s, String s1, String s2, String s3) {
        echo(s1);

    }

    @Override
    public void printStaticFieldDeclaration(String s, String s1, String s2) {
        echo(s1);

    }

    @Override
    public void printConstructor(String s, String s1, String s2, String s3) {
        echo(s1);

    }

    @Override
    public void print(char c) {
        echo(c);
    }

    @Override
    public void print(int i) {
    }

    @Override
    public void print(String s) {
        echo(s);

    }

    @Override
    public void printNumeric(String s) {
        echo(s);

    }

    @Override
    public void printString(String s, String s1) {
        echo(s1);
    }

    @Override
    public void printKeyword(String s) {
        echo(s);
    }

    @Override
    public void printJavaWord(String s) {
        echo(s);
    }

}
