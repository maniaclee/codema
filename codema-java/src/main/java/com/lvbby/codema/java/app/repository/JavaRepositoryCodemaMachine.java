package com.lvbby.codema.java.app.repository;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.app.dto.$src__name_DTO;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
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
        List<JavaMethod> collect = javaClass.getMethods().stream().map(javaMethod -> wrap(javaMethod, buildUtil)).collect(Collectors.toList());
        config.handle(codemaContext, config, new JavaTemplateResult(config, $src__name_DTO.class, javaClass, ImmutableMap.of("methods", collect)));
    }

    public static JavaMethod findBuildFromMethod(JavaClass clz, JavaType javaType) {
        List<JavaMethod> collect = clz.getMethods().stream().filter(method -> method.getArgs().stream().anyMatch(javaArg -> Objects.equals(javaArg.getType(), javaType))).collect(Collectors.toList());
        Validate.isTrue(collect.size() <= 1, "multi build methods found.", JSON.toJSONString(javaType));
        return collect.isEmpty() ? null : collect.get(0);
    }

    /***
     * insert(Entity entity) ---> insert(Entity dto){Dto a = BuildUtils.buildEntity(dto);}
     * @param m
     * @param buildUtil
     * @return
     */
    private static JavaMethod wrap(JavaMethod m, JavaClass buildUtil) {
        JavaMethod javaMethod = ReflectionUtils.copy(m, JavaMethod.class);
        RepositoryMethod re = new RepositoryMethod(m, buildUtil);
        if (CollectionUtils.isNotEmpty(m.getArgs()))
            re.buildParameterMethod = findBuildFromMethod(buildUtil, m.getArgs().get(0).getType());//目前只考虑一个参数的情况
        re.buildReturnMethod = findBuildFromMethod(buildUtil, m.getReturnType());
        return javaMethod;
    }

    public static class RepositoryMethod {
        private JavaMethod javaMethod;
        private JavaMethod buildReturnMethod;
        private JavaMethod buildParameterMethod;
        private JavaClass buildClass;

        public RepositoryMethod(JavaMethod javaMethod, JavaClass buildClass) {
            this.javaMethod = javaMethod;
            this.buildClass = buildClass;
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

        public JavaMethod getBuildParameterMethod() {
            return buildParameterMethod;
        }

        public void setBuildParameterMethod(JavaMethod buildParameterMethod) {
            this.buildParameterMethod = buildParameterMethod;
        }

        public JavaClass getBuildClass() {
            return buildClass;
        }

        public void setBuildClass(JavaClass buildClass) {
            this.buildClass = buildClass;
        }
    }
}
