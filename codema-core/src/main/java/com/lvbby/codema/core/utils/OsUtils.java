package com.lvbby.codema.core.utils;

import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.InputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: OsUtils.java, v 0.1 2017年12月15日 下午7:34 dushang.lp Exp $
 */
public class OsUtils {

    public static String getClipboardString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static DataFlavor  findDataFlavor(Transferable trans,String mimeType , Class  destClass){
        for (DataFlavor dataFlavor : trans.getTransferDataFlavors()) {
            System.out.println(dataFlavor.getHumanPresentableName() + "___" + dataFlavor.getRepresentationClass().getName());
            if(dataFlavor.getHumanPresentableName().equals(mimeType) && dataFlavor.getRepresentationClass().equals(destClass))
                return dataFlavor;
        }
        return null;
    }
    public static String getClipboardHtml() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            DataFlavor fragmentHtmlFlavor = findDataFlavor(trans,"text/rtf", InputStream.class);
            System.out.println("final ========== " + fragmentHtmlFlavor);
            if (trans.isDataFlavorSupported(fragmentHtmlFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) IOUtils.toString((InputStream) trans.getTransferData(fragmentHtmlFlavor));
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(getClipboardHtml());
    }
}