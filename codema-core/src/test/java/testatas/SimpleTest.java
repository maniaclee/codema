package testatas;

import com.alibaba.fastjson.JSON;
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
        //        String s = "CapitalPlanPageRequest[clientId=c3cf102c8947c6f2755367c47bdb1f0b7b488976,sceneType=WFIN,requestParams={YEB_SOURCE_FROM=01, USER_ID=2088302055007631, OPERATE_SCENE=USER_CAPITAL_PLAN_LIST_QUERY, YEB_ORDER_TYPE=fixed_purchase, STATUS_LIST=['S'], SCENE_PLAN_TYPE=WFIN, YEB_IS_QUERY_AMOUNT=true, PRINCIPAL={extralInfo={}, userId=2088302055007631, userMobile=15873066867, userName=flpuhk, businessProperties=[{eventPropertyKey=umidToken, eventPropertyValue=dQxLkgNLOk6/NDVj03J0LuevSiZscV1a}, {eventPropertyKey=apdid, eventPropertyValue=eYOIkqAVWdULgZ3t+eKWJldsTR2gMhdk59it7T5uk9MbyAdg79BiiFY/}]}, ENVIRONMENT={sessionId=GZ00qoPy4E4HXvESrn2dsZNqmWyOcs63mobilegwGZ00, umidToken=dQxLkgNLOk6/NDVj03J0LuevSiZscV1a, apdid=eYOIkqAVWdULgZ3t+eKWJldsTR2gMhdk59it7T5uk9MbyAdg79BiiFY/, serverId=albertdeMacBook-Pro.local, clientIp=30.31.182.15}},itemsPerPage=30,currentPage=1,userId=2088302055007631,tableFlag=<null>,orderId=<null>,channel=<null>,bizScene=<null>,bizCode=<null>,filterAtter=<null>]";
        String source = "UniformEvent[localTxMode=false,localDatasourceId=,localEventDataSourceId=,id=897a9f242e2a2495cac8430a8b780d0d,topic=TP_M_FINANCE,eventCode=EC_finbatchcore_contract_task,eventType=<null>,eventName=<null>,transactional=false,eventTxSynListener=<null>,serverId=chonghuan-MacBook.local,gmtOccur=Wed Jan 24 14:57:10 CST 2018,clientIp=<null>,clientMac=<null>,clientId=<null>,clientPCIDMac=<null>,clientPCIDHwid=<null>,clientPCIDGuid=<null>,umidHardVersion=<null>,umidSysInfo=<null>,umidHardInfo=<null>,umidSoftInfo=<null>,umidRID=<null>,umidSoftVersion=<null>,sessionId=<null>,"
                        + "eventPayload=ContractEvent[contractType=P2P_SERVICE_CONTRACT,bizSource=appointment,arrangementNo=20180115W2020315000005031889,outBizNo=2018012400903001000000000000880000001724,productId=<null>,userId=2088302249335889,loanUserId=<null>,contractTemplateId=20180104000690000001000000000013,shouldContractSign=true,contractSignData=ContractSignData[outBizId=<null>,userId=2088302249335889,certNo=621200197710308149,userName=重幻二,oppositePersonal=false,oppositeInstId=Z98,oppositeUserId=<null>,oppositeCertNo=<null>,oppositeUserName=<null>,contractId=<null>,signBizId=<null>,extInfo=<null>],contractParams=<null>]"
                        + ",principal=<null>,txId=<null>,priority=5,sendOnceMessage=false,crossIDC=false,hostName=chonghuan-MacBook.local,throwExceptionOnFailed=true]";
        String s = "CreateStarWishReq[starWishSubjectVO=[subjectId=14,subjectContent=#是时候犒赏一下自己了#,createTime=20180802154633259,modifyTime=20180802220302762,isStick=false,isHot=false,recommendStatus=UP,activityCode=<null>,filterAtter=<null>],starWishContent=,startWishTargetAmount=300.00,starWishOpenStatus=ANONYMOUS,activityCode=<null>,userId=2088202941206880,sceneType=STAR_WISH,channelType=ALIPAYAPP,bizCode=STAR_WISH_SERVICE], result=SimpleResult[result=StarWishVO[userId=2088202941206880,userPortraitUrl=<null>,userShowName=hmmfux,starWishId=20180803001100140700880600000208,starWishSubjectVO=StarWishSubjectVO[subjectId=14,subjectContent=#是时候犒赏一下自己了#,createTime=20180802154633259,modifyTime=20180802220302762,isStick=false,isHot=false,recommendStatus=UP,activityCode=<null>,filterAtter=<null>],starWishContent=,startWishTargetAmount=300.00,starWishOpenStatus=ANONYMOUS,starVO=StarVO[starId=<null>,starPosition=<null>,starLevel=<null>,starDesc=<null>,starNickName=<null>,starPicUrl=<null>,starBgUrl=<null>,starType=<null>,starRightAscension=<null>,starDeclination=<null>,starConstellation=<null>,starConstellationEnglishName=<null>,starSize=<null>,filterAtter=<null>],starWishStatus=CREATED,blessCount=0,createDate=Fri Aug 03 15:41:26 CST 2018,endDate=<null>,completeDate=<null>,selectedProductId=<null>,selectedProductType=<null>,assetIsolationAccount=<null>,starWishCompleteAmount=0.00,starWishEndAmount=0.00,starWishAssetDetailVO=<null>,starWishContentAuditStatus=UNCHECKED,starWishContentLength=12,filterAtter=<null>],success=true,errorContext=<null>,displayMsg=<null>]";

        s = s.replaceAll("[a-zA-Z]+\\[", "[");
        s = s.replaceAll("=\\[", ":{");

        s = ReflectionUtils.replace(s, "=([^,]*),", matcher -> {
            String value = matcher.group(1);
            if (value.equalsIgnoreCase("<null>")) {
                return "null";
            }
            if(value.equalsIgnoreCase("true")||value.equalsIgnoreCase("true")){
                return value;
            }
            return String.format(":'%s',", value);
        });
        s = s.replaceAll("\\[", "{");
        s = s.replaceAll("]", "}");
        System.out.println(s);
        System.out.println(JSON.parseObject(s));
        System.out.println(JSON.toJSONString(JSON.parseObject(s),true));
        //        System.out.println(ReflectionUtils.apacheToStringBuilder2json(s));
        //        System.out.println(ReflectionUtils.apacheToStringBuilder2json(source));

        //        Codema.execPrint(new StringBuilderStyleSourceMachine().source(s));

    }

}