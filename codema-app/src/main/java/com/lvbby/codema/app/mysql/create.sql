CREATE TABLE ${table.nameInDb} (
  # <% for (f in table.fields ) {
  #   var nullable = "";
  #   if (f.nullable){nullable = "NOT NULL";}
  #   var auto_inc = "";
  #   if(f.primaryKey && (@f.getDbType().equals("INT") ||@f.getDbType().equals("BIGINT"))){
  #     auto_inc = "AUTO_INCREMENT";
  #   }
  # %>
  ${f.nameInDb} ${f.dbType}  ${nullable} ${auto_inc} COMMENT '${f.comment}',
  #<%} %>
  PRIMARY KEY (${table.primaryKey.nameInDb})
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;