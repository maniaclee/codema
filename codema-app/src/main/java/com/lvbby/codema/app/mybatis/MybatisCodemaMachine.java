package com.lvbby.codema.app.mybatis;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.render.XmlTemplateResult;
import com.lvbby.codema.core.resource.Resource;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.core.tool.mysql.entity.SqlColumn;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 输入一个JavaBeanEntity，依赖一个SqlTable
 * Created by lipeng on 16/12/23.
 * 产生dao和mapper.xml
 */
public class MybatisCodemaMachine extends AbstractJavaCodemaMachine<JavaClass,Object> {

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

    private String mapperDir;
    private String configPackage;
    private List<Resource> mapperXmlTemplates;
    /***
     * 表名到mapper xml的映射关系
     */
    private Function<String,String> table2mapperName = s -> s + ".xml";
    @Override protected void doCode() throws Exception {
        JavaClass cu = source;
        SqlTable sqlTable = codemaContext.getCodemaBeanFactory().getBean(SqlTable.class);
        validate(sqlTable);
        preCode();
        Resource mapperTemplate = getMapperXmlTemplates().stream()
                .filter(resource -> StringUtils
                        .equals(getTable2mapperName().apply(sqlTable.getNameInDb()),
                                resource.getResourceName())).findAny().orElse(null);
        if (mapperTemplate == null) {
            return;
        }
        /** 1. 渲染mapper xml ， 替换变量*/
        String renderXml = new XmlTemplateResult(read(mapperTemplate.getInputStream()))
                .bind("mapper", parseDestClassFullName(cu))
                .bind("resultClass", cu.classFullName()).getString();

        Document document = read(renderXml);
        /** 处理预设模板 */
        processPresetTemplate(document,cu,sqlTable);

        List<JavaMethod> javaMethods = parseMethods(document, cu, sqlTable);
        /** 3. 生成mapper interface */
        handle(
                new JavaTemplateResult(this, $Mapper_.class, cu).bind("methods", javaMethods));

        /** 2. 根据mapper xml 生成mapper */
        //预设的模板处理
        BasicResult mapperXml = new XmlTemplateResult(document)
                .filePath(getDestResourceRoot(), getMapperDir(),
                        String.format("%s.xml",sqlTable.getName()));
        handle(mapperXml);

    }

