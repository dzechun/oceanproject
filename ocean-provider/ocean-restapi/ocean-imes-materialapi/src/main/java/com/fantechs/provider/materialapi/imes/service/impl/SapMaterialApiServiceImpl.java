package com.fantechs.provider.materialapi.imes.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIAL;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapMaterialApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@org.springframework.stereotype.Service
public class SapMaterialApiServiceImpl implements SapMaterialApiService {
    private static final Logger logger = LoggerFactory.getLogger(SapMaterialApiServiceImpl.class);

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private LogsUtils logsUtils;


    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码
    ReentrantLock lock = new ReentrantLock(true);

    @Override
    public int getMaterial(SearchSapMaterialApi searchSapMaterialApi) {
        if(lock.tryLock()) {
            try {
                Authenticator.setDefault(new BasicAuthenticator(userName, password));
                SIMESMATERIALQUERYOutService service = new SIMESMATERIALQUERYOutService();
                SIMESMATERIALQUERYOut out = service.getHTTPPort();
                DTMESMATERIALQUERYREQ req = new DTMESMATERIALQUERYREQ();
                List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
                if (StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");

                if (StringUtils.isEmpty(searchSapMaterialApi.getStartTime()) && StringUtils.isEmpty(searchSapMaterialApi.getEndTime())) {
                    String endTime = DateUtil.format(new Date(), "yyyyMMdd");
                    req.setERSDA("20180101");
                    req.setERSDAEND(endTime);
                } else if (StringUtils.isNotEmpty(searchSapMaterialApi.getStartTime()) || StringUtils.isNotEmpty(searchSapMaterialApi.getEndTime())) {
                    //(StringUtils.isEmpty(searchSapMaterialApi.getStartTime()) || StringUtils.isEmpty(searchSapMaterialApi.getEndTime()))

                    req.setERSDA(searchSapMaterialApi.getStartTime());
                    req.setERSDAEND(searchSapMaterialApi.getEndTime());
                } else {
                    throw new BizErrorException("开始和结束时间不能为空");
                }

                DTMESMATERIALQUERYRES res = out.siMESMATERIALQUERYOut(req);
                if (StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())) {
                    if (StringUtils.isEmpty(res.getMATS())) throw new BizErrorException("请求结果为空");
                    List<BaseMaterial> addList = new ArrayList<BaseMaterial>();
                    List<BaseMaterial> updateList = new ArrayList<BaseMaterial>();
                    for (DTMESMATERIAL material : res.getMATS()) {
                        if (StringUtils.isEmpty(material.getMATNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                        //判断物料信息为新增或者修改，并填充
                        saveAndUpdate(material, addList, updateList);
                    }
                    baseFeignApi.addList(addList);
                    baseFeignApi.batchUpdateByCode(updateList);
                    logsUtils.addlog((byte) 1, (byte) 1, orgIdList.get(0).getOrganizationId(), null, req.toString());
                    return 1;
                } else {
                    logsUtils.addlog((byte) 0, (byte) 1, orgIdList.get(0).getOrganizationId(), res.getMESSAGE(), req.getERSDA() + "-" + req.getERSDAEND());
                    throw new BizErrorException("接口请求失败,错误信息为：" + res.getMESSAGE());
                }
            }finally {
                lock.unlock();
            }
        }
        throw new BizErrorException("正在同步物料，请勿重新操作");
    }

    public void saveAndUpdate(DTMESMATERIAL material, List<BaseMaterial> addList, List<BaseMaterial> updateList){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        String materialCode =baseUtils.removeZero(material.getMATNR());
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();
        searchBaseMaterial.setMaterialCode(materialCode);
        searchBaseMaterial.setOrganizationId(orgId);
        ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
        BaseMaterial baseMaterial = new BaseMaterial();
        baseMaterial.setOrganizationId(orgId);

        if(StringUtils.isEmpty(list.getData())){
            baseMaterial.setMaterialName(material.getMAKTX());
            baseMaterial.setMaterialDesc(material.getMAKTX());
            baseMaterial.setMaterialCode(materialCode);
            baseMaterial.setCreateTime(new Date());
            baseMaterial.setModifiedTime(new Date());
            baseMaterial.setStatus((byte)1);
            baseMaterial.setSystemSource("ERP");
            baseMaterial.setIsDelete((byte)1);
            addList.add(baseMaterial);
        }else{
            baseMaterial = list.getData().get(0);
            baseMaterial.setMaterialName(material.getMAKTX());
            baseMaterial.setMaterialDesc(material.getMAKTX());
            baseMaterial.setSystemSource("ERP");
            updateList.add(baseMaterial);
        }

    }
}
