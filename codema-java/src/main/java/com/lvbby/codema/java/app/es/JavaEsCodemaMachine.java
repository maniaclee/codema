package com.lvbby.codema.java.app.es;

import com.lvbby.codema.core.CodemaContext;
import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.core.inject.CodemaInjectable;
import com.lvbby.codema.core.inject.CodemaRunner;
import com.lvbby.codema.core.inject.NotNull;
import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.utils.JavaUtils;
import com.lvbby.codema.core.utils.JsonBuilder;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.inject.JavaTemplate;
import com.lvbby.codema.java.inject.JavaTemplateInjector;
import com.lvbby.codema.java.inject.JavaTemplateParameter;
import org.apache.commons.lang.StringUtils;

import static com.lvbby.codema.core.utils.JsonBuilder.node;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaEsCodemaMachine implements CodemaInjectable {

    @ConfigBind(JavaEsCodemaConfig.class)
    @CodemaRunner
    @JavaTemplate
    public void code(CodemaContext codemaContext, @NotNull JavaEsCodemaConfig config, @NotNull @JavaTemplateParameter(identifier = JavaTemplateInjector.java_source) JavaClass javaClass) throws Exception {
        config.handle(codemaContext, config, new BasicResult().result(genEsMapping(javaClass)));
    }

    public static JsonBuilder genEsMapping(JavaClass javaClass) {
        return node()
                .child("mappings", node()
                        .child(javaClass.getNameCamel(),
                                node().child("properties", _mapping(javaClass))))
                ;
    }

    private static JsonBuilder _mapping(JavaClass javaClass) {
        JsonBuilder node = node();
        javaClass.getFields().stream().forEach(javaField -> {
            node.child(javaField.getName(), node().child("type", convertType(javaField.getType())));
        });
        return node;
    }

    private static Object convertType(String type) {
        type = StringUtils.trimToEmpty(type);
        type = JavaUtils.replace(type, "[^<>]+<([^<>]+)>", matcher -> matcher.group(1));
        return type.toLowerCase();
    }

}
