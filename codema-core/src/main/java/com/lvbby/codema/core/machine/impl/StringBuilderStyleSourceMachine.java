package com.lvbby.codema.core.machine.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lvbby.codema.core.AbstractBaseMachine;
import com.lvbby.codema.core.utils.ReflectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author dushang.lp
 * @version $Id: JsonDataSourceMachine.java, v 0.1 2018年01月24日 下午6:14 dushang.lp Exp $
 */
public class StringBuilderStyleSourceMachine extends AbstractBaseMachine<String, JSONObject> {
    @Override
    protected void doCode() throws Exception {
//        eventPayload=ContractEvent[contractType=P2P_SERVICE_CONTRACT
        //com.alipay.common.event.UniformEvent@179ce2c6[localTxMode=false,localDatasourceId=,localEventDataSourceId=,id=897a9f242e2a2495cac8430a8b780d0d,topic=TP_M_FINANCE,eventCode=EC_finbatchcore_contract_task,eventType=<null>,eventName=<null>,transactional=false,eventTxSynListener=<null>,serverId=chonghuan-MacBook.local,gmtOccur=Wed Jan 24 14:57:10 CST 2018,clientIp=<null>,clientMac=<null>,clientId=<null>,clientPCIDMac=<null>,clientPCIDHwid=<null>,clientPCIDGuid=<null>,umidHardVersion=<null>,umidSysInfo=<null>,umidHardInfo=<null>,umidSoftInfo=<null>,umidRID=<null>,umidSoftVersion=<null>,sessionId=<null>,eventPayload=ContractEvent[contractType=P2P_SERVICE_CONTRACT,bizSource=appointment,arrangementNo=20180115W2020315000005031889,outBizNo=2018012400903001000000000000880000001724,productId=<null>,userId=2088302249335889,loanUserId=<null>,contractTemplateId=20180104000690000001000000000013,shouldContractSign=true,contractSignData=ContractSignData[outBizId=<null>,userId=2088302249335889,certNo=621200197710308149,userName=重幻二,oppositePersonal=false,oppositeInstId=Z98,oppositeUserId=<null>,oppositeCertNo=<null>,oppositeUserName=<null>,contractId=<null>,signBizId=<null>,extInfo=<null>],contractParams=<null>],principal=<null>,txId=<null>,priority=5,sendOnceMessage=false,crossIDC=false,hostName=chonghuan-MacBook.local,throwExceptionOnFailed=true]
        System.out.println(source);
        String s = source.replaceAll("[^\\[\\],=]+\\[", "{");
        s=s.replace("<null>","null");
        s=ReflectionUtils.replace(s,"[=]([^=,\\]]+),",matcher -> String.format("=%s,", convert(matcher.group(1))));
        s=s.replace("=,",":null,");
        s=s.replaceAll("=",":");
        s=s.replaceAll("]","}");
        System.out.println(s);
        handle(new JsonResult(JSON.parseObject(s)));
    }
    private String convert(String value){
        if("null".equalsIgnoreCase(value)){
            return value;
        }
        if (Lists.newArrayList("true","false").contains(value)) {
            return value;
        }
        //TODO Date
        return String.format("\"%s\"", value);
    }

    //解析Date输出的格林威治时间
    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.parse(date);
    }

    public static void main(String[] args) throws Exception {
        String source = "com.alipay.common.event.UniformEvent@179ce2c6[localTxMode=false,localDatasourceId=,localEventDataSourceId=,id=897a9f242e2a2495cac8430a8b780d0d,topic=TP_M_FINANCE,eventCode=EC_finbatchcore_contract_task,eventType=<null>,eventName=<null>,transactional=false,eventTxSynListener=<null>,serverId=chonghuan-MacBook.local,gmtOccur=Wed Jan 24 14:57:10 CST 2018,clientIp=<null>,clientMac=<null>,clientId=<null>,clientPCIDMac=<null>,clientPCIDHwid=<null>,clientPCIDGuid=<null>,umidHardVersion=<null>,umidSysInfo=<null>,umidHardInfo=<null>,umidSoftInfo=<null>,umidRID=<null>,umidSoftVersion=<null>,sessionId=<null>,eventPayload=ContractEvent[contractType=P2P_SERVICE_CONTRACT,bizSource=appointment,arrangementNo=20180115W2020315000005031889,outBizNo=2018012400903001000000000000880000001724,productId=<null>,userId=2088302249335889,loanUserId=<null>,contractTemplateId=20180104000690000001000000000013,shouldContractSign=true,contractSignData=ContractSignData[outBizId=<null>,userId=2088302249335889,certNo=621200197710308149,userName=重幻二,oppositePersonal=false,oppositeInstId=Z98,oppositeUserId=<null>,oppositeCertNo=<null>,oppositeUserName=<null>,contractId=<null>,signBizId=<null>,extInfo=<null>],contractParams=<null>],principal=<null>,txId=<null>,priority=5,sendOnceMessage=false,crossIDC=false,hostName=chonghuan-MacBook.local,throwExceptionOnFailed=true]";
//        Codema.execPrint(new StringBuilderStyleSourceMachine().source(source).next(DataSrcMachineFactory.toJsonString(true)));
        String date = "Wed Jan 24 14:57:10 CST 2018";
        System.out.println(parseDate(date));
    }

}