update ${table.nameInDb} set
<% for (f in table.fields ) {%>
<isNotNull  property="${f.nameCamel}" prepend=",">
  ${f.nameInDb} = #${f.nameCamel}#
</isNotNull>
<%}%>