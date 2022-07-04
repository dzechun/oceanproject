package com.fantechs.provider.baseapi.esop.service.impl;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.SkipHttpsUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.service.EsopMaterialService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopMaterialServiceImpl implements EsopMaterialService {

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;


    private String url = "http://xbqms.donlim.com:8081/qms/qualitiy/esop/material"; //新宝工单接口地址

    @Override
    @GlobalTransactional
    public BaseMaterial getMaterial(String materialCode) {
        if (StringUtils.isEmpty(materialCode)) throw new BizErrorException("物料（产品）编码不能为空");
        String result = null;
      //  物料在esop系统调用，有用户，直接获取
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        BaseMaterial material = null;
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        searchBaseMaterial.setOrganizationId(sysUser.getOrganizationId());
        List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
        if(StringUtils.isNotEmpty(baseMaterials)) {
            material = baseMaterials.get(0);
        }else {
            try {
                URL realUrl = new URL(url + "?code=" + materialCode);
                CloseableHttpClient httpClient = SkipHttpsUtil.wrapClient();
                HttpGet get = new HttpGet(String.valueOf(realUrl));
                CloseableHttpResponse response = null;
                try {
                    response = httpClient.execute(get);
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                    Map<String, Object> map = JsonUtils.jsonToMap(result);
                    Map<String, Object> data = (Map<String, Object>) map.get("data");
                    //校验产品编码(新宝一级公司没有物料因此产品编码当做物料使用存入物料表)
                    if (StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(data.get("code"))) {
                        BaseMaterial baseMaterial = new BaseMaterial();
                        baseMaterial.setMaterialName((String) data.get("name"));
                        baseMaterial.setMaterialCode((String) data.get("code"));
                        baseMaterial.setMaterialDesc((String) data.get("specs"));
                        baseMaterial.setOrganizationId(sysUser.getOrganizationId());
                        baseMaterial.setStatus((byte) 1);
                        baseMaterial.setIsDelete((byte) 1);
                        material = baseFeignApi.saveByApi(baseMaterial).getData();

                        //绑定产品型号
                        SearchBaseProductModel searchBaseProductModel = new SearchBaseProductModel();
                        searchBaseProductModel.setProductModelCode((String) data.get("product_model"));
                        List<BaseProductModel> baseProductModel = baseFeignApi.findList(searchBaseProductModel).getData();
                        BaseTab tab = new BaseTab();
                        tab.setMaterialId(material.getMaterialId());
                        tab.setProductModelId(baseProductModel.get(0).getProductModelId());
                        baseFeignApi.addTab(tab);

                    }
               //    logsUtils.addlog((byte) 1, (byte) 1, orgId, result, materialCode);
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
        }
        return material;
    }
}
