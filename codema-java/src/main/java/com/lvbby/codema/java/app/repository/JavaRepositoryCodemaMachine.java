package com.lvbby.codema.java.app.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaArg;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaRepositoryCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaRepositoryCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaRepositoryCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        JavaClass buildUtil = codemaContext.getCodema().getCodemaBeanFactory().getBean(config.getConvertUtilsClass());
        Validate.notNull(buildUtil, "buildClass not found");

        List<RepositoryMethod> collect = javaClass.getMethods().stream().map(javaMethod -> new RepositoryMethod(javaMethod, buildUtil)).collect(Collectors.toList());
        config.handle(codemaContext, config, new JavaTemplateResult(config, $Repository_.class, javaClass, ImmutableMap.of(
                "methods", collect,
                "Repository",ObjectUtils.firstNonNull(ScriptEngineFactory.instance.eval(config.getDestClassName(), javaClass.getName()), javaClass.getName()+"Repository")
        )));
    }


    /***
     * insert(Entity entity) ---> insert(Entity dto){Dto a = BuildUtils.buildEntity(dto);}
     */
    public static class RepositoryMethod {
        private JavaMethod javaMethod;
        private JavaMethod buildReturnMethod;
        private List<JavaMethod> buildParameterMethods = Lists.newLinkedList();
        private JavaClass buildClass;

        public RepositoryMethod(JavaMethod javaMethod, JavaClass buildClass) {
            this.javaMethod = ReflectionUtils.copy(javaMethod,JavaMethod.class);
            this.buildClass = buildClass;
            //收集build类里的信息
            Map<JavaType, JavaMethod> byParameter = buildClass.getMethods().stream().collect(Collectors.toMap(m -> m.getArgs().get(0).getType(), m -> m));
            Map<JavaType, JavaMethod> byReturnType = buildClass.getMethods().stream().collect(Collectors.toMap(m -> m.getReturnType(), m -> m));
            //处理parameter
            javaMethod.getArgs().forEach(javaArg -> {
                JavaType argType = javaArg.getType();
                if (byReturnType.containsKey(argType)) {
                    JavaMethod buildMethod = byReturnType.get(argType);
                    javaArg.setName("src");
                    javaArg.setType(buildMethod.getArgs().get(0).getType());
                    buildParameterMethods.add(byReturnType.get(argType));
                }
            });
            //处理returnType
            buildReturnMethod = byParameter.get(javaMethod.getReturnType());
        }

        public JavaMethod getJavaMethod() {
            return javaMethod;
        }

        public void setJavaMethod(JavaMethod javaMethod) {
            this.javaMethod = javaMethod;
        }

        public JavaMethod getBuildReturnMethod() {
            return buildReturnMethod;
        }

        public void setBuildReturnMethod(JavaMethod buildReturnMethod) {
            this.buildReturnMethod = buildReturnMethod;
        }

        public JavaClass getBuildClass() {
            return buildClass;
        }

        public void setBuildClass(JavaClass buildClass) {
            this.buildClass = buildClass;
        }

        public List<JavaMethod> getBuildParameterMethods() {
            return buildParameterMethods;
        }

        public void setBuildParameterMethods(List<JavaMethod> buildParameterMethods) {
            this.buildParameterMethods = buildParameterMethods;
        }
    }

    public static void main(String[] args) {
        JavaMethod javaMethod = new JavaMethod();
        JavaArg javaArg = new JavaArg();
        javaArg.setName("Sdfdfdf");
        javaArg.setType(JavaType.ofClass(String.class));
        javaMethod.setArgs(Lists.newArrayList(javaArg));
        System.out.println(ReflectionToStringBuilder.toString(javaMethod, ToStringStyle.JSON_STYLE));
        System.out.println(ReflectionToStringBuilder.toString(ReflectionUtils.copy(javaMethod,JavaMethod.class), ToStringStyle.JSON_STYLE));
    }
}
