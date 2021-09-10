package com.fantechs.provider.chinafiveringapi.api.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.chinafiveringapi.api.service.ExportDataService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ExportDataServiceImpl implements ExportDataService {

    // 接口地址
    private final String address = "http://mattest.cwcec.com/LocWebServices/WebService1.asmx";


    @Override
    /**
     * create by: Dylan
     * description: 入库单回传接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto writeDeliveryDetails(String jsonVoiceArray, String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try{
            baseExecuteResultDto= callWebService(address,"writeDeliveryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

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
        try{
            baseExecuteResultDto= callWebService(address,"writeMakeInventoryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

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
        try{
            baseExecuteResultDto= callWebService(address,"writeIssueDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

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
        try{
            baseExecuteResultDto= callWebService(address,"writeMoveInventoryDetails",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

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
        try{
            baseExecuteResultDto= callWebService(address,"writePackingLists",jsonVoiceArray,projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());

            baseExecuteResultDto.setIsSuccess(true);

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    private  String actionBySOAP(String method,String JsonVoiceArray,String projectID){
        String str="";
        if(StringUtils.isEmpty(projectID)){
            str="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:"+method+"/>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
        }
        else {
            str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:" + method + ">\n" +
                    "         <tem:JsonVoiceArray>" + JsonVoiceArray + "</tem:JsonVoiceArray>\n" +
                    "         <tem:projectID>" + projectID + "</tem:projectID>\n" +
                    "      </tem:" + method + ">\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

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
            conn.setRequestProperty("Content-Type", "text/xml; charset=GBK");
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
