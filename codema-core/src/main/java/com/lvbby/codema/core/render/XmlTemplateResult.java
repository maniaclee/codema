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
            //创建字符串缓冲区
            StringWriter stringWriter = new StringWriter();
            //设置文件编码
            OutputFormat xmlFormat = new OutputFormat();
            xmlFormat.setEncoding("UTF-8");
            // 设置换行
            xmlFormat.setNewlines(true);
            // 生成缩进
            xmlFormat.setIndent(true);
            // 使用4个空格进行缩进, 可以兼容文本编辑器
            xmlFormat.setIndent("    ");

            //创建写文件方法
            XMLWriter xmlWriter = new XMLWriter(stringWriter, xmlFormat);
            //写入文件
            xmlWriter.write(document);
            //关闭
            xmlWriter.close();
            // 输出xml
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
