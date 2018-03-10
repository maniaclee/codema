package com.lvbby.codema.app.repository;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.AppMachine;
import com.lvbby.codema.app.AppTemplateResource;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.baisc.JavaClassNameParser;
import com.lvbby.codema.java.baisc.JavaClassNameParserFactory;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;
import com.lvbby.codema.java.result.JavaTemplateResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 代理dao interface生成repository， 进行source到convert的转化
 * Created by lipeng on 16/12/23.
 */
@AppTemplateResource($Repository_.class)
public class JavaRepositoryMachine extends AppMachine {
    @Getter
    @Setter
    private JavaClassNameParser convertUtilsClass = JavaClassNameParserFactory.className("BuildUtils");

    public JavaTemplateResult codeEach(JavaClass javaClass) throws Exception {
        JavaClass buildUtil = findJavaBean(convertUtilsClass.getClassName(javaClass));
        Validate.notNull(buildUtil, "buildClass not found");

        List<RepositoryMethod> collect = javaClass.getMethods().stream().map(javaMethod -> new RepositoryMethod(javaMethod, buildUtil)).collect(Collectors.toList());
        return super.codeEach(javaClass)
                .bind("methods", collect)
                .bind("buildUtilClass", buildUtil);
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
