package com.lvbby.codema.app.convert;

import com.google.common.collect.Lists;
import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;
import com.lvbby.codema.java.template.annotaion.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
@Sentence("var Convert = config.destClassName;")
public class $Convert_ {

    // <%
    // var Class = from.name;
    // var Class1= @config.getConvertToClassNameParser().getClassName(source,from);
    // %>
    public static $Class1_ build$Class1_($Class_ from) {
        if (from == null) {
            return null;
        }
        $Class1_ re = new $Class1_();
        /**#
         <%
         for( f in  from.fields){
         var name =f.name;
         var nameUpper = lee.capital(name);
         %>
         re.set$nameUpper_(from.get$nameUpper_());
         <% }%>**/
        return re;
    }

    public static List<$Class1_> build$Class1_s(List<$Class_> from) {
        if (from == null) {
            return null;
        }
        List<$Class1_> re = Lists.newArrayList();
        for ($Class_ e : from) {
            re.add(build$Class1_(e));
        }
        return re;
    }

    public static $Class_ build$Class_($Class1_ from) {
        if (from == null) {
            return null;
        }
        $Class_ re = new $Class_();
        /**#
         <%
         for( f in  from.fields){
         var name =f.name;
         var nameUpper = lee.capital(name);
         %>
         re.set$nameUpper_(from.get$nameUpper_());
         <% }%>**/
        return re;
    }

    public static List<$Class_> build$Class_s(List<$Class1_> from) {
        if (from == null) {
            return null;
        }
        List<$Class_> re = new ArrayList<$Class_>();
        for ($Class1_ e : from) {
            re.add(build$Class_(e));
        }
        return re;
    }

}
