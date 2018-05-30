package testatas;

import com.lvbby.codema.core.utils.ReflectionUtils;
import org.junit.Test;

/**
 *
 * @author dushang.lp
 * @version $Id: SimpleTest.java, v 0.1 2017-11-29 上午12:38 dushang.lp Exp $
 */
public class SimpleTest {

    @Test
    public void sdfsdf() throws Exception {
        System.out.println("sdfsd");
    }

    @Test
    public void name() throws Exception {
        String s = "CapitalPlanRequest[clientId=9fbe53350f985315f5e14f8d84d7e5ab2b3fb283,sceneType=RHT,requestParams={INST_ID=CEB, YEB_EXT_INFO={134=1, USER_IP=10.15.30.125, clientSessionId=RZ00y9a6vgDsKRHm3u15HlGAlCD1fh16mobilegwRZ00, incomeCardNo=6226740904584541, loansType=CAR, freezeCodeForLoansManager=22007, incomeCardUserName=嵌嵌, apdidToken=null}, YEB_SOURCE_FROM=01, FIXED_TYPE=MONTH, YEB_FIXED_TYPE=fixed_month, USER_ID=2088202904579163, LOANS_TYPE=CAR, APPLY_TIME=Tue May 29 22:49:50 CST 2018, PAY_CHANNEL=MONEYFUND, SCENE_PLAN_TYPE=RHT_OUT, PRINCIPAL={extralInfo={}, userId=2088202904579163, userMobile=15248563043, userName=嵌嵌, businessProperties=[{eventPropertyKey=umidToken, eventPropertyValue=iyACldnJdY75HMRRXlosaZGuqOIzuJTf}, {eventPropertyKey=apdid, eventPropertyValue=eYOIkpyAZ8DyJg6NEqGo0mbVveNnKAkQ0pNoVHAaedaM0kHSc2yUnZYq}]}, FIXED_DAY=12, PAY_CHANNEL_TYPE=DEBIT_EXPRESS, MEMO=sjs, FREEZE_CODE=22007, APPLY_AMOUNT={cent=3600, currency={currencyCode=CNY}}, YEB_ORDER_TYPE=fixed_redeem, SOURCE_FROM=ALIPAYAPP, BANK_CARD_SIGNID=1802090030731662, ENVIRONMENT={sessionId=RZ00y9a6vgDsKRHm3u15HlGAlCD1fh16mobilegwRZ00, umidToken=iyACldnJdY75HMRRXlosaZGuqOIzuJTf, apdid=eYOIkpyAZ8DyJg6NEqGo0mbVveNnKAkQ0pNoVHAaedaM0kHSc2yUnZYq, serverId=wealthmanagertwa-test-61.alipay.net, clientIp=10.15.30.125}},responseParams=<null>]";
        System.out.println(ReflectionUtils.apacheToStringBuilder2json(s));
    }


}