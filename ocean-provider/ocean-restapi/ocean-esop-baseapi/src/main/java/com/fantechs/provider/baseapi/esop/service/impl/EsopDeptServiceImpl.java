package com.fantechs.provider.baseapi.esop.service.impl;


import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopDeptMapper;
import com.fantechs.provider.baseapi.esop.service.EsopDeptService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopDeptServiceImpl extends BaseService<EsopDept> implements EsopDeptService {

    @Resource
    private EsopDeptMapper esopDeptMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;


    @Override
    @GlobalTransactional
    public List<BaseDept> addDept(Map<String, Object> map) throws ParseException {
        map.put("isWas",1); //1为各个分公司
        map.put("isDeleted",0); //新宝0为未删除 1为已删除
        List<EsopDept> list = esopDeptMapper.findList(map);
        List<BaseDept> baseDepts = new ArrayList<BaseDept>();
        List<BaseOrganizationDto> baseOrganizationDtos = baseUtils.getOrId();
        if(StringUtils.isNotEmpty(list) && StringUtils.isNotEmpty(baseOrganizationDtos)){
            //根据目前需求，每个组织都需要基础数据，因此每个组织拷贝一份
            for(BaseOrganizationDto dto : baseOrganizationDtos){
                for(EsopDept dept :list){
                    baseDepts.add(getBaseDepy(dept,dto.getOrganizationId()));
                }
            }
        }
        ResponseEntity<List<BaseDept>> baseDeptlist = baseFeignApi.batchAddDept(baseDepts);
    //    logsUtils.addlog((byte)1,(byte)1,orgId,null,null);
        return baseDeptlist.getData();
    }


    public BaseDept getBaseDepy(EsopDept esopDept,Long orgId){
        BaseDept baseDept = new BaseDept();
        baseDept.setDeptName(esopDept.getName());
        baseDept.setDeptCode(esopDept.getCode());
        baseDept.setDeptDesc(esopDept.getName());
        baseDept.setCreateTime(esopDept.getCreatedTime());
        baseDept.setModifiedTime(esopDept.getModifyTime());
        baseDept.setIsDelete((byte)1);
        baseDept.setStatus(1);
        //新宝未使用工厂，默认工厂关联部门与车间
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        searchBaseFactory.setOrgId(orgId);
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        if(StringUtils.isNotEmpty(factoryList.getData()))
            baseDept.setFactoryId(factoryList.getData().get(0).getFactoryId());
        baseDept.setIsDelete(esopDept.getIsDeleted());
        baseDept.setOrganizationId(orgId);
        return baseDept;
    }
}
