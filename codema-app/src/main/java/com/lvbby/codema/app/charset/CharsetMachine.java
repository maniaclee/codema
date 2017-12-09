package com.lvbby.codema.app.charset;

import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.result.BasicResult;
import org.apache.commons.io.IOUtils;
import org.mozilla.intl.chardet.nsDetector;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 按正确编码格式读取文件
 * @author dushang.lp
 * @version $Id: CharsetMachine.java, v 0.1 2017年12月09日 下午1:49 dushang.lp Exp $
 */
public class CharsetMachine extends AbstractBaseMachine<InputStream, String> {
    @Override protected void doCode() throws Exception {
        byte[] bytes = IOUtils.toByteArray(source);
        String charset = charset(new ByteArrayInputStream(bytes));
        handle(BasicResult.instance(IOUtils.toString(new ByteArrayInputStream(bytes), charset)));
    }

    public static String charset(InputStream inputStream) throws Exception {
        nsDetector det = new nsDetector();

        // Set an observer...
        // The Notify() will be called when a matching charset is found.

        final String[] charsertResult = { null };
        det.Init(charset -> charsertResult[0] = charset);

        BufferedInputStream imp = new BufferedInputStream(inputStream);

        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            det.DoIt(buf, len, false);
        }
        det.DataEnd();
        return charsertResult[0];
    }
}