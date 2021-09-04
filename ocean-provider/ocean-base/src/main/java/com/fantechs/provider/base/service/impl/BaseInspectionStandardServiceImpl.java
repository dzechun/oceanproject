package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionStandardImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseInspectionStandardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseInspectionStandardServiceImpl extends BaseService<BaseInspectionStandard> implements BaseInspectionStandardService {

    @Resource
    private BaseInspectionStandardMapper baseInspectionStandardMapper;
    @Resource
    private BaseInspectionStandardDetMapper baseInspectionStandardDetMapper;
    @Resource
    private BaseHtInspectionStandardMapper baseHtInspectionStandardMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseSupplierMapper baseSupplierMapper;
    @Resource
    private BaseInspectionWayMapper baseInspectionWayMapper;
    @Resource
    private BaseMaterialSupplierMapper baseMaterialSupplierMapper;
    @Resource
    private BaseInspectionExemptedListMapper baseInspectionExemptedListMapper;

    @Override
    public List<BaseInspectionStandard> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        List<BaseInspectionStandard> baseInspectionStandards = baseInspectionStandardMapper.findList(map);
        SearchBaseInspectionStandardDet searchBaseInspectionStandardDet = new SearchBaseInspectionStandardDet();

        for (BaseInspectionStandard baseInspectionStandard : baseInspectionStandards) {
            searchBaseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
            List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandardDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionStandardDet));
            if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
                baseInspectionStandard.setBaseInspectionStandardDets(baseInspectionStandardDets);
            }
        }

        return baseInspectionStandards;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionStandard baseInspectionStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionStandardCode", baseInspectionStandard.getInspectionStandardCode());
        BaseInspectionStandard baseInspectionStandard1 = baseInspectionStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionStandard1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(baseInspectionStandard.getMaterialId()!=null && baseInspectionStandard.getMaterialId()==0){
            example.clear();
            example.createCriteria().andEqualTo("orgId", user.getOrganizationId())
                    .andEqualTo("materialId", baseInspectionStandard.getMaterialId())
                    .andEqualTo("inspectionWayId", baseInspectionStandard.getInspectionWayId());
            BaseInspectionStandard baseInspectionStandard2 = baseInspectionStandardMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseInspectionStandard2)) {
                throw new BizErrorException("该检验方式已有通用的检验标准");
            }
        }

        //新增检验标准
        baseInspectionStandard.setCreateUserId(user.getUserId());
        baseInspectionStandard.setCreateTime(new Date());
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setStatus(StringUtils.isEmpty(baseInspectionStandard.getStatus())?1:baseInspectionStandard.getStatus());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i = baseInspectionStandardMapper.insertUseGeneratedKeys(baseInspectionStandard);

        //新增检验标准明细
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets) {
                if(baseInspectionStandardDet.getInspectionTag()!=null && baseInspectionStandardDet.getInspectionTag()==(byte)2) {
                    if(StringUtils.isNotEmpty(baseInspectionStandardDet.getSpecificationUpperLimit(),baseInspectionStandardDet.getSpecificationFloor())) {
                        if (baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == -1
                                || baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == 0) {
                            throw new BizErrorException("规格上限必须大于规格下限");
                        }
                    }
                }
                baseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
                baseInspectionStandardDet.setCreateUserId(user.getUserId());
                baseInspectionStandardDet.setCreateTime(new Date());
                baseInspectionStandardDet.setModifiedUserId(user.getUserId());
                baseInspectionStandardDet.setModifiedTime(new Date());
                baseInspectionStandardDet.setStatus(StringUtils.isEmpty(baseInspectionStandardDet.getStatus())?1:baseInspectionStandardDet.getStatus());
                baseInspectionStandardDet.setOrgId(user.getOrganizationId());
            }
            baseInspectionStandardDetMapper.insertList(baseInspectionStandardDets);
        }

        //履历
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insert(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionStandard baseInspectionStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionStandardCode", baseInspectionStandard.getInspectionStandardCode())
                .andNotEqualTo("inspectionStandardId",baseInspectionStandard.getInspectionStandardId());
        BaseInspectionStandard baseInspectionStandard1 = baseInspectionStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionStandard1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(baseInspectionStandard.getMaterialId()!=null && baseInspectionStandard.getMaterialId()==0){
            example.clear();
            example.createCriteria().andEqualTo("orgId",user.getOrganizationId())
                    .andEqualTo("materialId",baseInspectionStandard.getMaterialId())
                    .andEqualTo("inspectionWayId",baseInspectionStandard.getInspectionWayId())
                    .andNotEqualTo("inspectionStandardId",baseInspectionStandard.getInspectionStandardId());
            BaseInspectionStandard baseInspectionStandard2 = baseInspectionStandardMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseInspectionStandard2)){
                throw new BizErrorException("该检验方式已有通用的检验标准");
            }
        }

        //修改检验标准
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i=baseInspectionStandardMapper.updateByPrimaryKeySelective(baseInspectionStandard);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<BaseInspectionStandardDet> baseInspectionStandardDetList = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDetList)) {
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDetList) {
                if (StringUtils.isNotEmpty(baseInspectionStandardDet.getInspectionStandardDetId())) {
                    baseInspectionStandardDetMapper.updateByPrimaryKeySelective(baseInspectionStandardDet);
                    idList.add(baseInspectionStandardDet.getInspectionStandardDetId());
                }
            }
        }

        //删除原有检验标准明细
        Example example1 = new Example(BaseInspectionStandardDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
        if(idList.size()>0){
            criteria1.andNotIn("inspectionStandardDetId",idList);
        }
        baseInspectionStandardDetMapper.deleteByExample(example1);

        //新增检验标准明细
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets) {
                if(idList.contains(baseInspectionStandardDet.getInspectionStandardDetId())){
                    continue;
                }
                baseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
                baseInspectionStandardDet.setCreateUserId(user.getUserId());
                baseInspectionStandardDet.setCreateTime(new Date());
                baseInspectionStandardDet.setModifiedUserId(user.getUserId());
                baseInspectionStandardDet.setModifiedTime(new Date());
                baseInspectionStandardDet.setStatus(StringUtils.isEmpty(baseInspectionStandardDet.getStatus())?1:baseInspectionStandardDet.getStatus());
                baseInspectionStandardDet.setOrgId(user.getOrganizationId());
                baseInspectionStandardDetMapper.insert(baseInspectionStandardDet);
            }
        }

        //履历
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insert(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtInspectionStandard> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionStandard baseInspectionStandard = baseInspectionStandardMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionStandard)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
            BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
            list.add(baseHtInspectionStandard);

            //删除相关检验标准明细
            Example example1 = new Example(BaseInspectionStandardDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
            baseInspectionStandardDetMapper.deleteByExample(example1);
        }

        //履历
        baseHtInspectionStandardMapper.insertList(list);

        return baseInspectionStandardMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseInspectionStandardImport> baseInspectionStandardImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseInspectionStandard> list = new LinkedList<>();
        LinkedList<BaseInspectionStandardDet> detList = new LinkedList<>();
        LinkedList<BaseHtInspectionStandard> htList = new LinkedList<>();
        LinkedList<BaseInspectionStandardImport> inspectionStandardImports = new LinkedList<>();
        for (int i = 0; i < baseInspectionStandardImports.size(); i++) {
            BaseInspectionStandardImport baseInspectionStandardImport = baseInspectionStandardImports.get(i);

            String inspectionStandardCode = baseInspectionStandardImport.getInspectionStandardCode();
            String inspectionStandardName = baseInspectionStandardImport.getInspectionStandardName();
            String materialCode = baseInspectionStandardImport.getMaterialCode();
            String supplierCode = baseInspectionStandardImport.getSupplierCode();
            Integer inspectionType = baseInspectionStandardImport.getInspectionType();
            String inspectionWayCode = baseInspectionStandardImport.getInspectionWayCode();

            if (StringUtils.isEmpty(
                    inspectionStandardCode,inspectionStandardName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseInspectionStandard.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("inspectionStandardCode", inspectionStandardCode);
            if (StringUtils.isNotEmpty(baseInspectionStandardMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //检验方式
            if(StringUtils.isNotEmpty(inspectionWayCode,inspectionType)){
                Example example2 = new Example(BaseInspectionWay.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("orgId", user.getOrganizationId())
                        .andEqualTo("inspectionWayCode",inspectionWayCode)
                        .andEqualTo("inspectionType",inspectionType);
                BaseInspectionWay baseInspectionWay = baseInspectionWayMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseInspectionWay)){
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setInspectionWayId(baseInspectionWay.getInspectionWayId());
            }

            //物料编码是否为空，为空则为通用检验标准
            if(StringUtils.isNotEmpty(materialCode)){
                Example example1 = new Example(BaseMaterial.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("organizationId", user.getOrganizationId())
                         .andEqualTo("materialCode",materialCode);
                BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseMaterial)){
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setMaterialId(baseMaterial.getMaterialId());
            }else {
                baseInspectionStandardImport.setMaterialId((long)0);
                example.clear();
                example.createCriteria().andEqualTo("orgId", user.getOrganizationId())
                        .andEqualTo("materialId",0)
                        .andEqualTo("inspectionWayId", baseInspectionStandardImport.getInspectionWayId());
                BaseInspectionStandard baseInspectionStandard = baseInspectionStandardMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(baseInspectionStandard)) {
                    fail.add(i+4);
                    continue;
                }
            }

            //客户编码
            if(StringUtils.isNotEmpty(supplierCode)){
                //该客户是否存在
                Example example3 = new Example(BaseSupplier.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", user.getOrganizationId())
                        .andEqualTo("supplierCode",supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }

                //该客户是否与物料关联
                Example example4 = new Example(BaseMaterialSupplier.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("materialId", baseInspectionStandardImport.getMaterialId())
                        .andEqualTo("supplierId",baseSupplier.getSupplierId());
                BaseMaterialSupplier baseMaterialSupplier = baseMaterialSupplierMapper.selectOneByExample(example4);
                if (StringUtils.isEmpty(baseMaterialSupplier)){
                    fail.add(i+4);
                    continue;
                }

                //客户的物料是否存在于免检清单
                Example example5 = new Example(BaseInspectionExemptedList.class);
                Example.Criteria criteria5 = example5.createCriteria();
                criteria5.andEqualTo("materialId", baseInspectionStandardImport.getMaterialId())
                        .andEqualTo("supplierId",baseSupplier.getSupplierId())
                        .andEqualTo("objType",1);
                List<BaseInspectionExemptedList> baseInspectionExemptedLists1 = baseInspectionExemptedListMapper.selectByExample(example5);

                example5.clear();
                example5.createCriteria().andEqualTo("materialId", baseInspectionStandardImport.getMaterialId())
                        .andEqualTo("customerId",baseSupplier.getSupplierId())
                        .andEqualTo("objType",2);
                List<BaseInspectionExemptedList> baseInspectionExemptedLists2 = baseInspectionExemptedListMapper.selectByExample(example5);
                if(StringUtils.isNotEmpty(baseInspectionExemptedLists1,baseInspectionExemptedLists2)){
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setSupplierId(baseSupplier.getSupplierId());
            }

            inspectionStandardImports.add(baseInspectionStandardImport);
        }


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
