package com.lvbby.codema.tool.coder.source;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.lvbby.codema.tool.coder.JavaCoderRequest;

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

    public ClassOrInterfaceDeclaration findClass() {
        return typeDeclaration.getNodesByType(ClassOrInterfaceDeclaration.class).stream().findFirst().orElse(null);
    }
}
