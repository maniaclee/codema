package com.lvbby.codema.app.mybatis;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.resource.Resource;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaAnnotation;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.template.TemplateContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 * 产生dao和mapper.xml
 */
public class MybatisCodemaMachine extends AbstractJavaCodemaMachine<MybatisCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, MybatisCodemaConfig config, JavaClass cu)
            throws Exception {
        SqlTable sqlTable = codemaContext.getCodemaBeanFactory().getBean(SqlTable.class);
        validate(sqlTable);

        Resource mapperTemplate = config.getMapperXmlTemplates().stream()
                .filter(resource -> StringUtils
                        .equals(config.getTable2mapperName().apply(sqlTable.getNameInDb()),
                                resource.getResourceName())).findAny().orElse(null);
        if (mapperTemplate == null) {
            return;
        }
        /** 1. 渲染mapper xml ， 替换变量*/
        String renderXml = new XmlTemplateResult(read(mapperTemplate.getInputStream()))
                .bind("mapper", config.parseDestClassFullName(cu))
                .bind("resultClass", cu.classFullName()).getString();

        Document document = read(renderXml);
        List<JavaMethod> javaMethods = parseMethods(document, cu, sqlTable);
        javaMethods.forEach(System.out::println);

        /** 2. 根据mapper xml 生成mapper */
        BasicResult mapperXml = new XmlTemplateResult(document)
                .filePath(config.getDestResourceRoot(), config.getMapperDir(),
                        String.format("%sMapper.xml",
                                ((JavaClass) codemaContext.getSource()).getName()));
        config.handle(codemaContext, mapperXml);

        /** 3. 生成mapper interface */
        config.handle(codemaContext,
                new JavaTemplateResult(config, $Mapper_.class, cu).bind("methods", javaMethods));
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
    private Document read(InputStream inputStream) throws Exception {
        return reader().read(inputStream);
    }
    private SAXReader reader() throws SAXException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return reader;
    }


    private List<JavaMethod> parseMethods(Document document, JavaClass entity, SqlTable sqlTable) {
        List<JavaMethod> re = Lists.newArrayList();
        for(Object o : document.getRootElement().elements()){
            Element element = (Element) o;
            JavaMethod javaMethod = new JavaMethod();
            //method name
            javaMethod.setName(element.attributeValue("id"));
            //method return type
            javaMethod.setReturnType(parseReturnType(element,entity,sqlTable));
            //args
            javaMethod.setArgs(parseArgs(element,entity,sqlTable));
            re.add(javaMethod);
        }
        return re;
    }

    /***
     * 1. 如果是insert/update/delete --->  int
     * 2. return标签 --> 解析return后直接使用
     * 3. resultType的mybatis标签
     * 3. 缺省是entity
     * @param element
     * @param entity
     * @param sqlTable
     * @return
     */
    private JavaType parseReturnType(Element element, JavaClass entity, SqlTable sqlTable) {
        String returnType = element.attributeValue("return");
        if(Lists.newArrayList("insert","update","delete").contains(element.getName())){
            return JavaType.ofClass(int.class);
        }
        if(StringUtils.isNotBlank(returnType)){
            return JavaType.ofClassName(escape(returnType));
        }
        String resultType = _innerXmlAttribute(element,"return");
        if(StringUtils.isNotBlank(resultType)){
            return JavaType.ofClassName(resultType);
        }
        return JavaType.ofClassName(entity.getName());
    }
    private String escape(String s){
        return s.replace("(","<").replace(")",">");
    }

    /***
     * 1. args标签
     */
    private List<JavaArg> parseArgs(Element element, JavaClass entity, SqlTable sqlTable) {
        String args = _innerXmlAttribute(element,"args");
        if(args!=null){
            return Arrays.stream(args.trim().split(",")).map(s -> {
                String[] split = s.trim().split("\\s+");
                return JavaArg.of(split[1], JavaType.ofClassName(split[0]));
            }).collect(Collectors.toList());
        }

        String parameterType = element.attributeValue("parameterType");
        if (StringUtils.isNotBlank(parameterType)) {
            if (StringUtils.equalsIgnoreCase(parameterType, "object")) {
                return Lists.newArrayList(
                        JavaArg.of(entity.getNameCamel(), JavaType.ofClassName(entity.getName())));
            }
            if (StringUtils.equalsIgnoreCase(parameterType, "map")) {
                parseArgsAsMap(element, entity,sqlTable);
            }
            List<String> vars = parseVars(element);
            Validate.isTrue(vars.size()==1,"only one var can be given");
            return Lists.newArrayList(argWithParam(vars.get(0), JavaType.ofClassName(escape(parameterType))));
        }
        return parseArgsAsMap(element, entity,sqlTable);
    }

    /***
     * 解析内部标签后然后删除
     */
    private String _innerXmlAttribute(Element element,String s ){
        Attribute attribute = element.attribute(s);
        if(attribute!=null){
            String value = attribute.getValue();
            element.remove(attribute);
            return escape(value);
        }
        return null;
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

    private List<JavaArg> parseArgsAsMap(Element element, JavaClass entity,SqlTable sqlTable) {
        //查找所有#{}变量
        List<String> args = parseVars(element);
        return args.stream().map(arg -> {
            JavaType type = entity.getFields().stream().filter(field -> field.getName().equals(arg))
                    .map(JavaField::getType).findAny().orElseThrow(() -> new RuntimeException(
                            String.format("unknown arg:%s", arg)));
            return argWithParam(arg, type);
        }).collect(Collectors.toList());
    }

    private List<String> parseVars(Element element) {
        List<String> re = Lists.newLinkedList();
        parseVar(element, re);
        return re;
    }

    private void parseVar(Element e , List<String> result){
        if(e.getName().equalsIgnoreCase("foreach")){
            String collection = e.attributeValue("collection");
            if(!Lists.newArrayList("list","array").contains(collection)){
                result.add(collection);
            }
        }else{
            ReflectionUtils.findAll(e.getText(), "#\\{([^#\\{\\}]+)\\}", matcher -> matcher.group(1)).forEach(s -> result.add(s));
        }
        for(Object child : e.elements()){
            parseVar((Element) child,result);
        }
    }

    public void preCode(CodemaContext codemaContext, MybatisCodemaConfig config)
            throws Exception {
        /**mybatis config*/

        config.handle(codemaContext, new BasicResult().result(loadResourceAsString("mybatis.xml"))
                .filePath(config.getDestResourceRoot(), "mybatis.xml")
                .writeMode(WriteMode.writeIfNoExist));

        /** dal config */
        if (StringUtils.isNotBlank(config.getConfigPackage())) {
            config.handle(codemaContext,
                new JavaTemplateResult(
                    new TemplateContext(DalConfig.class, config).pack(config.getConfigPackage()))
                        .writeMode(WriteMode.writeIfNoExist));
        }
    }

    private void validate(SqlTable sqlTable) {
        Validate.notNull(sqlTable.getPrimaryKeyField(),
                "no primary key found for table : " + sqlTable.getNameInDb());
    }

    public static void main(String[] args) throws Exception {
    }
}
