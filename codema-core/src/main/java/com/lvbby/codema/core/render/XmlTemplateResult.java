package com.lvbby.codema.core.render;

import com.lvbby.codema.core.ResultContext;
import com.lvbby.codema.core.result.MergeCapableFileResult;
import com.lvbby.codema.core.utils.XmlMerger;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/14.
 */
public class XmlTemplateResult extends TemplateEngineResult implements MergeCapableFileResult {

    public   XmlTemplateResult(Document document) {
        super(format(document));
    }

    public XmlTemplateResult(String template) {
        super(template);
    }

    public static String format(Document document) {
        try {
            //�����ַ���������
            StringWriter stringWriter = new StringWriter();
            //�����ļ�����
            OutputFormat xmlFormat = new OutputFormat();
            xmlFormat.setEncoding("UTF-8");
            // ���û���
            xmlFormat.setNewlines(true);
            // ��������
            xmlFormat.setIndent(true);
            // ʹ��4���ո��������, ���Լ����ı��༭��
            xmlFormat.setIndent("    ");

            //����д�ļ�����
            XMLWriter xmlWriter = new XMLWriter(stringWriter, xmlFormat);
            //д���ļ�
            xmlWriter.write(document);
            //�ر�
            xmlWriter.close();
            // ���xml
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void beforeRender(Map bindingParameters) {
        template(getTemplate().replaceAll("<!--\\s*", "").replaceAll("\\s*-->", ""));
    }

    @Override
    public String parseMergeResult(InputStream dest, ResultContext resultContext) throws Exception {
        return new XmlMerger().merge(getString(), IOUtils.toString(dest));
    }
}
