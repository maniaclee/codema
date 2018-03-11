package com.lvbby.codema.app.mybatis;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.core.Machine;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.result.JavaTemplateResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 输入一个JavaBeanEntity，依赖一个SqlTable
 * 生成：
 * 1. dao interface
 * 2. mapper xml
 * 3. mybatis.xml
 * 4. DalConfig.java
 * Created by lipeng on 16/12/23.
 */
@AppTemplateResource($Mapper_.class)
public class MybatisMapperMachine extends AppMachine {

    public static final String tag_select="select";
    public static final String tag_update="update";
    public static final String tag_delete="delete";
    public static final String tag_insert="insert";
    public static final List<String> tags=Lists.newArrayList(tag_delete,tag_insert,tag_select,tag_update);
    public static final List<String> tags_transaction=Lists.newArrayList(tag_delete,tag_insert,tag_update);
    public static final String attribute_parameterType="parameterType";
    public static final String attribute_resultMap="resultMap";
    public static final String attribute_resultType="resultType";
    public static final String attribute_id="id";
    public static final String attribute_inner_return="return";
    public static final String attribute_inner_args="args";

    @Getter
    @Setter
    private Machine<?,String> mapperXmlMachine;
    @Setter
    @Getter
    private Machine<?,SqlTable> sqlTableMachine;

    @Override
    public JavaTemplateResult codeEach(JavaClass cu) throws Exception {

        SqlTable sqlTable = sqlTableMachine.getResult().getResult();
        validate(sqlTable);

        /** 2. 生成mapper interface */
        Document document = read(mapperXmlMachine.getResult().getResult());
        List<JavaMethod> javaMethods = parseMethods(document, source, sqlTable);
        return buildJavaTemplateResult()
                .bind("methods", javaMethods);
    }


    /***
     * 不要用这个方法，太慢：DocumentHelper.parseText
     * @param s
     * @return
     * @throws Exception
     */
    private Document read(String s) throws Exception {
        return reader().read(new StringReader(s));
    }

    private SAXReader reader() throws SAXException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader;
    }


    private List<JavaMethod> parseMethods(Document document, JavaClass entity, SqlTable sqlTable) {
       return visitElements(document.getRootElement())
                .filter(element -> tags.contains(element.getName()))
                .map(element -> {
                    JavaMethod javaMethod = new JavaMethod();
                    //method name
                    javaMethod.setName(id(element));
                    //method return type
                    javaMethod.setReturnType(parseReturnType(document,element, entity, sqlTable));
                    //args
                    javaMethod.setArgs(parseArgs(element, entity, sqlTable));
                    return javaMethod;
                }).collect(Collectors.toList());
    }

    /***
     * 1. 如果是insert/update/delete --->  int
     * 2. return标签 --> 解析return后直接使用
     * 3. resultType的mybatis标签
     * 3. 缺省是entity
     *
     * @param document
     * @param element
     * @param entity
     * @param sqlTable
     * @return
     */
    private JavaType parseReturnType(Document document, Element element, JavaClass entity, SqlTable sqlTable) {
        switch (element.getName()) {
            case tag_insert:
            case tag_delete:
            case tag_update:
                return JavaType.ofClass(int.class);
        }
        String resultMapValue = element.attributeValue(attribute_resultMap);
        if (StringUtils.isNotBlank(resultMapValue)) {
            return JavaType.ofClassName(entity.classFullName());
        }

        String resultType = element.attributeValue(attribute_resultType);
        if (StringUtils.isNotBlank(resultType)) {
            return JavaType.ofClassName(resultType);
        }
        return JavaType.ofClassName(entity.getName());
    }

    private String escape(String s) {
        return s.replace("(", "<").replace(")", ">");
    }

    /***
     * 1. args标签
     */
    private List<JavaArg> parseArgs(Element element, JavaClass entity, SqlTable sqlTable) {
        switch (element.getName()) {
            case tag_insert:
                return Lists.newArrayList(
                        JavaArg.of(entity.getNameCamel(), JavaType.ofClassName(entity.getName())));
            case tag_delete:
            case tag_update:
        }

        String parameterType = element.attributeValue(attribute_parameterType);
        if (StringUtils.isNotBlank(parameterType)) {
            if (StringUtils.equalsIgnoreCase(parameterType, "object")) {
                return Lists.newArrayList(
                        JavaArg.of(entity.getNameCamel(), JavaType.ofClassName(entity.getName())));
            }
            if (StringUtils.equalsIgnoreCase(parameterType, "map")) {
                parseArgsAsMap(element, entity, sqlTable);
            }
            List<String> vars = parseVars(element);
            Validate.isTrue(vars.size() == 1, "only one var can be given");
            return Lists.newArrayList(argWithParam(vars.get(0), JavaType.ofClassName(escape(parameterType))));
        }
        return parseArgsAsMap(element, entity, sqlTable);
    }

    /***
     * 构造 @Param(value="xxx")int xxx 的arg
     * @param name
     * @param javaType
     * @return
     */
    private JavaArg argWithParam(String name, JavaType javaType) {
        JavaArg arg = JavaArg.of(name, javaType);
        arg.addAnnotation(new JavaAnnotation("Param").add("value", name));
        return arg;
    }

    private List<JavaArg> parseArgsAsMap(Element element, JavaClass entity, SqlTable sqlTable) {
        //查找所有#{}变量
        List<String> args = parseVars(element);
        return args.stream().map(arg -> {
            JavaType type = entity.getFields().stream().filter(field -> field.getName().equals(arg))
                    .map(JavaField::getType).findAny().orElseThrow(() -> new RuntimeException(
                            String.format("unknown arg:%s,element[%s]", arg,element)));
            return argWithParam(arg, type);
        }).collect(Collectors.toList());
    }

    private List<String> parseVars(Element element) {
        List<String> re = Lists.newLinkedList();
        parseVar(element, re);
        return re;
    }

    private void parseVar(Element e, List<String> result) {
        if (e.getName().equalsIgnoreCase("foreach")) {
            String collection = e.attributeValue("collection");
            if (!Lists.newArrayList("list", "array").contains(collection)) {
                result.add(collection);
            }
        } else {
            ReflectionUtils.findAll(e.getText(), "#\\{([^#\\{\\}]+)\\}", matcher -> matcher.group(1)).forEach(s -> result.add(s));
        }
        for (Object child : e.elements()) {
            parseVar((Element) child, result);
        }
    }


    private void validate(SqlTable sqlTable) {
        Validate.notNull(sqlTable.getPrimaryKey(),
                "no primary key found for table : " + sqlTable.getNameInDb());
    }

    private String id(Element element){
        return element.attributeValue(attribute_id);
    }

    private Stream<Element> visitElements(Element element){
        List<Element> re = Lists.newLinkedList();
        if(element!=null){
            for (Object o : element.elements()) {
                re.add((Element) o);
            }
        }
        return re.stream();
    }

}
