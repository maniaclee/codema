package com.lvbby.codema.app.es;

import com.lvbby.codema.core.result.BasicResult;
import com.lvbby.codema.core.result.Result;
import com.lvbby.codema.core.utils.JsonBuilder;
import com.lvbby.codema.core.utils.ReflectionUtils;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.machine.AbstractJavaCodemaMachine;
import com.lvbby.codema.java.machine.AbstractJavaInputCodemaMachine;
import org.apache.commons.lang3.StringUtils;

import static com.lvbby.codema.core.utils.JsonBuilder.node;

/**
 * Created by lipeng on 16/12/23.
 */
public class JavaEsCodemaMachine extends AbstractJavaInputCodemaMachine<JavaEsCodemaConfig,String> {

    public Result<String> codeEach(JavaEsCodemaConfig config, JavaClass javaClass) throws Exception {
        return new BasicResult().result(genEsMapping(javaClass).toJson());
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
            node.child(javaField.getName(), node().child("type", javaField.getType().getSpecificType()));
        });
        return node;
    }

    private static Object convertType(String type) {
        type = StringUtils.trimToEmpty(type);
        type = ReflectionUtils.replace(type, "[^<>]+<([^<>]+)>", matcher -> matcher.group(1));
        return type.toLowerCase();
    }

}
