package com.lvbby.codema.java.app.convert;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipeng on 2016/12/24.
 */
public class $Convert_ {

    // <%
    // for( c in  cs){
    // var Class =c.name;
    // var Class1=@map.get(c);
    // %>
    public static $Class1_ build$Class1_($Class_ from) {
        if (from == null)
            return null;
        $Class1_ re = new $Class1_();
        /**#
         <%
         for( f in  c.fields){
         var name =f.name;
         var nameUpper = lee.capital(name);
         %>
         re.set$nameUpper_(from.get$nameUpper_());
         <% }%>**/
        return re;
    }

    public static List<$Class1_> build$Class1_s(List<$Class_> from) {
        List<$Class1_> re = new ArrayList<>();
        if (from == null)
            return re;
        for ($Class_ e : from) {
            re.add(build$Class1_(e));
        }
        return re;
    }

    public static $Class_ build$Class_($Class1_ from) {
        if (from == null)
            return null;
        $Class_ re = new $Class_();
        /**#
         <%
         for( f in  c.fields){
         var name =f.name;
         var nameUpper = lee.capital(name);
         %>
         re.set$nameUpper_(from.get$nameUpper_());
         <% }%>**/
        return re;
    }

    public static List<$Class_> build$Class_s(List<$Class1_> from) {
        List<$Class_> re = new ArrayList<>();
        if (from == null)
            return re;
        for ($Class1_ e : from) {
            re.add(build$Class_(e));
        }
        return re;
    }
    // <% }%>

}
