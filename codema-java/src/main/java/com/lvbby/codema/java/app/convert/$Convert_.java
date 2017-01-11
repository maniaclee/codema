package com.lvbby.codema.java.app.convert;

import com.lvbby.codema.java.template.$Class1_;
import com.lvbby.codema.java.template.$Class_;

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
        $Class1_ re = new $Class1_();
        // <%
        // for( f in  c.fields){
        // var name =f.name;
        // var nameUpper = lee.capital(name);
        // %>
        //        re.set$nameUpper_(from.get$nameUpper_());
        // <% }%>
        return re;
    }
    // <% }%>

}
