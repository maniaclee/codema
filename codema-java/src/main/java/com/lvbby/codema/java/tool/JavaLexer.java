package com.lvbby.codema.java.tool;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.Token;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.common.collect.Lists;
import com.lvbby.codema.java.entity.JavaAnnotation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/16.
 */
public class JavaLexer {
    public static CompilationUnit read(String code) {
        return JavaParser.parse(code);
    }

    public static CompilationUnit read(File code) throws Exception {
        return JavaParser.parse(IOUtils.toString(new FileInputStream(code)));
    }

    public static TypeDeclaration<?> parseJavaClass(String code) {
        return read(code).getType(0);
    }

    public static List<FieldDeclaration> getFields(CompilationUnit cu) {
        if (CollectionUtils.isEmpty(cu.getTypes()) || cu.getTypes().size() < 1)
            return Lists.newLinkedList();
        return getFields(cu.getType(0));
    }

    public static Optional<ClassOrInterfaceDeclaration> getClass(CompilationUnit cu) {
        return Optional.ofNullable(cu).map(CompilationUnit::getTypes).filter(typeDeclarations -> typeDeclarations.size() > 0).map(typeDeclarations -> (ClassOrInterfaceDeclaration) typeDeclarations.get(0));
    }

    public static BodyDeclaration parseMethod(String code) {
        return JavaParser.parseInterfaceBodyDeclaration(code);
    }


    public static List<FieldDeclaration> getFields(TypeDeclaration<?> cu) {
        return cu.getFields().stream().filter(f -> isProperty(f)).collect(Collectors.toList());
    }

    public static <T extends Node> T ensureComment(T node, boolean lineComment) {
        if (node != null && node.getComment() == null) {
            node.setComment(lineComment ? new LineComment() : new BlockComment());
        }
        return node;
    }

    public static String getComment(Comment comment){
        return Optional.ofNullable(comment).map(c->{
            /**
             *  * 序列化后的machine
             * @see MachineSerialConfig
             *
             */
            String content = c.getContent();
            content = content.replaceAll("@.*","");
            content=content.replaceAll("\\s*\\*+\\s*","");
            return StringUtils.trim(content);
        }).orElse(null);
    }

    public static <T extends Node> T ensureComment(T node) {
        return ensureComment(node, true);
    }

    public static JavaAnnotation parseAnnotation(AnnotationExpr expr) {
        JavaAnnotation javaAnnotation = new JavaAnnotation(expr.getNameAsString());
        if (expr instanceof SingleMemberAnnotationExpr) {
            return javaAnnotation.add("value", trimString(((SingleMemberAnnotationExpr) expr).getMemberValue()));
        }
        if (expr instanceof NormalAnnotationExpr) {
            ((NormalAnnotationExpr) expr).getPairs().forEach(memberValuePair -> {
                if (memberValuePair.getValue() instanceof ArrayInitializerExpr) {
                    ArrayInitializerExpr value = (ArrayInitializerExpr) memberValuePair.getValue();
                    value.getValues().forEach(expression -> javaAnnotation.add(memberValuePair.getNameAsString(), trimString(expression)));
                } else {
                    javaAnnotation.add(memberValuePair.getNameAsString(), trimString(memberValuePair.getValue()));
                }
            });
        }
        return javaAnnotation;
    }

    private static String trimString(Expression expression) {
        String s = expression.toString();
        if (s.startsWith("\"") && s.endsWith("\""))
            s = s.substring(1, s.length() - 1);
        return s;
    }


    private static TokenRange tokenRange(String s, Position begin, Position end) {
        JavaToken javaToken = new JavaToken(Token.newToken(1, s), Lists.newArrayList());
        return new TokenRange(javaToken, javaToken);
    }

    public static void appendFieldCommentAdEnd(FieldDeclaration fieldDeclaration, String comment) {
        VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(fieldDeclaration.getVariables().size() - 1);
        variableDeclarator.setComment(new BlockComment(variableDeclarator.getComment().map(comment1 -> comment1.getContent() + comment).orElse(comment)));
    }

