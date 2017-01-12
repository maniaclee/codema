package com.lvbby.codema.java.app.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

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
        config.handle(codemaContext, config, new JavaTemplateResult(config, $src__name_Repository.class, javaClass, ImmutableMap.of("methods", collect)));
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
            Map<JavaType, JavaMethod> buildTargets = buildClass.getMethods().stream().collect(Collectors.toMap(m -> m.getArgs().get(0).getType(), m -> m));
            Map<JavaType, JavaMethod> returnTargets = buildClass.getMethods().stream().collect(Collectors.toMap(m -> m.getReturnType(), m -> m));
            //处理parameter
            javaMethod.getArgs().forEach(javaArg -> {
                JavaType argType = javaArg.getType();
                if (returnTargets.containsKey(argType)) {
                    JavaMethod buildMethod = returnTargets.get(argType);
                    javaArg.setName("src");
                    javaArg.setType(buildMethod.getArgs().get(0).getType());
                    buildParameterMethods.add(returnTargets.get(argType));
                }
            });
            //处理returnType
            buildReturnMethod = buildTargets.get(javaMethod.getReturnType());
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
}
