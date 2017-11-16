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
import com.lvbby.codema.java.result.JavaXmlTemplateResult;
import com.lvbby.codema.java.template.TemplateContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        //mapper template xml
        Document document = new SAXReader().read(mapperTemplate.getInputStream());
        /** 1. mapper xml */
        BasicResult mapperXml = new XmlTemplateResult(document)
                .bind("mapper", config.parseDestClassFullName(cu))
                .bind("resultClass", cu.classFullName())
                .filePath(config.getDestResourceRoot(), config.getMapperDir(),
                        String.format("%sMapper.xml",
                                ((JavaClass) codemaContext.getSource()).getName()));
        config.handle(codemaContext, mapperXml);

        /** 2. 根据mapper xml 生成mapper */
        List<JavaMethod> javaMethods = parseMethods(DocumentHelper.parseText(mapperXml.getString()), cu, sqlTable);
        javaMethods.forEach(System.out::println);
        /** mapper interface */
        JavaTemplateResult mapper = new JavaTemplateResult(config, $Mapper_.class, cu)
                .bind("methods", javaMethods);
        config.handle(codemaContext,mapper);
    }

    private List<JavaMethod> parseMethods(Document document, JavaClass entity, SqlTable sqlTable) {
        List<JavaMethod> re = Lists.newArrayList();
        for (Object o : document.getRootElement().elements()) {
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

    private JavaType parseReturnType(Element element, JavaClass entity, SqlTable sqlTable) {
        String resultType = element.attributeValue("resultType");
        String sqlType = element.getName();
        if(Lists.newArrayList("insert","update","delete").contains(sqlType)){
            return JavaType.ofClass(int.class);
        }
        if(StringUtils.isNotBlank(resultType)){
            return JavaType.ofClassName(resultType);
        }
        return JavaType.ofClassName(entity.getName());
    }

    private List<JavaArg> parseArgs(Element element, JavaClass entity, SqlTable sqlTable) {
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
            return Lists.newArrayList(argWithParam(vars.get(0), JavaType.ofClassName(parameterType)));
        }
        return parseArgsAsMap(element, entity,sqlTable);
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
        return ReflectionUtils
                    .findAll(element.getText(), "#\\{([^#\\{\\}]+)\\}", matcher -> matcher.group(1));
    }

    public void preCode(CodemaContext codemaContext, JavaMybatisCodemaConfig config)
            throws Exception {
        /**mybatis config*/

        config.handle(codemaContext, new BasicResult().result(loadResourceAsString("mybatis.xml"))
                .filePath(config.getDestResourceRoot(), "mybatis.xml")
                .writeMode(WriteMode.writeIfNoExist));

        /** dal config */
        if (config.isNeedConfigClass()) {
            config.handle(codemaContext, new JavaTemplateResult(
                    new TemplateContext(DalConfig.class, config).pack(config.getConfigPackage()))
                    .writeMode(WriteMode.writeIfNoExist));
        }
    }

    private void validate(SqlTable sqlTable) {
        Validate.notNull(sqlTable.getPrimaryKeyField(),
                "no primary key found for table : " + sqlTable.getNameInDb());
    }

    public static void main(String[] args) throws Exception {
        new SAXReader().read(new FileInputStream("/Users/dushang.lp/workspace/test-codema/src/main/resources/mapper/ArticleMapper.xml"));
    }
}
