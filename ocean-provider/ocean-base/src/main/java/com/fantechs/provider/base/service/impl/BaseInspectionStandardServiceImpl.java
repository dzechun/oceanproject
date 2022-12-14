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
import java.util.stream.Collectors;

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
    @Resource
    private BaseInspectionItemMapper baseInspectionItemMapper;
    @Resource
    private BaseSampleProcessMapper baseSampleProcessMapper;

    @Override
    public List<BaseInspectionStandard> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
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

        //????????????????????????
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
                throw new BizErrorException("??????????????????????????????????????????");
            }
        }

        //??????????????????
        baseInspectionStandard.setCreateUserId(user.getUserId());
        baseInspectionStandard.setCreateTime(new Date());
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setStatus(StringUtils.isEmpty(baseInspectionStandard.getStatus())?1:baseInspectionStandard.getStatus());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i = baseInspectionStandardMapper.insertUseGeneratedKeys(baseInspectionStandard);

        //????????????????????????
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets) {
                if(StringUtils.isEmpty(baseInspectionStandardDet.getInspectionTag())){
                    throw new BizErrorException("????????????????????????");
                }else if(baseInspectionStandardDet.getInspectionTag()==(byte)2) {
                    if(StringUtils.isNotEmpty(baseInspectionStandardDet.getSpecificationUpperLimit(),baseInspectionStandardDet.getSpecificationFloor())) {
                        if (baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == -1
                                || baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == 0) {
                            throw new BizErrorException("????????????????????????????????????");
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

        //??????
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insertSelective(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionStandard baseInspectionStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //????????????????????????
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
                throw new BizErrorException("??????????????????????????????????????????");
            }
        }

        //??????????????????
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i=baseInspectionStandardMapper.updateByPrimaryKeySelective(baseInspectionStandard);

        //???????????????????????????
        ArrayList<Long> idList = new ArrayList<>();
        List<BaseInspectionStandardDet> baseInspectionStandardDetList = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDetList)) {
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDetList) {
                if (StringUtils.isNotEmpty(baseInspectionStandardDet.getInspectionStandardDetId())) {
                    baseInspectionStandardDetMapper.updateByPrimaryKey(baseInspectionStandardDet);
                    idList.add(baseInspectionStandardDet.getInspectionStandardDetId());
                }
            }
        }

        //??????????????????????????????
        Example example1 = new Example(BaseInspectionStandardDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
        if(idList.size()>0){
            criteria1.andNotIn("inspectionStandardDetId",idList);
        }
        baseInspectionStandardDetMapper.deleteByExample(example1);

        //????????????????????????
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

        //??????
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insertSelective(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

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

            //??????????????????????????????
            Example example1 = new Example(BaseInspectionStandardDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
            baseInspectionStandardDetMapper.deleteByExample(example1);
        }

        //??????
        baseHtInspectionStandardMapper.insertList(list);

        return baseInspectionStandardMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseInspectionStandardImport> baseInspectionStandardImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<BaseInspectionStandard> list = new LinkedList<>();
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
            String inspectionItemCodeBig = baseInspectionStandardImport.getInspectionItemCodeBig();
            String inspectionItemCodeSmall = baseInspectionStandardImport.getInspectionItemCodeSmall();
            String sampleProcessCode = baseInspectionStandardImport.getSampleProcessCode();

            if (StringUtils.isEmpty(
                    inspectionStandardCode,inspectionStandardName
            )){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
            Example example = new Example(BaseInspectionStandard.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("inspectionStandardCode", inspectionStandardCode);
            if (StringUtils.isNotEmpty(baseInspectionStandardMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
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
            }

            //????????????
            if(StringUtils.isNotEmpty(supplierCode)){
                //?????????????????????
                Example example3 = new Example(BaseSupplier.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", user.getOrganizationId())
                        .andEqualTo("supplierCode",supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }

                //??????????????????????????????
                Example example4 = new Example(BaseMaterialSupplier.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("materialId", baseInspectionStandardImport.getMaterialId())
                        .andEqualTo("supplierId",baseSupplier.getSupplierId());
                BaseMaterialSupplier baseMaterialSupplier = baseMaterialSupplierMapper.selectOneByExample(example4);
                if (StringUtils.isEmpty(baseMaterialSupplier)){
                    fail.add(i+4);
                    continue;
                }

                //??????????????????????????????????????????
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

            //????????????
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

            //???????????????????????????????????????????????????????????????
            if(StringUtils.isEmpty(materialCode,supplierCode)) {
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

            //---------??????-----------
            //????????????
            Example example6 = new Example(BaseInspectionItem.class);
            Example.Criteria criteria6 = example6.createCriteria();
            if(StringUtils.isNotEmpty(inspectionItemCodeSmall)){
                criteria6.andEqualTo("organizationId",user.getOrganizationId())
                        .andEqualTo("inspectionItemCode",inspectionItemCodeSmall);
                BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectOneByExample(example6);
                if (StringUtils.isEmpty(baseInspectionItem)) {
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setInspectionItemId(baseInspectionItem.getInspectionItemId());
            }else if(StringUtils.isNotEmpty(inspectionItemCodeBig)){
                criteria6.andEqualTo("organizationId",user.getOrganizationId())
                        .andEqualTo("inspectionItemCode",inspectionItemCodeBig);
                BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectOneByExample(example6);
                if (StringUtils.isEmpty(baseInspectionItem)) {
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setInspectionItemId(baseInspectionItem.getInspectionItemId());
            }

            //????????????
            if(StringUtils.isNotEmpty(sampleProcessCode)){
                Example example7 = new Example(BaseSampleProcess.class);
                Example.Criteria criteria7 = example7.createCriteria();
                criteria7.andEqualTo("orgId",user.getOrganizationId())
                        .andEqualTo("sampleProcessCode",sampleProcessCode);
                BaseSampleProcess baseSampleProcess = baseSampleProcessMapper.selectOneByExample(example7);
                if (StringUtils.isEmpty(baseSampleProcess)) {
                    fail.add(i+4);
                    continue;
                }
                baseInspectionStandardImport.setSampleProcessId(baseSampleProcess.getSampleProcessId());
            }

            inspectionStandardImports.add(baseInspectionStandardImport);
        }

        if(StringUtils.isNotEmpty(inspectionStandardImports)){
            //???????????????????????????
            HashMap<String, List<BaseInspectionStandardImport>> map = inspectionStandardImports.stream().collect(Collectors.groupingBy(BaseInspectionStandardImport::getInspectionStandardCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<BaseInspectionStandardImport> baseInspectionStandardImports1 = map.get(code);
                BaseInspectionStandard baseInspectionStandard = new BaseInspectionStandard();
                //??????????????????????????????
                BeanUtils.copyProperties(baseInspectionStandardImports1.get(0), baseInspectionStandard);
                baseInspectionStandard.setCreateTime(new Date());
                baseInspectionStandard.setCreateUserId(user.getUserId());
                baseInspectionStandard.setModifiedUserId(user.getUserId());
                baseInspectionStandard.setModifiedTime(new Date());
                baseInspectionStandard.setOrgId(user.getOrganizationId());
                baseInspectionStandard.setStatus((byte)1);
                baseInspectionStandard.setInspectionType(StringUtils.isEmpty(baseInspectionStandardImports1.get(0).getInspectionType())?null:baseInspectionStandardImports1.get(0).getInspectionType().byteValue());
                success += baseInspectionStandardMapper.insertUseGeneratedKeys(baseInspectionStandard);

                //??????
                BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
                BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
                htList.add(baseHtInspectionStandard);

                //??????????????????????????????
                LinkedList<BaseInspectionStandardDet> detList = new LinkedList<>();
                for (BaseInspectionStandardImport baseInspectionStandardImport : baseInspectionStandardImports1) {
                    BaseInspectionStandardDet baseInspectionStandardDet = new BaseInspectionStandardDet();
                    BeanUtils.copyProperties(baseInspectionStandardImport, baseInspectionStandardDet);
                    baseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
                    baseInspectionStandardDet.setStatus((byte) 1);
                    baseInspectionStandardDet.setIfMustInspection(StringUtils.isEmpty(baseInspectionStandardImport.getIfMustInspection()) ? null : baseInspectionStandardImport.getIfMustInspection().byteValue());
                    baseInspectionStandardDet.setInspectionTag(StringUtils.isEmpty(baseInspectionStandardImport.getInspectionTag()) ? null : baseInspectionStandardImport.getInspectionTag().byteValue());
                    detList.add(baseInspectionStandardDet);
                }
                baseInspectionStandardDetMapper.insertList(detList);
            }
            baseHtInspectionStandardMapper.insertList(htList);
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }
}
