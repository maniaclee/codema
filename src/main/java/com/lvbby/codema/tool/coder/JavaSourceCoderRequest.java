package com.lvbby.codema.tool.coder;

import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * Created by lipeng on 2016/12/20.
 */
public class JavaSourceCoderRequest extends JavaCoderRequest {
    private TypeDeclaration<?> typeDeclaration;
    private String source;

    public TypeDeclaration<?> getTypeDeclaration() {
        return typeDeclaration;
    }

    public void setTypeDeclaration(TypeDeclaration<?> typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
