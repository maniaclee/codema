package com.lvbby.codema.java.app.repository;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
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
import java.util.Objects;
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

    public static JavaMethod findBuildFromMethod(JavaClass clz, JavaType javaType) {
        List<JavaMethod> collect = clz.getMethods().stream().filter(method -> method.getArgs().stream().anyMatch(javaArg -> Objects.equals(javaArg.getType(), javaType))).collect(Collectors.toList());
        Validate.isTrue(collect.size() <= 1, "multi build methods found.", JSON.toJSONString(javaType));
        return collect.isEmpty() ? null : collect.get(0);
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
            this.javaMethod = javaMethod;
            this.buildClass = buildClass;
            //收集build类里的信息
            Map<JavaType, JavaMethod> buildTargets = buildClass.getMethods().stream().collect(Collectors.toMap(m -> m.getArgs().get(0).getType(), m -> m));
            //处理parameter
            javaMethod.getArgs().forEach(javaArg -> {
                if (buildTargets.containsKey(javaArg.getType())) {
                    JavaMethod buildMethod = buildTargets.get(javaArg.getType());
                    javaArg.setType(buildMethod.getReturnType());
                    buildParameterMethods.add(buildMethod);
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
    }
}
