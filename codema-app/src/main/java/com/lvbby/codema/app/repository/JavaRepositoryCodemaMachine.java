package com.lvbby.codema.app.repository;

import com.google.common.collect.Lists;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaRepositoryCodemaMachine extends AbstractJavaCodemaMachine<JavaRepositoryCodemaConfig> {

    public void codeEach(CodemaContext codemaContext, JavaRepositoryCodemaConfig config, JavaClass javaClass) throws Exception {
        JavaClass buildUtil = codemaContext.getCodema().getCodemaBeanFactory().getBean(config.getConvertUtilsClass());
        Validate.notNull(buildUtil, "buildClass not found");

        List<RepositoryMethod> collect = javaClass.getMethods().stream().map(javaMethod -> new RepositoryMethod(javaMethod, buildUtil)).collect(Collectors.toList());
        JavaTemplateResult result = new JavaTemplateResult(config, $Repository_.class, javaClass)
                .bind("methods", collect)
                .bind("buildUtilClass", buildUtil)
                .bind("Repository", config.evalDestClassName(javaClass, javaClass.getName() + "Repository"))
                .addImport(buildUtil);
        //        collect.stream().filter(r -> r.getBuildReturnMethod() != null).forEach(r -> result.addImport(r.getBuildReturnMethod().getReturnType(), codemaContext));
        config.handle(codemaContext, result);
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
            this.javaMethod = ReflectionUtils.copy(javaMethod, JavaMethod.class);
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

}
