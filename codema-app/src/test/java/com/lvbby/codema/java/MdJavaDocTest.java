package com.lvbby.codema.java;

import com.google.common.collect.Lists;
import com.lvbby.codema.app.javaMdDoc.JavaMdDocMachine;
import com.lvbby.codema.core.handler.ResultHandlerFactory;
import com.lvbby.codema.java.api.JavaSourceMachineFactory;
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
            Lists.newArrayList(new File(System.getProperty("user.home"), "workspace/finfiprod")));
    }

    public void mdJavaDoc(String reference) throws Exception {
        JavaSourceMachineFactory.fromClassFullName()
                .source(reference)
                .next(new JavaMdDocMachine())
                .addResultHandler(ResultHandlerFactory.print)
                .addResultHandler(ResultHandlerFactory.clipBoard)
                .run();
//        String service;
//        String method;
//        if(reference.matches(".*(\\.[a-z][^\\.]+)$")){
//            int i = reference.lastIndexOf(".");
//            service=reference.substring(0,i);
//            method=reference.substring(i+1);
//        }else {
//            String[] split = reference.split("[#]");
//            service = split[0];
//            method = split.length > 1 ? split[1] : null;
//        }
//
//        JavaMdDocMachine md = new JavaMdDocMachine();
//        md.setTemplate(md.getTemplate()+"====================");
//        md.resultHandlers(Lists.newArrayList(new PrintResultHandler(),new ClipBoardResultHandler()));
//        md.setMethod(method);
//        JavaClassMachineFactory.fromClassFullName().source(service).next(md).run();
    }

    @Test public void testMdJavaDoc() throws Exception {
//        mdJavaDoc("com.alipay.finbatchcore.biz.shared.action.ProcessAction");
        mdJavaDoc("com.alipay.finfiprod.common.service.facade.rfm.user.service.RfmInvestorCertifyFacade#userCheck");
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
//        System.out.println(new JavaMdDocInterfaceMachine().getTemplate());
    }
}