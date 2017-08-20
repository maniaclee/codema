package com.lvbby.codema.app.mybatis;

import com.lvbby.codema.core.config.ConfigBind;
import com.lvbby.codema.java.baisc.JavaBasicCodemaConfig;
import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by lipeng on 2017/1/8.
 */
@ConfigBind(JavaMybatisCodemaMachine.class)
public class JavaMybatisCodemaConfig extends JavaBasicCodemaConfig {
    private String mapperDir;
    private String configPackage;
    //指定id的值，指定一个java bean里哪个属性作为id
    private Function<JavaClass, JavaField> idQuery = javaClass -> javaClass.getFields().stream().
            filter(javaField -> Objects.equals(javaField.getName(), "id"))
            .findAny().orElse(null);

    public Function<JavaClass, JavaField> getIdQuery() {
        return idQuery;
    }

    public void setIdQuery(Function<JavaClass, JavaField> idQuery) {
        this.idQuery = idQuery;
    }

    public String getConfigPackage() {
        return configPackage;
    }

    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }
}
