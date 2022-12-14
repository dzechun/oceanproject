package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackageSpecificationImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialPackage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BasePackageSpecificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Slf4j
@Service
public class BasePackageSpecificationServiceImpl extends BaseService<BasePackageSpecification> implements BasePackageSpecificationService {

    @Resource
    private BasePackageSpecificationMapper basePackageSpecificationMapper;
    @Resource
    private BaseHtPackageSpecificationMapper baseHtPackageSpecificationMapper;
    @Resource
    private BaseMaterialPackageMapper baseMaterialPackageMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;
    @Resource
    private BasePackingUnitMapper basePackingUnitMapper;
    @Resource
    private BaseBarcodeRuleMapper baseBarcodeRuleMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BasePackageSpecification basePackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("packageSpecificationCode", basePackageSpecification.getPackageSpecificationCode());
        List<BasePackageSpecification> basePackageSpecifications = basePackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(basePackageSpecifications)) {
            throw new BizErrorException("???????????????????????????");
        }

        basePackageSpecification.setCreateUserId(user.getUserId());
        basePackageSpecification.setCreateTime(new Date());
        basePackageSpecification.setModifiedUserId(user.getUserId());
        basePackageSpecification.setModifiedTime(new Date());
        basePackageSpecification.setOrganizationId(user.getOrganizationId());

        //??????????????????
        basePackageSpecificationMapper.insertUseGeneratedKeys(basePackageSpecification);

        //?????????????????????????????????
        List<BaseMaterialPackageDto> baseMaterialPackages = basePackageSpecification.getBaseMaterialPackages();
        if (StringUtils.isNotEmpty(baseMaterialPackages)){
            for (BaseMaterialPackage baseMaterialPackage : baseMaterialPackages) {
                if (StringUtils.isNotEmpty(baseMaterialPackage.getProcessId())){
                    Example example1 = new Example(BaseMaterialPackage.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("materialId", baseMaterialPackage.getMaterialId())
                            .andEqualTo("processId", baseMaterialPackage.getProcessId());
                    List<BaseMaterialPackage> baseMaterialPackages1 = baseMaterialPackageMapper.selectByExample(example1);
                    if (StringUtils.isNotEmpty(baseMaterialPackages1)){
                        throw new BizErrorException("??????????????????????????????????????????");
                    }
                }
                baseMaterialPackage.setPackageSpecificationId(basePackageSpecification.getPackageSpecificationId());
            }
        }
        if (StringUtils.isNotEmpty(baseMaterialPackages)){
            baseMaterialPackageMapper.insertList(baseMaterialPackages);
        }

        BaseHtPackageSpecification baseHtPackageSpecification = new BaseHtPackageSpecification();
        BeanUtils.copyProperties(basePackageSpecification, baseHtPackageSpecification);
        return baseHtPackageSpecificationMapper.insertSelective(baseHtPackageSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BasePackageSpecification basePackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("packageSpecificationCode", basePackageSpecification.getPackageSpecificationCode())
        .andNotEqualTo("packageSpecificationId", basePackageSpecification.getPackageSpecificationId());
        List<BasePackageSpecification> basePackageSpecifications = basePackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(basePackageSpecifications)) {
            throw new BizErrorException("???????????????????????????");
        }

        basePackageSpecification.setModifiedUserId(user.getUserId());
        basePackageSpecification.setModifiedTime(new Date());
        basePackageSpecification.setOrganizationId(user.getOrganizationId());

        BaseHtPackageSpecification baseHtPackageSpecification = new BaseHtPackageSpecification();
        BeanUtils.copyProperties(basePackageSpecification, baseHtPackageSpecification);
        baseHtPackageSpecificationMapper.insertSelective(baseHtPackageSpecification);

        //????????????????????????
        Example example1 = new Example(BaseMaterialPackage.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("packageSpecificationId", basePackageSpecification.getPackageSpecificationId());
        baseMaterialPackageMapper.deleteByExample(example1);

        //??????????????????
        List<BaseMaterialPackageDto> baseMaterialPackages = basePackageSpecification.getBaseMaterialPackages();
        if (StringUtils.isNotEmpty(baseMaterialPackages)){
            for (BaseMaterialPackage baseMaterialPackage : baseMaterialPackages) {
            if (StringUtils.isNotEmpty(baseMaterialPackage.getProcessId())){
                Example example2 = new Example(BaseMaterialPackage.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("materialId", baseMaterialPackage.getMaterialId())
                        .andEqualTo("processId", baseMaterialPackage.getProcessId())
                        .andNotEqualTo("packageSpecificationId", basePackageSpecification.getPackageSpecificationId());
                List<BaseMaterialPackage> baseMaterialPackages1 = baseMaterialPackageMapper.selectByExample(example2);
                if (StringUtils.isNotEmpty(baseMaterialPackages1)){
                    throw new BizErrorException("??????????????????????????????????????????");
                }
            }
            baseMaterialPackage.setPackageSpecificationId(basePackageSpecification.getPackageSpecificationId());
            }
        }
        if (StringUtils.isNotEmpty(baseMaterialPackages)){
            baseMaterialPackageMapper.insertList(baseMaterialPackages);
        }
        return basePackageSpecificationMapper.updateByPrimaryKeySelective(basePackageSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<BaseHtPackageSpecification> smtPackageSpecifications = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BasePackageSpecification basePackageSpecification = basePackageSpecificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePackageSpecification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtPackageSpecification baseHtPackageSpecification = new BaseHtPackageSpecification();
            BeanUtils.copyProperties(basePackageSpecification, baseHtPackageSpecification);
            baseHtPackageSpecification.setModifiedTime(new Date());
            baseHtPackageSpecification.setModifiedUserId(user.getUserId());
            smtPackageSpecifications.add(baseHtPackageSpecification);

            //????????????????????????????????????
            Example example = new Example(BaseMaterialPackage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packageSpecificationId", baseHtPackageSpecification.getPackageSpecificationId());
            baseMaterialPackageMapper.deleteByExample(example);
        }
        baseHtPackageSpecificationMapper.insertList(smtPackageSpecifications);
        return basePackageSpecificationMapper.deleteByIds(ids);
    }

    @Override
    public List<BasePackageSpecificationDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BasePackageSpecificationDto> basePackageSpecificationDtos = basePackageSpecificationMapper.findList(map);
        SearchBaseMaterialPackage searchBaseMaterialPackage = new SearchBaseMaterialPackage();

        for (BasePackageSpecificationDto basePackageSpecificationDto : basePackageSpecificationDtos) {
            searchBaseMaterialPackage.setPackageSpecificationId(basePackageSpecificationDto.getPackageSpecificationId());
            List<BaseMaterialPackageDto> baseMaterialPackageDtos = baseMaterialPackageMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialPackage));
            if (StringUtils.isNotEmpty(baseMaterialPackageDtos)){
                basePackageSpecificationDto.setBaseMaterialPackages(baseMaterialPackageDtos);
            }
        }

        return basePackageSpecificationDtos;
    }

    @Override
    public List<BasePackageSpecificationDto> findByMaterialProcess(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BasePackageSpecificationDto> basePackageSpecificationDtos = basePackageSpecificationMapper.findByMaterialProcess(map);
        SearchBaseMaterialPackage searchBaseMaterialPackage = new SearchBaseMaterialPackage();

        for (BasePackageSpecificationDto basePackageSpecificationDto : basePackageSpecificationDtos) {
            searchBaseMaterialPackage.setPackageSpecificationId(basePackageSpecificationDto.getPackageSpecificationId());
            List<BaseMaterialPackageDto> baseMaterialPackageDtos = baseMaterialPackageMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialPackage));
            if (StringUtils.isNotEmpty(baseMaterialPackageDtos)){
                basePackageSpecificationDto.setBaseMaterialPackages(baseMaterialPackageDtos);
            }
        }

        return basePackageSpecificationDtos;
    }

    @Override
    public List<BasePackageSpecificationDto> findByMaterialProcessNotDet(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BasePackageSpecificationDto> basePackageSpecificationDtos = basePackageSpecificationMapper.findByMaterialProcess(map);
        SearchBaseMaterialPackage searchBaseMaterialPackage = new SearchBaseMaterialPackage();
        return basePackageSpecificationDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BasePackageSpecificationImport> basePackageSpecificationImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<BasePackageSpecification> list = new LinkedList<>();
        LinkedList<BaseHtPackageSpecification> htList = new LinkedList<>();
        LinkedList<BasePackageSpecificationImport> packageSpecificationImports = new LinkedList<>();

        for (int i = 0; i < basePackageSpecificationImports.size(); i++) {
            BasePackageSpecificationImport basePackageSpecificationImport = basePackageSpecificationImports.get(i);
            String packageSpecificationCode = basePackageSpecificationImport.getPackageSpecificationCode();
            String packageSpecificationName = basePackageSpecificationImport.getPackageSpecificationName();
            String materialCode = basePackageSpecificationImport.getMaterialCode();
            String processCode = basePackageSpecificationImport.getProcessCode();
            String barcodeRuleCode = basePackageSpecificationImport.getBarcodeRuleCode();
            String packingUnitName = basePackageSpecificationImport.getPackingUnitName();
            if (StringUtils.isEmpty(
                    packageSpecificationCode,packageSpecificationName,materialCode,processCode
            )){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
            Example example = new Example(BasePackageSpecification.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("packageSpecificationCode",packageSpecificationCode);
            if (StringUtils.isNotEmpty(basePackageSpecificationMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //??????????????????????????????????????????????????????
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            basePackageSpecificationImport.setMaterialId(baseMaterial.getMaterialId());

            //???????????????????????????????????????????????????????????????
            Example example2 = new Example(BaseProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria2.andEqualTo("processCode",processCode);
            BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseProcess)){
                fail.add(i+4);
                continue;
            }
            basePackageSpecificationImport.setProcessId(baseProcess.getProcessId());

            //???????????????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(barcodeRuleCode)){
                SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
                searchBaseBarcodeRule.setBarcodeRuleCode(barcodeRuleCode);
                searchBaseBarcodeRule.setCodeQueryMark((byte) 1);
                List<BaseBarcodeRuleDto> baseBarcodeRuleDtos = baseBarcodeRuleMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
                if (StringUtils.isEmpty(baseBarcodeRuleDtos)){
                    fail.add(i+4);
                    continue;
                }
                basePackageSpecificationImport.setBarcodeRuleId(baseBarcodeRuleDtos.get(0).getBarcodeRuleId());
            }

            //???????????????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(packingUnitName)){
                Example example3 = new Example(BasePackingUnit.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria3.andEqualTo("packingUnitName",packingUnitName);
                BasePackingUnit basePackingUnit = basePackingUnitMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(basePackingUnit)){
                    fail.add(i+4);
                    continue;
                }
                basePackageSpecificationImport.setPackingUnitId(basePackingUnit.getPackingUnitId());
            }

            packageSpecificationImports.add(basePackageSpecificationImport);
        }

        if (StringUtils.isNotEmpty(packageSpecificationImports)){
            //???????????????????????????
            Map<String, List<BasePackageSpecificationImport>> map = packageSpecificationImports.stream().collect(Collectors.groupingBy(BasePackageSpecificationImport::getPackageSpecificationCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<BasePackageSpecificationImport> PackageSpecificationImports1 = map.get(code);
                //??????????????????????????????
                BasePackageSpecification basePackageSpecification = new BasePackageSpecification();
                BeanUtils.copyProperties(PackageSpecificationImports1.get(0), basePackageSpecification);
                basePackageSpecification.setCreateTime(new Date());
                basePackageSpecification.setCreateUserId(currentUser.getUserId());
                basePackageSpecification.setModifiedUserId(currentUser.getUserId());
                basePackageSpecification.setModifiedTime(new Date());
                basePackageSpecification.setOrganizationId(currentUser.getOrganizationId());
                basePackageSpecification.setStatus((byte) 1);
                success += basePackageSpecificationMapper.insertUseGeneratedKeys(basePackageSpecification);

                BaseHtPackageSpecification baseHtPackageSpecification = new BaseHtPackageSpecification();
                BeanUtils.copyProperties(basePackageSpecification, baseHtPackageSpecification);
                baseHtPackageSpecification.setModifiedTime(new Date());
                baseHtPackageSpecification.setModifiedUserId(currentUser.getUserId());
                htList.add(baseHtPackageSpecification);

                //??????????????????????????????
                LinkedList<BaseMaterialPackage> baseMaterialPackages = new LinkedList<>();
                for (BasePackageSpecificationImport basePackageSpecificationImport : PackageSpecificationImports1) {
                    BaseMaterialPackage baseMaterialPackage = new BaseMaterialPackage();
                    BeanUtils.copyProperties(basePackageSpecificationImport, baseMaterialPackage);
                    baseMaterialPackage.setPackageSpecificationId(basePackageSpecification.getPackageSpecificationId());
                    baseMaterialPackage.setStatus((byte) 1);
                    baseMaterialPackages.add(baseMaterialPackage);
                }
                baseMaterialPackageMapper.insertList(baseMaterialPackages);
            }
            baseHtPackageSpecificationMapper.insertList(htList);
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }
}
