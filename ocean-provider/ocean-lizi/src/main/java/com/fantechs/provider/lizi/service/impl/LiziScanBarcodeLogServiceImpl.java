package com.fantechs.provider.lizi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.lizi.entity.LiziScanBarcodeLog;
import com.fantechs.provider.lizi.entity.dto.LiziScanBarcodeLogDto;
import com.fantechs.provider.lizi.entity.dto.uploadApiDto;
import com.fantechs.provider.lizi.entity.search.SearchLiziScanBarcodeLog;
import com.fantechs.provider.lizi.mapper.LiziScanBarcodeLogMapper;
import com.fantechs.provider.lizi.service.LiziScanBarcodeLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/16.
 */
@Service
public class LiziScanBarcodeLogServiceImpl extends BaseService<LiziScanBarcodeLog> implements LiziScanBarcodeLogService {

    @Resource
    private LiziScanBarcodeLogMapper liziScanBarcodeLogMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    //上传接口
    private final String address_prod = "https://factory-qa-service.viomi.com.cn/common/addDevice";
    private final  String address_dev = "https://factory-qa-service-test.viomi.com.cn/common/addDevice";
    //private final  String key = "e17erh3h4h4358270ef86cwfwef058d723a0a";
    private final String key = "e17sr6sy4fh8uo386cwfwef050d7g9a0a";
    private final  String type = "13";

    @Override
    public List<LiziScanBarcodeLogDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return liziScanBarcodeLogMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public LiziScanBarcodeLogDto add(String sn) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] material = sn.split("/");
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setIdCode(material[0]);
        ResponseEntity<List<BaseMaterial>> responseEntity = baseFeignApi.findList(searchBaseMaterial);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        if(responseEntity.getData().isEmpty()){
            throw new BizErrorException("获取物料信息失败");
        }
        LiziScanBarcodeLog liziScanBarcodeLog = new LiziScanBarcodeLog();
        liziScanBarcodeLog.setMaterialId(responseEntity.getData().get(0).getMaterialId());
        liziScanBarcodeLog.setMaterialCode(responseEntity.getData().get(0).getMaterialCode());
        liziScanBarcodeLog.setMaterialName(responseEntity.getData().get(0).getMaterialName());
        liziScanBarcodeLog.setBarcode(sn);
        liziScanBarcodeLog.setQty(BigDecimal.ONE);
        liziScanBarcodeLog.setCreateTime(new Date());
        liziScanBarcodeLog.setCreateUserId(sysUser.getUserId());
        liziScanBarcodeLog.setModifiedTime(new Date());
        liziScanBarcodeLog.setModifiedUserId(sysUser.getUserId());
        liziScanBarcodeLog.setOrgId(sysUser.getOrganizationId());
        liziScanBarcodeLogMapper.insertUseGeneratedKeys(liziScanBarcodeLog);
        //上传接口
        Map<String,String> map = new HashMap<>();
        map.put("sn",sn);
        map.put("sn_time", DateUtils.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        uploadApiDto uploadApiDto = new uploadApiDto();
        uploadApiDto.setKey(key);
        if(StringUtils.isEmpty(responseEntity.getData().get(0).getRemark())){
            uploadApiDto.setType(type);
        }else {
            uploadApiDto.setType(responseEntity.getData().get(0).getRemark());
        }
        uploadApiDto.setData(JSONObject.toJSONString(map));
        String jsonArray = JSONObject.toJSONString(uploadApiDto);

        String res = this.uploadApi(address_dev,jsonArray, sysUser.getOrganizationId());

        SearchLiziScanBarcodeLog searchLiziScanBarcodeLog = new SearchLiziScanBarcodeLog();
        searchLiziScanBarcodeLog.setScanBarcodeLogId(liziScanBarcodeLog.getScanBarcodeLogId());
        LiziScanBarcodeLogDto liziScanBarcodeLogDto = this.findList(ControllerUtil.dynamicConditionByEntity(searchLiziScanBarcodeLog)).get(0);
        liziScanBarcodeLogDto.setOption1(res);
        return liziScanBarcodeLogDto;
    }

    /**
     * 上传云米QMES设备信息
     * @param address
     * @param jsonArray
     * @param orgId
     */
    private String uploadApi(String address, String jsonArray, Long orgId){
        StringBuilder sb = new StringBuilder();
        byte result=0;//调用结果(0-失败 1-成功)
        String host = "";
        //q开始时间
        Long beginTime = new Date().getTime();
        try {
            // URL连接
            URL url = new URL(address);
            host = InetAddress.getByName(url.getHost()).getHostAddress();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(jsonArray.getBytes().length));
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);

            // 请求输入内容
            OutputStream output = conn.getOutputStream();
            output.write(jsonArray.getBytes());
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
            result=1;
        }catch (Exception ex) {
            sb.append("错误信息："+ex.getMessage());
            ex.printStackTrace();
        }
        //结束时间
        Long opetime =new Date().getTime()-beginTime;
        //接口日志
        this.addlog(result,(byte)1,orgId,sb.toString(),jsonArray,address,host,BigDecimal.valueOf(opetime));
        return sb.toString();
    }

    /**
     * 日志
     * @param result
     * @param type
     * @param orgId
     * @param responseData
     * @param requestParameter
     */
    public void  addlog(Byte result,Byte type,Long orgId,String responseData,String requestParameter,String remark,String host,BigDecimal time){
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("栗子条码上传");
        sysApiLog.setThirdpartyApiIpPort(host);
        sysApiLog.setCallResult(result);
        sysApiLog.setApiUrl(remark);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-lizi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        sysApiLog.setRemark(remark);
        sysApiLog.setConsumeTime(time);
        securityFeignApi.add(sysApiLog);
    }
}
