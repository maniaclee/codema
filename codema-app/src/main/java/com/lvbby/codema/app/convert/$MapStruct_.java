package com.lvbby.codema.app.convert;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.annotaion.Sentence;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Sentence("var Class = srcClassName; var class=lee.unCapital(srcClassName);")
@Sentence("var Class1 =  @config.getConvertToClass().getClassName(source);var class1=lee.unCapital(Class1);")
@Sentence("var MapStruct = destClassName;")
@Mapper
public interface $MapStruct_ {
//    $MapStruct_ INSTANCE = Mappers.getMapper($MapStruct_.class);
//    static $MapStruct_Convertor instance() {
//        return INSTANCE;
//    }

    @Mappings({})
    $Class_ to$Class_($Class1_ $class1_);
    @Mappings({})
    $Class1_ to$Class1_($Class_ $class_);

    List<$Class1_> to$Class1_s(List<$Class_> $class_s);

    List<$Class_> to$Class_s(List<$Class1_> $class1_s);

}
