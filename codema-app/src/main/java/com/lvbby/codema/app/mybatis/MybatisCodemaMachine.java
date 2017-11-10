package com.lvbby.codema.app.mybatis;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.render.TemplateEngineResult;
import com.lvbby.codema.core.resource.Resource;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.WriteMode;
import com.lvbby.codema.core.tool.mysql.entity.SqlTable;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import com.lvbby.codema.java.result.JavaXmlTemplateResult;
import com.lvbby.codema.java.template.TemplateContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;
import java.util.regex.Pattern;
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
        Document mapper = new SAXReader().read(mapperTemplate.getInputStream());
        parseMethods(mapper, cu, sqlTable);
        TemplateEngineResult daoTemplateResult = new JavaTemplateResult(config, $Dao_.class, cu)
                .bind("table", sqlTable);
        config.handle(codemaContext, daoTemplateResult);

        String xml = IOUtils
                .toString(MybatisCodemaMachine.class.getResourceAsStream("mybatis_dao.xml"));
        config.handle(codemaContext,
                JavaXmlTemplateResult.ofResource(config, xml, cu).bind("table", sqlTable)
                        .bind("dao", daoTemplateResult.getResult())
                        .filePath(config.getDestResourceRoot(), config.getMapperDir(),
                                String.format("%sMapper.xml",
                                        ((JavaClass) codemaContext.getSource()).getName())));
    }

    private List<JavaMethod> parseMethods(Document document, JavaClass entity, SqlTable sqlTable) {
        for (Object o : document.getRootElement().elements()) {
            Element element = (Element) o;
            String sqlType = element.getName();
            JavaMethod javaMethod = new JavaMethod();
            javaMethod.setName(element.attributeValue("id"));
            if (StringUtils.equalsIgnoreCase(sqlType, "insert")) {
                JavaArg javaArg = new JavaArg();
                javaArg.setName(entity.getNameCamel());
                javaArg.setType(JavaType.ofClassName(entity.getName()));
                javaMethod.setArgs(Lists.newArrayList(javaArg));
                javaMethod.setReturnType(
                        JavaType.ofClass(sqlTable.getPrimaryKeyField().getJavaType()));
            }
            if (StringUtils.equalsIgnoreCase(sqlType, "select")) {
                javaMethod.setArgs(parseArgs(element,entity,sqlTable));
                javaMethod.setReturnType(
                        JavaType.ofClass(sqlTable.getPrimaryKeyField().getJavaType()));
            }
        }
        return null;
    }

    private List<JavaArg> parseArgs(Element element, JavaClass entity, SqlTable sqlTable) {
        String parameterType = element.attributeValue("parameterType");
        if (StringUtils.isNotBlank(parameterType)) {
            if (StringUtils.equalsIgnoreCase(parameterType, "object")) {
                return Lists.newArrayList(
                        JavaArg.of(entity.getNameCamel(), JavaType.ofClassName(entity.getName())));
            }
            return Lists.newArrayList(
                    JavaArg.of(entity.getNameCamel(), JavaType.ofClassName(parameterType)));
        }
        Pattern.compile("[#$]\\{([#\\{\\}]+)\\}").matcher(element.getText());
        List<String> args = ReflectionUtils
                .findAll(element.getText(), "[#$]\\{([#\\{\\}]+)\\}", matcher -> matcher.group(1));
        if (CollectionUtils.isEmpty(args)) {
            return null;
        }
        return args.stream().map(arg -> {
            JavaType type = entity.getFields().stream().filter(field -> field.getName().equals(arg))
                    .map(JavaField::getType).findAny().orElseThrow(() -> new RuntimeException(
                            String.format("unknown arg:%s", arg)));
            return JavaArg.of(type.getName(), type);
        }).collect(Collectors.toList());
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

}
