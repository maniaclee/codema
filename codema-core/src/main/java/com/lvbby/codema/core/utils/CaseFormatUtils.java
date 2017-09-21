package com.lvbby.codema.core.utils;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

/**
 *
 * @author dushang.lp
 * @version $Id: CaseFormatUtils.java, v 0.1 2017-09-21 ÉÏÎç11:57 dushang.lp Exp $
 */
public class CaseFormatUtils {
    public static String toCaseFormat(CaseFormat caseFormat, String s) {
        //underscore or hyphen may has issue with format , eg: This_is_not_regular_hyphen
        if (s.contains("-") || s.contains("_")) {
            s = s.toLowerCase();
        }
        return detectCaseFormat(s).to(caseFormat, s);
    }

    public static CaseFormat detectCaseFormat(String s) {
        boolean capital = isCapital(s);
        if (s.contains("_")) {
            return capital ? CaseFormat.UPPER_UNDERSCORE : CaseFormat.LOWER_UNDERSCORE;
        }
        if (s.contains("-")) {
            return CaseFormat.LOWER_HYPHEN;
        }
        return capital ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL;
    }

    public static boolean isCapital(String s) {
        if (StringUtils.isBlank(s))
            return false;
        return StringUtils.isAllUpperCase(s.substring(0, 1));
    }

    public static void main(String[] args) {
        Consumer<CaseFormat> consumer = caseFormat -> {
            System.out.println(toCaseFormat(caseFormat, "ThisClass"));
            System.out.println(toCaseFormat(caseFormat, "thisClass"));
            System.out.println(toCaseFormat(caseFormat, "this_Class"));
            System.out.println(toCaseFormat(caseFormat, "This_Class"));
            System.out.println(toCaseFormat(caseFormat, "this-Class"));
            System.out.println(toCaseFormat(caseFormat, "This-Class"));
            System.out.println("-==============");
        };
        consumer.accept(CaseFormat.LOWER_CAMEL);
        consumer.accept(CaseFormat.UPPER_CAMEL);
        consumer.accept(CaseFormat.UPPER_UNDERSCORE);
        consumer.accept(CaseFormat.LOWER_UNDERSCORE);
        consumer.accept(CaseFormat.LOWER_HYPHEN);
    }
}