    public static void addComment(Node node, String s) {
        addComment(node, true, s);
    }

    public static void addComment(Node node, boolean append, String s) {
        Validate.notNull(node, "node can't be null");
        if (!node.getComment().isPresent())
            node.setComment(new LineComment(""));
        Comment comment = node.getComment().get();
        if (append)
            comment.setContent(comment.getContent() + "\n" + s);
        else
            comment.setContent(s + "\n" + comment.getContent());
    }

    public static List<MethodDeclaration> getMethods(TypeDeclaration<?> cu) {
        ClassOrInterfaceDeclaration classOrInterfaceType = (ClassOrInterfaceDeclaration) cu;
        if (classOrInterfaceType.isInterface()) {
            return getMethods(cu, null);
        }
        return getMethods(cu, Modifier.PUBLIC);
    }

    public static List<MethodDeclaration> getMethods(TypeDeclaration<?> cu, Modifier... modifiers) {
        List<Modifier> ms = modifiers == null || modifiers.length == 0 ? null : Lists.newArrayList(modifiers);
        return cu.getMethods().stream().filter(m -> ms == null || m.getModifiers().containsAll(ms)).collect(Collectors.toList());
    }

    public static List<MethodDeclaration> getMethodByName(TypeDeclaration<?> cu, String methodName) {
        return getMethods(cu).stream().filter(methodDeclaration -> methodDeclaration.getNameAsString().equals(methodName)).collect(Collectors.toList());
    }

    public static MethodDeclaration getMethodByNameSingle(TypeDeclaration<?> cu, String methodName) {
        return getMethods(cu).stream().filter(methodDeclaration -> methodDeclaration.getNameAsString().equals(methodName)).findFirst().orElse(null);
    }

    public static boolean isProperty(FieldDeclaration n) {
        return !n.isStatic() && !n.isTransient();
    }

    public static String getFieldName(FieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getVariable(0).getNameAsString();
    }

    public static String getFieldGetterName(FieldDeclaration fieldDeclaration) {
        return camel("get", getFieldName(fieldDeclaration));
    }

    public static String getFieldSetterName(FieldDeclaration fieldDeclaration) {
        return camel("set", getFieldName(fieldDeclaration));
    }

    public static String camel(String s, String... ss) {
        if (ss == null || ss.length == 0)
            return StringUtils.uncapitalize(s);
        return s.toLowerCase() + Lists.newArrayList(ss).stream().map(e -> StringUtils.capitalize(e)).collect(Collectors.joining());
    }

    public static ClassOrInterfaceType type(String s) {
        return new ClassOrInterfaceType(s);
    }

    public static String list(String clz) {
        return String.format("List<%s>", clz);
    }

    public static ClassOrInterfaceType listType(String clz) {
        return new ClassOrInterfaceType(list(clz));
    }


    public static VariableDeclarationExpr declareNewVarConstructor(ClassOrInterfaceType type, String var) {
        return declareNewVar(type, var, new ObjectCreationExpr(null, type, new NodeList<>()));
    }

    public static VariableDeclarationExpr declareNewVar(ClassOrInterfaceType type, String var, Expression expression) {
        return new VariableDeclarationExpr(new VariableDeclarator(type, var, expression));
    }


    public static Expression newVar(ClassOrInterfaceType type, Expression... expressions) {
        return new ObjectCreationExpr(null, type, NodeList.nodeList(expressions));
    }


    public static String methodReturnType(MethodDeclaration m) {
        String s = m.getType().toString();
        return "void".equalsIgnoreCase(s) ? null : s;
    }

    public static List<Expression> getMethodParameterVars(MethodDeclaration m) {
        return m.getParameters().stream().map(p -> new NameExpr(p.getName())).collect(Collectors.toList());
    }

