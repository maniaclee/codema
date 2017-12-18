update ${table.nameInDb} set
<% for (f in table.fields ) {%>
<isNotEmpty  property="${f.nameCamel}">
  ${f.nameInDb} = #${f.nameCamel}#,
</isNotEmpty>
<%}%>