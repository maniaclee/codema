package com.lvbby.codema.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/9/13.
 */
public class XmlMerger {

    public String merge(String src, String dest) throws DocumentException {
        Document docSrc = DocumentHelper.parseText(src);
        Document docDest = DocumentHelper.parseText(dest);
        return format(merge(docSrc, docDest));
    }

    public Document merge(Document docSrc, Document docDest) {
        Element srcElement = docSrc.getRootElement();
        Element dest = docDest.getRootElement();
        if (isEqual(dest, srcElement)) {
            for (Object o : srcElement.elements()) {
                merge((Element) o, dest);
            }
        } else {
            docDest.add((Element) srcElement.clone());
        }
        return docDest;
    }

    private void merge(Element src, Element parent) {
        Element element = find(src, parent);
        //加入
        if (element == null || isEmpty(src)) {
            parent.add((Element) src.clone());
        } else {
            List elements = src.elements();
            if (CollectionUtils.isNotEmpty(elements)) {
                for (Object o : elements) {
                    merge((Element) o, element);
                }
            }
        }
    }

    private boolean isEmpty(Element element) {
        return CollectionUtils.isEmpty(element.attributes());
    }

    private Element find(Element src, Element parent) {
        for (Object o : parent.elements()) {
            Element e = (Element) o;
            if (isEqual(e, src)) {
                return e;
            }
        }
        return null;
    }

    public boolean isEqual(Element element, Element dest) {
        String id = element.attributeValue("id");
        String idDest = dest.attributeValue("id");
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(idDest)
            && StringUtils.equals(id, idDest)) {
            return true;
        }
        return StringUtils.equals(getElementId(element), getElementId(dest));
    }

    public String getElementId(Element element) {
        return (String) element.attributes().stream()
            .map(o -> String.format("%s:%s", ((Attribute) o).getName(), ((Attribute) o).getValue()))
            .collect(Collectors.joining(";"));
    }

    public String format(Document document) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            //            format.setEncoding("gb2312");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String merge = new XmlMerger().merge(
            IOUtils.toString(XmlMerger.class.getClassLoader().getResourceAsStream("a.xml")),
            IOUtils.toString(XmlMerger.class.getClassLoader().getResourceAsStream("b.xml")));
        System.out.println(merge);
    }
}
