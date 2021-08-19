package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.SkipHttpsUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.baseapi.esop.service.EsopWorkOrderApiService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
public class EsopWorkOrderApiServiceImpl implements EsopWorkOrderApiService {
    private static final Logger log = LoggerFactory.getLogger(EsopLineServiceImpl.class);
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private PMFeignApi pmFeignApi;


    private String url = "http://xbqms.donlim.com:8081/qms/qualitiy/esop/order"; //新宝工单接口地址

    @Override
    @LcnTransaction
    public int getWorkOrder(String proCode) {
        if (StringUtils.isEmpty(proCode)) throw new BizErrorException("产线id不能为空");
        String result = null;
        try {
            URL realUrl = new URL(url +"?lineId="+ proCode);
            CloseableHttpClient httpClient = SkipHttpsUtil.wrapClient();
            HttpGet get = new HttpGet(String.valueOf(realUrl));
            CloseableHttpResponse response = null;
            Long orgId = baseUtils.getOrId();
            BaseMaterial material = null;
            try {
                response = httpClient.execute(get);
                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                    Map<String, Object> map = JsonUtils.jsonToMap(result);
                    log.info("-----map----"+map);
                    Map<String, Object> data = (Map<String, Object>) map.get("data");
                    //校验产品编码(新宝一级公司没有物料因此产品编码当做物料使用存入物料表)
                    if(StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(data.get("product_model"))){
                        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                        searchBaseMaterial.setMaterialCode((String)data.get("product_model"));
                        searchBaseMaterial.setOrganizationId(baseUtils.getOrId());
                        ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);

                        if(StringUtils.isEmpty(list.getData())) {
                            BaseMaterial baseMaterial =  new BaseMaterial();
                            baseMaterial.setMaterialCode((String)data.get("product_model"));
                            baseMaterial.setMaterialName((String)data.get("product_name"));
                            baseMaterial.setMaterialDesc((String)data.get("product_name"));
                            baseMaterial.setOrganizationId(orgId);
                            ResponseEntity<BaseMaterial> baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
                             material = baseMaterialResponseEntity.getData();
                        }else{
                            material = list.getData().get(0);
                        }


                        SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
                        searchBaseProLine.setProCode(proCode);
                        searchBaseProLine.setOrgId(orgId);
                        ResponseEntity<List<BaseProLine>> baseProLines = baseFeignApi.findList(searchBaseProLine);
                        if(StringUtils.isEmpty(baseProLines.getData()))
                            throw new BizErrorException("未查询到对应的产线,请先同步产线"+proCode);

                        MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
                        mesPmWorkOrder.setWorkOrderCode(String.valueOf(data.get("code")));
                        mesPmWorkOrder.setWorkOrderQty(new BigDecimal((int)data.get("num")));
                        mesPmWorkOrder.setProLineId(baseProLines.getData().get(0).getProLineId());
                        mesPmWorkOrder.setPlanStartTime(new Date());
                        mesPmWorkOrder.setWorkOrderStatus((byte)3);
                        mesPmWorkOrder.setWorkOrderType((byte)0);
                        mesPmWorkOrder.setMaterialId(material.getMaterialId());
                        mesPmWorkOrder.setIsDelete((byte)1);
                        mesPmWorkOrder.setOrgId(orgId);
                        pmFeignApi.saveByApi(mesPmWorkOrder);
                    }else{
                //        logsUtils.addlog((byte)2,(byte)1,orgId,result,proCode);
                    }


                }
             //   logsUtils.addlog((byte)1,(byte)1,orgId,result,proCode);
                return 1;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    return 0;
    }

    @Override
    public int getAllWorkOrder(SearchBaseProLine searchBaseProLine) {
        searchBaseProLine.setOrgId(baseUtils.getOrId());
        ResponseEntity<List<BaseProLine>> list = baseFeignApi.findList(searchBaseProLine);
        if(StringUtils.isNotEmpty(list.getData())){
            for(BaseProLine baseProLine : list.getData()){
               this.getWorkOrder(baseProLine.getProCode());
            }
        }
        return 1;
    }

}
