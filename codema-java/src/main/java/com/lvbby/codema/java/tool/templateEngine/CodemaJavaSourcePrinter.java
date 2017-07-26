package com.lvbby.codema.java.tool.templateEngine;

import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

/**
 * Created by dushang.lp on 2017/7/21.
 */
public class CodemaJavaSourcePrinter {

    public static DataKey<String> dataKey_fieldAppend = CodemaDataKey.instance();
    private final PrettyPrinterConfiguration configuration;

    public static CodemaJavaSourcePrinter instance = new CodemaJavaSourcePrinter();

    public CodemaJavaSourcePrinter() {
        this(new PrettyPrinterConfiguration());
    }

    public CodemaJavaSourcePrinter(PrettyPrinterConfiguration configuration) {
        this.configuration = configuration;
    }

    public String print(Node node) {
        final PrettyPrintVisitor visitor = new MyPrintVisitory(configuration);
        node.accept(visitor, null);
        return visitor.getSource();
    }

    public static String toJavaSource(Node node) {
        return instance.print(node);
    }

}
