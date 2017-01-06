package com.lvbby.codema.java.deprecated.jd;

import jd.core.printer.Printer;

/**
 * Created by lipeng on 2017/1/1.
 */
public class BasicJavaSrcExtractor implements Printer {

    private StringBuilder builder = new StringBuilder();

    public void echo(String s) {
        builder.append(s);
    }
    public void echo(char s) {
        builder.append(s);
    }

    public String getResult() {
        return builder.toString();
    }

    @Override
    public void print(byte b) {

    }

    @Override
    public void print(char c) {

    }

    @Override
    public void print(int i) {

    }

    @Override
    public void print(String s) {

    }

    @Override
    public void printNumeric(String s) {

    }

    @Override
    public void printString(String s, String s1) {

    }

    @Override
    public void printKeyword(String s) {

    }

    @Override
    public void printJavaWord(String s) {

    }

    @Override
    public void printType(String s, String s1, String s2) {

    }

    @Override
    public void printTypeDeclaration(String s, String s1) {

    }

    @Override
    public void printTypeImport(String s, String s1) {

    }

    @Override
    public void printField(String s, String s1, String s2, String s3) {

    }

    @Override
    public void printFieldDeclaration(String s, String s1, String s2) {

    }

    @Override
    public void printStaticField(String s, String s1, String s2, String s3) {

    }

    @Override
    public void printStaticFieldDeclaration(String s, String s1, String s2) {

    }

    @Override
    public void printConstructor(String s, String s1, String s2, String s3) {

    }

    @Override
    public void printConstructorDeclaration(String s, String s1, String s2) {

    }

    @Override
    public void printStaticConstructorDeclaration(String s, String s1) {

    }

    @Override
    public void printMethod(String s, String s1, String s2, String s3) {

    }

    @Override
    public void printMethodDeclaration(String s, String s1, String s2) {

    }

    @Override
    public void printStaticMethod(String s, String s1, String s2, String s3) {

    }

    @Override
    public void printStaticMethodDeclaration(String s, String s1, String s2) {

    }

    @Override
    public void start(int i, int i1, int i2) {

    }

    @Override
    public void end() {

    }

    @Override
    public void indent() {
        echo("\t");
    }

    @Override
    public void desindent() {

    }

    @Override
    public void startOfLine(int i) {

    }

    @Override
    public void endOfLine() {
        echo("\n");
    }

    @Override
    public void extraLine(int i) {

    }

    @Override
    public void startOfComment() {

    }

    @Override
    public void endOfComment() {

    }

    @Override
    public void startOfJavadoc() {

    }

    @Override
    public void endOfJavadoc() {

    }

    @Override
    public void startOfXdoclet() {

    }

    @Override
    public void endOfXdoclet() {

    }

    @Override
    public void startOfError() {

    }

    @Override
    public void endOfError() {

    }

    @Override
    public void startOfImportStatements() {

    }

    @Override
    public void endOfImportStatements() {

    }

    @Override
    public void startOfTypeDeclaration(String s) {

    }

    @Override
    public void endOfTypeDeclaration() {

    }

    @Override
    public void startOfAnnotationName() {

    }

    @Override
    public void endOfAnnotationName() {

    }

    @Override
    public void startOfOptionalPrefix() {

    }

    @Override
    public void endOfOptionalPrefix() {

    }

    @Override
    public void debugStartOfLayoutBlock() {

    }

    @Override
    public void debugEndOfLayoutBlock() {

    }

    @Override
    public void debugStartOfSeparatorLayoutBlock() {

    }

    @Override
    public void debugEndOfSeparatorLayoutBlock(int i, int i1, int i2) {

    }

    @Override
    public void debugStartOfStatementsBlockLayoutBlock() {

    }

    @Override
    public void debugEndOfStatementsBlockLayoutBlock(int i, int i1, int i2) {

    }

    @Override
    public void debugStartOfInstructionBlockLayoutBlock() {

    }

    @Override
    public void debugEndOfInstructionBlockLayoutBlock() {

    }

    @Override
    public void debugStartOfCommentDeprecatedLayoutBlock() {

    }

    @Override
    public void debugEndOfCommentDeprecatedLayoutBlock() {

    }

    @Override
    public void debugMarker(String s) {

    }

    @Override
    public void debugStartOfCaseBlockLayoutBlock() {

    }

    @Override
    public void debugEndOfCaseBlockLayoutBlock() {

    }
}