    public static ClassOrInterfaceDeclaration addField(ClassOrInterfaceDeclaration testClass, TypeDeclaration typeDeclaration) {
        testClass.addField(typeDeclaration.getNameAsString(), JavaLexer.camel(typeDeclaration.getNameAsString()), Modifier.PRIVATE);
        return testClass;
    }

    /***
     * gen parameters' instances
     *
     * @param m
     * @return
     */
    public static List<Expression> newInstanceForDefaultValue(MethodDeclaration m) {
        return m.getParameters().stream().map(p -> newInstanceForDefaultValue(p.getType().toString())).collect(Collectors.toList());
    }


    private static Map<String,String> defaultValueMap = new HashMap(){
        {
            put(int.class.getSimpleName(),"0");
            put(short.class.getSimpleName(),"0");
            put(double.class.getSimpleName(),"0");
            put(float.class.getSimpleName(),"0f");
            put(byte.class.getSimpleName(),"0");
            put(Integer.class.getSimpleName(),"0");
            put(long.class.getSimpleName(),"0l");
            put(Long.class.getSimpleName(),"0l");
            put(boolean.class.getSimpleName(),"true");
            put(BigDecimal.class.getSimpleName(),"BigDecimal.ZERO");
            put(String.class.getSimpleName(),"\"\"");
            put("collection","new ArrayList()");
            put("list","new ArrayList()");
            put("iterable","new ArrayList()");
        }
    };
    /***
     * gen instance for given type : like String ->"" , int -> 1, List -> new ArrayList()
     *
     * @param type
     * @return
     */
    public static Expression newInstanceForDefaultValue(String type) {
        String lowerCase = type.toLowerCase().replaceAll("<[^>]+>", "").trim();//remove generic type
        String defaultValue = defaultValueMap.get(lowerCase);
        if(defaultValue!=null){
            return new NameExpr(defaultValue);
        }
        return newVar(type(type));
    }

    public static <T extends NodeWithAnnotations> T addAnnotationWithImport(T t, Class annotation) {
        t.addAnnotation(annotation);
        if (t instanceof Node) {
            CompilationUnit ancestorOfType = getAncestorOfType((Node) t, CompilationUnit.class);
            if (ancestorOfType != null) {
                if (hasImport(ancestorOfType, annotation))
                    ancestorOfType.addImport(annotation);
            }
        }
        return t;
    }

    public static boolean hasImport(CompilationUnit compilationUnit, Class clz) {
        return compilationUnit.getImports().stream()
                .filter(im -> !im.isStatic() && im.getNameAsString().equalsIgnoreCase(clz.getName()))
                .findFirst().isPresent();
    }

    public static <N> N getAncestorOfType(Node node, Class<N> classType) {
        Node parent = node.getParentNode().orElse(null);
        while (parent != null) {
            if (classType.isAssignableFrom(parent.getClass())) {
                return classType.cast(parent);
            }
            parent = parent.getParentNode().orElse(null);
        }
        return null;
    }

    public static String getPackage(CompilationUnit compilationUnit) {
        return compilationUnit.getPackageDeclaration().map(packageDeclaration -> packageDeclaration.getNameAsString()).orElse("");
    }

    public static List<String> getImports(CompilationUnit compilationUnit) {
        return compilationUnit.getImports().stream().map(i -> i.toString().replaceAll("import", "").trim()).collect(Collectors.toList());
    }

    public static String getFullClassName(CompilationUnit unit) {
        if (unit == null)
            return null;
        String pack = unit.getPackageDeclaration()
                .map(packageDeclaration -> packageDeclaration.getNameAsString())
                .map(s -> StringUtils.isBlank(s) ? "" : (s + "."))
                .orElse("");
        return getClass(unit).map(classOrInterfaceDeclaration -> pack + classOrInterfaceDeclaration.getNameAsString()).orElse(null);
    }
}
