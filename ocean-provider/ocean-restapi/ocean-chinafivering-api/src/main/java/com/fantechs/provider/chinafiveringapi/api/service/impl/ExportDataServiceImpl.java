package com.fantechs.provider.chinafiveringapi.api.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.chinafiveringapi.api.service.ExportDataService;
import com.fantechs.provider.chinafiveringapi.api.utils.LogsUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ExportDataServiceImpl implements ExportDataService {

    // 接口地址 http://mattest.cwcec.com/LocWebServices/WebService1.asmx
    // Webservice接口地址(测试环境)
    //private final String address = "http://mattest.cwcec.com/LocWebServices/WebService1.asmx";
    //Webservice接口地址(生产环境)
    private final String address = "http://mat.cwcec.com/LocWebServices/WebService1.asmx";

    @Resource
    LogsUtils logsUtils;

    @Override
    /**
     * create by: Dylan
     * description: 入库单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeDeliveryDetails(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writeDeliveryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writeDeliveryDetails");
        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 盘点单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeMakeInventoryDetails(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writeMakeInventoryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writeMakeInventoryDetails");
        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 出库单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeIssueDetails(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writeIssueDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writeIssueDetails");
        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 调拨单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeMoveInventoryDetails(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writeMoveInventoryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writeMoveInventoryDetails");
        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 箱单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writePackingLists(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writePackingLists",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());
            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writePackingLists");

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 库位信息回传
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeShelvesNo(String jsonVoiceArray, String projectID) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"writeShelvesNo",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());
            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,jsonVoiceArray,"writeShelvesNo");

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 封单信息回传
     * create time:
     * @return
     */
    public BaseExecuteResultDto overIssue(String ISGUID, String userName) throws Exception {
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        byte result=0;//调用结果(0-失败 1-成功)
        String str="";
        try{
            baseExecuteResultDto= callWebService(address,"overIssue",ISGUID,userName);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());
            //success
            str=baseExecuteResultDto.getExecuteResult().toString();
            if(str.contains("success")){
                result=1;
                baseExecuteResultDto.setIsSuccess(true);
            }
            else {
                throw new BizErrorException(str);
            }

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        String paraMeter="ISGUID:"+ISGUID+","+"userName:"+userName;

        //记录日志
        logsUtils.addlog(result,(byte)1,1004L,str,paraMeter,"overIssue");

        return baseExecuteResultDto;
    }

    private  String actionBySOAP(String method, String JsonVoiceArray, String projectID){
        String str="";
        if(method.equals("overIssue")){
            str="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:overIssue>\n" +
                    "         <tem:isguid>" + JsonVoiceArray + "</tem:isguid>\n" +
                    "         <tem:username>" + projectID + "</tem:username>\n" +
                    "      </tem:overIssue>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
        }
        else {
            if (StringUtils.isEmpty(projectID)) {
                str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <tem:" + method + "/>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
            } else {

                str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <tem:" + method + ">\n" +
                        "         <tem:JsonVoiceArray>" + JsonVoiceArray + "</tem:JsonVoiceArray>\n" +
                        "         <tem:ProjectID>3919</tem:ProjectID>\n" +
                        "      </tem:" + method + ">\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

            }
        }

        return str;
    }

    private BaseExecuteResultDto callWebService(String wsdl, String method, String JsonVoiceArray, String projectID) throws Exception {
        StringBuilder sb = new StringBuilder();
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "20000");
            System.setProperty("sun.net.client.defaultReadTimeout", "20000");

            // URL连接
            URL url = new URL(wsdl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(actionBySOAP(method, JsonVoiceArray,projectID).getBytes().length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);

            // 请求输入内容
            OutputStream output = conn.getOutputStream();
            output.write(actionBySOAP(method,JsonVoiceArray, projectID).getBytes());
            output.flush();
            output.close();
            // 请求返回内容
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n");
            }
            br.close();
            isr.close();

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex) {
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        baseExecuteResultDto.setExecuteResult(sb.toString());
        return baseExecuteResultDto;
    }
}
