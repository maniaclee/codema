package com.lvbby.codema.core.utils;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by lipeng on 2017/9/13.
 */
public class XmlMerger {


    public void merge(String src, String dest) throws DocumentException {
        Document docSrc = DocumentHelper.parseText(src);
        Document docDest = DocumentHelper.parseText(dest);
        merge(docSrc, docDest);
    }

    private void merge(Document docSrc, Document docDest) {
        Element rootElement = docSrc.getRootElement();
        for (Object o : rootElement.elements()) {
            Element element = (Element) o;
            System.out.println(element);
        }
        Element dest = docDest.getRootElement();

    }

    private void merge(Element src, Element parent) {

    }

    public static void main(String[] args) throws Exception {
        new XmlMerger().merge(IOUtils.toString(XmlMerger.class.getClassLoader().getResourceAsStream("a.xml")), IOUtils.toString(XmlMerger.class.getClassLoader().getResourceAsStream("b.xml")));
    }
}