    public void preCode()
            throws Exception {
        /**mybatis config*/

        handle(new BasicResult().result(loadResourceAsString("mybatis.xml"))
                .filePath(getDestResourceRoot(), "mybatis.xml")
                .writeMode(WriteMode.writeIfNoExist));

        /** dal config */
        if (StringUtils.isNotBlank(getConfigPackage())) {
            handle( new JavaTemplateResult( this,DalConfig.class,source)
                            .writeMode(WriteMode.writeIfNoExist));
        }
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
        //提取resultMap信息
        Element resultMap = document.getRootElement().element(attribute_resultMap);
        String resultMapName = Optional.ofNullable(resultMap).map(element1 -> element1.attributeValue("id")).orElse(null);
        String resultMapType = Optional.ofNullable(resultMap).map(element1 -> element1.attributeValue("type")).orElse(null);

        String returnType = _innerXmlAttribute(element, attribute_inner_return);
        if (tags_transaction.contains(element.getName())) {
            return JavaType.ofClass(int.class);
        }
        if (StringUtils.isNotBlank(returnType)) {
            return JavaType.ofClassName(escape(returnType));
        }

        String resultMapValue = element.attributeValue(attribute_resultMap);
        if (StringUtils.equals(resultMapName, resultMapValue)) {
            return JavaType.ofClassName(resultMapType);
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
        String args = _innerXmlAttribute(element, attribute_inner_args);
        if (args != null) {
            return Arrays.stream(args.trim().split(",")).map(s -> {
                String[] split = s.trim().split("\\s+");
                return JavaArg.of(split[1], JavaType.ofClassName(split[0]));
            }).collect(Collectors.toList());
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
     * 解析内部标签后然后删除
     */
    private String _innerXmlAttribute(Element element, String s) {
        Attribute attribute = element.attribute(s);
        if (attribute != null) {
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

    private List<JavaArg> parseArgsAsMap(Element element, JavaClass entity, SqlTable sqlTable) {
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
        Validate.notNull(sqlTable.getPrimaryKeyField(),
                "no primary key found for table : " + sqlTable.getNameInDb());
    }
    protected void processPresetTemplate(Document document, JavaClass cu, SqlTable table){
        Element rootElement = document.getRootElement();
        //insert
        Element insert = rootElement.element(tag_insert);
        if(insert!=null && StringUtils.isBlank(insert.getText())){
            String insertSql = String.format("insert into %s(%s) values(%s)",
                    table.getNameInDb(),
                    table.getFields().stream().map(SqlColumn::getNameInDb).collect(Collectors.joining(",")),
                    table.getFields().stream().map(sqlColumn -> String.format("#{%s}",sqlColumn.getNameCamel())).collect(Collectors.joining(","))
            );
            insert.setText(insertSql);
        }
        //update
        Element update = rootElement.element(tag_update);
        if(update!=null && StringUtils.isBlank(update.getText())){
            update.setText(String.format("\nupdate %s set\n", table.getNameInDb()));
            table.getFields().stream()
                    .forEach(sqlColumn -> {
                        DefaultElement result = new DefaultElement("if");
                        result.addAttribute("test", String.format("%s !=null", sqlColumn.getNameCamel()));
                        result.setText(String.format("%s = #{%s}", sqlColumn.getNameInDb(),sqlColumn.getNameCamel()));
                        update.add(result);
                    });
            update.addAttribute(attribute_parameterType,"object");
        }
        //resultMap
        Element resultMap = rootElement.element(attribute_resultMap);
        if(resultMap!=null && StringUtils.isBlank(resultMap.getText())){
                    //"<result property=\"%s\" column=\"%s\" javaType=\"%s\" jdbcType=\"%s\"/>"
             table.getFields().stream()
                .forEach(sqlColumn -> {
                    DefaultElement result = new DefaultElement("result");
                    result.addAttribute("property", sqlColumn.getNameCamel());
                    result.addAttribute("column", sqlColumn.getNameInDb());
                    result.addAttribute("javaType", sqlColumn.getJavaType().getName());
                    result.addAttribute("jdbcType", sqlColumn.getDbType());
                    resultMap.add(result);
                });
        }
        //springData style
        visitElements(rootElement)
                .filter(element -> CollectionUtils.isEmpty(element.elements())&&StringUtils.isBlank(element.getText()))
                .forEach(element -> {
                    System.out.println(element);
                    SqlBuilder sql = SqlBuilder
                            .parseFromSpringDataStyle(id(element));
                    element.setText(sql.getSql(table.getNameInDb()));
                    if(StringUtils.isBlank(element.attributeValue(attribute_resultMap))&&StringUtils.isBlank(element.attributeValue("resultType"))){
                        if(resultMap!=null){
                            element.addAttribute(attribute_resultMap,id(resultMap));
                        }else{
                            element.addAttribute(attribute_resultType,cu.classFullName());
                        }
                    }
                });
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

    /**
     * Getter method for property   mapperDir.
     *
     * @return property value of mapperDir
     */
    public String getMapperDir() {
        return mapperDir;
    }

    /**
     * Setter method for property   mapperDir .
     *
     * @param mapperDir  value to be assigned to property mapperDir
     */
    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }

    /**
     * Getter method for property   configPackage.
     *
     * @return property value of configPackage
     */
    public String getConfigPackage() {
        return configPackage;
    }

    /**
     * Setter method for property   configPackage .
     *
     * @param configPackage  value to be assigned to property configPackage
     */
    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    /**
     * Getter method for property   mapperXmlTemplates.
     *
     * @return property value of mapperXmlTemplates
     */
    public List<Resource> getMapperXmlTemplates() {
        return mapperXmlTemplates;
    }

    /**
     * Setter method for property   mapperXmlTemplates .
     *
     * @param mapperXmlTemplates  value to be assigned to property mapperXmlTemplates
     */
    public void setMapperXmlTemplates(List<Resource> mapperXmlTemplates) {
        this.mapperXmlTemplates = mapperXmlTemplates;
    }

    /**
     * Getter method for property   table2mapperName.
     *
     * @return property value of table2mapperName
     */
    public Function<String, String> getTable2mapperName() {
        return table2mapperName;
    }

    /**
     * Setter method for property   table2mapperName .
     *
     * @param table2mapperName  value to be assigned to property table2mapperName
     */
    public void setTable2mapperName(Function<String, String> table2mapperName) {
        this.table2mapperName = table2mapperName;
    }
}
