package com.lvbby.codema.java.entity;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: AnnotationType.java, v 0.1 2017-11-15 下午4:16 dushang.lp Exp $
 */
public class AnnotationType {
    private List<JavaAnnotation> annotations;

    public List<JavaAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<JavaAnnotation> annotations) {
        this.annotations = annotations;
    }

    public void addAnnotation(JavaAnnotation annotation){
        if(annotation==null)
            return ;
        if(annotations==null){
            annotations= Lists.newArrayList();
        }
        annotations.add(annotation);
    }
    @Override public String toString() {
        if(CollectionUtils.isEmpty(annotations))
            return "";
        return annotations.stream().map(JavaAnnotation::toString).collect(Collectors.joining("\n"));
    }
}