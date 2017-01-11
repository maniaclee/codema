package com.lvbby.codema.java.app.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableMap;
import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.engine.ScriptEngineFactory;
import com.lvbby.codema.core.error.CodemaRuntimeException;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.java.app.baisc.JavaSourceParam;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.result.JavaTemplateResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaConvertCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaConvertCodemaConfig.class)
    @CodemaRunner
    public void code(CodemaContext codemaContext, @NotNull JavaConvertCodemaConfig config, @NotNull JavaSourceParam javaSourceParam) throws Exception {
        Validate.notBlank(config.getConvertToClassName(), "convert-to-class-name can't be blank");

        List<JavaClass> javaClasses = codemaContext.getCodema().getCodemaBeanFactory().getBeans(codemaBean -> codemaBean.getId().startsWith(config.getFromPackage()), JavaClass.class);
        javaClasses.addAll(javaSourceParam.getJavaClass(config.getFromPackage()));
        if (CollectionUtils.isEmpty(javaClasses))
            return;

        Map<JavaClass, String> map = javaClasses.stream().collect(Collectors.toMap(o -> o, o -> getTargetClassName(config, o)));
        ImmutableMap<String, Object> args = ImmutableMap.of(
                "Convert", config.getDestClassName(),
                "cs", javaClasses,
                "map", map
        );
        System.err.println(JSON.toJSONString(args, SerializerFeature.PrettyFormat));
        config.handle(codemaContext, config, new JavaTemplateResult(config, $Convert_.class, null, args));
    }

    private String getTargetClassName(JavaConvertCodemaConfig config, JavaClass javaClass) {
        try {
            return ScriptEngineFactory.instance.eval(config.getConvertToClassName(), javaClass.getName());
        } catch (Exception e) {
            throw new CodemaRuntimeException("failed to eval expr", e);
        }
    }


}
