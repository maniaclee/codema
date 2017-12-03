package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.javaMdDoc.JavaMdDocInterfaceCodemaMachine;
import com.lvbby.codema.core.Codema;
import com.lvbby.codema.core.handler.ClipBoardResultHandler;
import com.lvbby.codema.core.handler.PrintResultHandler;
import com.lvbby.codema.java.machine.JavaClassMachineFactory;
import com.lvbby.codema.java.source.JavaClassSourceParser;
import com.lvbby.codema.java.tool.JavaSrcLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 *
 * @author dushang.lp
 * @version $Id: MdJavaDocTest.java, v 0.1 2017-09-03 下午5:48 dushang.lp Exp $
 */
public class MdJavaDocTest extends BaseTest {
    @Before
    public void init() {
        JavaSrcLoader.initJavaSrcRoots(
            Lists.newArrayList(new File(System.getProperty("user.home"), "workspace")),6);
    }

    public void mdJavaDoc(String reference) throws Exception {
        String service;
        String method;
        if(reference.matches(".*(\\.[a-z][^\\.]+)$")){
            int i = reference.lastIndexOf(".");
            service=reference.substring(0,i);
            method=reference.substring(i+1);
        }else {
            String[] split = reference.split("[#]");
            service = split[0];
            method = split.length > 1 ? split[1] : null;
        }

        JavaMdDocInterfaceCodemaMachine md = new JavaMdDocInterfaceCodemaMachine();
        md.resultHandlers(Lists.newArrayList(new PrintResultHandler(),new ClipBoardResultHandler()));
        md.setMethod(method);
        JavaClassMachineFactory.fromClassFullName().source(service).next(md).code();
    }

    @Test public void testMdJavaDoc() throws Exception {
        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentQueryFacade#queryAppointmentPeriods");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.UserPurchaseFacade#queryPurchaseProductCount");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.UserPurchaseFacade#queryPurchaseProductList");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AssetQueryFacade#queryAssetStatistic");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentQueryFacade#queryUserAppointmentStatistic");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AssetQueryFacade#queryAssetList");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentQueryFacade#queryUserAppointmentList");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentQueryFacade#queryAppointmentDetail");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AssetQueryFacade#queryAssetDetail");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentFacade#applyAppointment");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.AppointmentFacade#cancelAppointment");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.UserPurchaseFacade#queryPurchaseProduct");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.UserPurchaseFacade#userPurchase");
//        mdJavaDoc("com.alipay.finfiprod.common.service.facade.service.UserPurchaseFacade#queryOrder");
//        mdJavaDoc("com.alipay.zcbprod.common.service.facade.asset.service.AssetQueryFacade#queryAssetProfitList");
//        mdJavaDoc("com.alipay.zcbprod.common.service.facade.appointment.service.AppointmentQueryFacade#queryAppointmentHistoryList");
//        mdJavaDoc("com.alipay.zcbprod.common.service.facade.common.BaseMultiSourcePageRequest");
    }
}