package com.fantechs.provider.base.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseMaterialService;
import com.fantechs.provider.base.service.WanbaoBarcodeRultDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BaseMaterialServiceImpl extends BaseService<BaseMaterial> implements BaseMaterialService {

    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseHtMaterialMapper baseHtMaterialMapper;
    @Resource
    private BaseSignatureMapper baseSignatureMapper;
    @Resource
    private BaseProductProcessRouteMapper baseProductProcessRouteMapper;
    @Resource
    private BaseProductBomMapper baseProductBomMapper;
    @Resource
    private BaseProductBomDetMapper baseProductBomDetMapper;
    @Resource
    private BaseMaterialSupplierMapper baseMaterialSupplierMapper;
    @Resource
    private BaseSupplierMapper baseSupplierMapper;
    @Resource
    private BasePackageSpecificationMapper basePackageSpecificationMapper;
    @Resource
    private BaseProductModelMapper baseProductModelMapper;
    @Resource
    private BaseTabMapper baseTabMapper;
    @Resource
    private BaseUnitPriceMapper baseUnitPriceMapper;
    @Resource
    private BaseLabelMapper baseLabelMapper;
    @Resource
    private BaseLabelCategoryMapper baseLabelCategoryMapper;
    @Resource
    private BaseBarcodeRuleSetMapper baseBarcodeRuleSetMapper;
    @Resource
    private BaseInspectionItemMapper baseInspectionItemMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private WanbaoBarcodeRultDataService wanbaoBarcodeRultDataService;

    @Override
    public List<BaseMaterialDto> findList(Map<String, Object> map){
        if(StringUtils.isEmpty(map.get("organizationId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("organizationId", user.getOrganizationId());
        }

        //?????????????????????????????????
        String ifUseSpecMaterialCategory = StringUtils.isEmpty(map.get("ifUseSpecMaterialCategory"))?"":map.get("ifUseSpecMaterialCategory").toString();
        if ("1".equals(ifUseSpecMaterialCategory)) {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("SpecMaterialCategoryCode");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isNotEmpty(sysSpecItemList)){
                SysSpecItem sysSpecItem = sysSpecItemList.get(0);
                String paraValue = sysSpecItem.getParaValue();
                List<String> materialCategoryCodes = Arrays.asList(paraValue.split(","));
                map.put("materialCategoryCodes",materialCategoryCodes);
            }
        }

        List<BaseMaterialDto> smtMaterialDtos = baseMaterialMapper.findList(map);
        if (StringUtils.isNotEmpty(smtMaterialDtos)) {
            for (BaseMaterialDto smtMaterialDto : smtMaterialDtos) {
                SearchBaseTab searchBaseTab = new SearchBaseTab();
                searchBaseTab.setMaterialId(smtMaterialDto.getMaterialId());
                List<BaseTabDto> baseTabs = baseTabMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTab));
                if (StringUtils.isNotEmpty(baseTabs)) {
                    BaseTabDto baseTabDto = baseTabs.get(0);
                    smtMaterialDto.setBaseTabDto(baseTabDto);
                }
            }
        }


        /*if (map.containsKey("propertyQueryMark")){
            if (map.get("propertyQueryMark").equals(1)){
                Iterator<SmtMaterialDto> iterator = smtMaterialDtos.iterator();
                while (iterator.hasNext()){
                    SmtMaterialDto smtMaterialDto = iterator.next();
                    BaseTab baseTab = smtMaterialDto.getBaseTab();
                    if (StringUtils.isNotEmpty(baseTab)){
                        if (!(baseTab.getMaterialProperty() != null && (baseTab.getMaterialProperty() == 0 || baseTab.getMaterialProperty() == 1))){
                            iterator.remove();
                        }
                    }else {
                        iterator.remove();
                    }
                }

            }
        }*/
        return smtMaterialDtos;
    }

    /**
     * ???????????????-????????????????????????
     * @param map
     * @return
     */
    @Override
    public List<BaseMaterial> findListByInitInventory(Map<String, Object> map) {
        Example example = new Example(BaseMaterial.class);
        example.createCriteria().orEqualTo("materialCode", map.get("materialCode"))
                .orEqualTo("option1", map.get("option1"));
        List<BaseMaterial> materials = baseMaterialMapper.selectByExample(example);
        return materials;
    }

    @Override
    public List<BaseMaterialDto> findAll(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("organizationId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("organizationId", user.getOrganizationId());
        }
        return baseMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseMaterial baseMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("materialCode", baseMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(baseMaterial.getMaterialVersion())) {
            criteria.andEqualTo("materialVersion", baseMaterial.getMaterialVersion());
        }
        List<BaseMaterial> baseMaterials = baseMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterial.setCreateUserId(currentUser.getUserId());
        baseMaterial.setCreateTime(new Date());
        baseMaterial.setModifiedUserId(currentUser.getUserId());
        baseMaterial.setModifiedTime(new Date());
        baseMaterial.setOrganizationId(currentUser.getOrganizationId());
        int i = baseMaterialMapper.insertUseGeneratedKeys(baseMaterial);

        //????????????????????????
        BaseTab baseTab = baseMaterial.getBaseTabDto();
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (specItems.isEmpty()){
            if (0 >= baseTab.getTransferQuantity()){
                throw new BizErrorException("????????????????????????0");
            }
            if (StringUtils.isNotEmpty(baseTab.getNetWeight(),baseTab.getGrossWeight())
                    &&baseTab.getNetWeight().compareTo(baseTab.getGrossWeight())==1){
                throw new BizErrorException("????????????????????????");
            }
        }
        baseTab.setMaterialId(baseMaterial.getMaterialId());
        baseTabMapper.insertSelective(baseTab);

        //????????????????????????
        BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
        BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
        baseHtMaterialMapper.insertSelective(baseHtMaterial);

        // ??????????????????????????????????????????
        List<Long> list = new ArrayList<>();
        list.add(baseMaterial.getMaterialId());
        wanbaoBarcodeRultDataService.updateByMaterial(list);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseMaterial baseMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("materialCode", baseMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(baseMaterial.getMaterialVersion())) {
            criteria.andEqualTo("materialVersion", baseMaterial.getMaterialVersion());
        }
        BaseMaterial material = baseMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(material) && !material.getMaterialId().equals(baseMaterial.getMaterialId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterial.setModifiedUserId(currentUser.getUserId());
        baseMaterial.setModifiedTime(new Date());
        baseMaterial.setOrganizationId(currentUser.getOrganizationId());
        int i = baseMaterialMapper.updateByPrimaryKeySelective(baseMaterial);

        BaseTab baseTab = baseMaterial.getBaseTabDto();
        if (StringUtils.isNotEmpty(baseTab)){
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoSyncData");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (specItems.isEmpty()){
                if (StringUtils.isEmpty(baseTab.getTransferQuantity())){
                    throw new BizErrorException("????????????????????????");
                }
                if (0 >= baseTab.getTransferQuantity()){
                    throw new BizErrorException("????????????????????????0");
                }
                if (StringUtils.isNotEmpty(baseTab.getNetWeight(),baseTab.getGrossWeight())
                        &&baseTab.getNetWeight().compareTo(baseTab.getGrossWeight())==1){
                    throw new BizErrorException("????????????????????????");
                }
            }
            //????????????????????????????????????
            Example example1 = new Example(BaseTab.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId",baseMaterial.getMaterialId());
            BaseTab baseTab1 = baseTabMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseTab1)){
                //????????????
                baseTabMapper.insertSelective(baseTab);
            }else {
                baseTabMapper.updateByPrimaryKeySelective(baseTab);
            }

        }

        //????????????????????????
        BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
        BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
        baseHtMaterialMapper.insertSelective(baseHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtMaterial> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseTab> baseTabs = new ArrayList<>();
        SearchBaseTab searchBaseTab = new SearchBaseTab();
        String[] idsArr = ids.split(",");
        for (String materialId : idsArr) {
            BaseMaterial baseMaterial = baseMaterialMapper.selectByPrimaryKey(materialId);
            if (StringUtils.isEmpty(baseMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            //????????????????????????
            Example example = new Example(BaseSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("materialId", baseMaterial.getMaterialId());
            List<BaseSignature> baseSignatures = baseSignatureMapper.selectByExample(example);

            //???????????????????????????
            Example example1 = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("materialId", materialId);
            List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example1);

            //?????????BOM??????
            Example example2 = new Example(BaseProductBom.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria2.andEqualTo("materialId", materialId);
            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example2);

            //?????????BOM????????????
            Example example3 = new Example(BaseProductBomDet.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria3.andEqualTo("materialId", materialId);
            List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.selectByExample(example3);

            //???????????????????????????????????????
            Example example4 = new Example(BaseMaterialSupplier.class);
            Example.Criteria criteria4 = example4.createCriteria();
            criteria4.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria4.andEqualTo("materialId", materialId);
            List<BaseMaterialSupplier> baseMaterialSuppliers = baseMaterialSupplierMapper.selectByExample(example4);

            //?????????????????????
            Example example5 = new Example(BaseUnitPrice.class);
            Example.Criteria criteria5 = example5.createCriteria();
            criteria5.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria5.andEqualTo("materialId",materialId);
            List<BaseUnitPrice> baseUnitPrices = baseUnitPriceMapper.selectByExample(example5);


            if (StringUtils.isNotEmpty(baseSignatures) || StringUtils.isNotEmpty(baseProductProcessRoutes)
                    || StringUtils.isNotEmpty(baseProductBoms) || StringUtils.isNotEmpty(baseProductBomDets)
                    || StringUtils.isNotEmpty(baseMaterialSuppliers) || StringUtils.isNotEmpty(baseUnitPrices)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //?????????????????????????????????
            Example example6 = new Example(BaseTab.class);
            Example.Criteria criteria6 = example6.createCriteria();
            criteria6.andEqualTo("materialId",materialId);
            baseTabMapper.deleteByExample(example6);

            //????????????????????????
            BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
            BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
            baseHtMaterial.setModifiedUserId(currentUser.getUserId());
            baseHtMaterial.setModifiedTime(new Date());
            list.add(baseHtMaterial);
        }

        i = baseMaterialMapper.deleteByIds(ids);
        //??????????????????
        baseHtMaterialMapper.insertList(list);
        return i;
    }

    @Override
    public int batchUpdateByCode(List<BaseMaterial> baseMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(baseMaterials)) {
            for (BaseMaterial baseMaterial : baseMaterials) {
                baseMaterial.setModifiedTime(new Date());

                //????????????
                BaseTab baseTab = baseMaterial.getBaseTabDto();
                if (StringUtils.isNotEmpty(baseTab)) {
                    baseTabMapper.updateByPrimaryKeySelective(baseTab);
                }
            }
            i = baseMaterialMapper.batchUpdateByCode(baseMaterials);


        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<BaseMaterial> baseMaterials) {

        List<BaseMaterial> baseMaterialAddList = new ArrayList<>();//??????????????????
        List<BaseMaterial> baseMaterialUpdateList = new ArrayList<>();//??????????????????

        int i = 0;
        for (BaseMaterial baseMaterial : baseMaterials) {
            if (StringUtils.isEmpty(baseMaterial.getMaterialCode())) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }

            Map<String,Object> map = new HashMap();
            map.put("materialCode", baseMaterial.getMaterialCode());
            map.put("codeQueryMark",1);
            List<BaseMaterialDto> returnList = baseMaterialMapper.findList(map);
            if (StringUtils.isEmpty(returnList)) {
                baseMaterialAddList.add(baseMaterial);
            } else {
                baseMaterialUpdateList.add(baseMaterial);
            }

            if(baseMaterialAddList.size() == 1000){
                i = batchSave(baseMaterialAddList);
                baseMaterialAddList.clear();
            }
            if(baseMaterialUpdateList.size() == 1000){
                i = batchUpdateByCode(baseMaterialUpdateList);
                baseMaterialUpdateList.clear();
            }
        }

        if(baseMaterialAddList.size() > 0){
            i = batchSave(baseMaterialAddList);
        }
        if(baseMaterialUpdateList.size() > 0){
            i = batchUpdateByCode(baseMaterialUpdateList);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int batchSave(List<BaseMaterial> baseMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(baseMaterials)) {
            for (BaseMaterial baseMaterial : baseMaterials) {

                baseMaterial.setCreateTime(new Date());
                baseMaterial.setModifiedTime(new Date());

                //????????????????????????
                BaseTab baseTab = baseMaterial.getBaseTabDto();
                if (StringUtils.isNotEmpty(baseTab)) {
                    baseTab.setMaterialId(baseMaterial.getMaterialId());
                    baseTabMapper.insertSelective(baseTab);
                }
            }
            i = baseMaterialMapper.insertList(baseMaterials);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseMaterialImport> baseMaterialImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        LinkedList<BaseHtMaterial> htList = new LinkedList<>();
        LinkedList<BaseTab> baseTabs = new LinkedList<>();

        ArrayList<BaseMaterialImport> materialImports = new ArrayList<>();


        for (int i = 0; i < baseMaterialImports.size(); i++) {
            BaseMaterialImport baseMaterialImport = baseMaterialImports.get(i);

            String materialCode = baseMaterialImport.getMaterialCode();
            String materialName = baseMaterialImport.getMaterialName();
            String labelCode = baseMaterialImport.getLabelCode();//????????????
            String supplierCode = baseMaterialImport.getSupplierCode();//???????????????
            String inspectionItemCode = baseMaterialImport.getInspectionItemCode();//??????????????????
            String inspectionTypeCode = baseMaterialImport.getInspectionTypeCode();//??????????????????
            String labelCategoryCode = baseMaterialImport.getLabelCategoryCode();//??????????????????
            String packageSpecificationCode = baseMaterialImport.getPackageSpecificationCode();//??????????????????
            String productModelCode = baseMaterialImport.getProductModelCode();//??????????????????
            String barcodeRuleSetCode = baseMaterialImport.getBarcodeRuleSetCode();//????????????????????????
            Integer transferQuantity = baseMaterialImport.getTransferQuantity();//????????????
            String voltage = baseMaterialImport.getVoltage();

            if (StringUtils.isEmpty(
                materialCode,materialName,voltage,productModelCode
            )){
                fail.add(i+4);
                continue;
            }

            //????????????????????????
            Example example = new Example(BaseMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("materialCode",materialCode);
            if (StringUtils.isNotEmpty(baseMaterialMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //???????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(labelCode)){
                SearchBaseLabel searchBaseLabel = new SearchBaseLabel();
                searchBaseLabel.setCodeQueryMark(1);
                searchBaseLabel.setLabelCode(labelCode);
                List<BaseLabelDto> baseLabelDtos = baseLabelMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabel));
                if (StringUtils.isEmpty(baseLabelDtos)){
                    fail.add(i+4);
                    continue;
                }

            }

            //????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example1 = new Example(BaseSupplier.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria1.andEqualTo("supplierCode",supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setSupplierId(baseSupplier.getSupplierId());
            }

            //?????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(inspectionItemCode)){
                Example example4 = new Example(BaseInspectionItem.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("inspectionItemCode",inspectionItemCode)
                        .andEqualTo("organizationId",currentUser.getOrganizationId());
                BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectOneByExample(example4);
                if (StringUtils.isEmpty(baseInspectionItem)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setInspectionItemId(baseInspectionItem.getInspectionItemId());
            }

            //??????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(labelCategoryCode)){
                SearchBaseLabelCategory searchBaseLabelCategory = new SearchBaseLabelCategory();
                searchBaseLabelCategory.setCodeQueryMark(1);
                searchBaseLabelCategory.setLabelCategoryCode(labelCategoryCode);
                List<BaseLabelCategoryDto> baseLabelCategoryDtos = baseLabelCategoryMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelCategory));
                if (StringUtils.isEmpty(baseLabelCategoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setLabelId(baseLabelCategoryDtos.get(0).getLabelCategoryId());
            }

            //??????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(packageSpecificationCode)){
                Example example2 = new Example(BasePackageSpecification.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria2.andEqualTo("packageSpecificationCode",packageSpecificationCode);
                BasePackageSpecification basePackageSpecification = basePackageSpecificationMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(basePackageSpecification)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setPackageSpecificationId(basePackageSpecification.getPackageSpecificationId());
            }

            //?????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(productModelCode)){
                Example example3 = new Example(BaseProductModel.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria3.andEqualTo("productModelCode", productModelCode);
                criteria3.andEqualTo("productModelDesc", materialCode);
                List<BaseProductModel> baseProductModels = baseProductModelMapper.selectByExample(example3);
                BaseProductModel baseProductModel = new BaseProductModel();
                if (StringUtils.isEmpty(baseProductModels)){
                    baseProductModel.setProductModelCode(productModelCode);
                    baseProductModel.setProductModelName(productModelCode);
                    baseProductModel.setProductModelDesc(materialCode);
                    baseProductModel.setCreateTime(new Date());
                    baseProductModel.setModifiedTime(new Date());
                    baseProductModel.setCreateUserId(currentUser.getUserId());
                    baseProductModel.setModifiedUserId(currentUser.getUserId());
                    baseProductModel.setIsDelete((byte) 1);
                    baseProductModel.setStatus(1);
                    baseProductModel.setOrganizationId(currentUser.getOrganizationId());
                    baseProductModelMapper.insertUseGeneratedKeys(baseProductModel);
                }else {
                    baseProductModel = baseProductModels.get(0);
                }
                baseMaterialImport.setProductModelId(baseProductModel.getProductModelId());
            }

            //??????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(barcodeRuleSetCode)){
                SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet = new SearchBaseBarcodeRuleSet();
                searchBaseBarcodeRuleSet.setCodeQueryMark(1);
                searchBaseBarcodeRuleSet.setBarcodeRuleSetCode(barcodeRuleSetCode);
                List<BaseBarcodeRuleSetDto> baseBarcodeRuleSetDtos = baseBarcodeRuleSetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSet));
                if (StringUtils.isEmpty(baseBarcodeRuleSetDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setBarcodeRuleSetId(baseBarcodeRuleSetDtos.get(0).getBarcodeRuleSetId());
            }

            //???????????????????????????????????????
            boolean tag = false;
            if (StringUtils.isNotEmpty(materialImports)){
                for (BaseMaterialImport materialImport : materialImports) {
                    if (materialImport.getMaterialCode().equals(materialCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            materialImports.add(baseMaterialImport);
        }

        if (StringUtils.isNotEmpty(materialImports)){
            List<Long> addMaterialIds = new ArrayList<>();
            for (BaseMaterialImport materialImport : materialImports) {
                BaseMaterial baseMaterial = new BaseMaterial();
                BeanUtils.copyProperties(materialImport, baseMaterial);
                baseMaterial.setCreateTime(new Date());
                baseMaterial.setCreateUserId(currentUser.getUserId());
                baseMaterial.setModifiedTime(new Date());
                baseMaterial.setMaterialId(currentUser.getUserId());
                baseMaterial.setOrganizationId(currentUser.getOrganizationId());
                baseMaterial.setStatus((byte) 1);

                success += baseMaterialMapper.insertUseGeneratedKeys(baseMaterial);

                BaseTab baseTab = new BaseTab();
                BeanUtils.copyProperties(materialImport,baseTab);
                baseTab.setMaterialId(baseMaterial.getMaterialId());
                baseTabs.add(baseTab);

                BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
                BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
                htList.add(baseHtMaterial);

                addMaterialIds.add(baseMaterial.getMaterialId());
            }

            baseHtMaterialMapper.insertList(htList);
            baseTabMapper.insertList(baseTabs);

            // ??????????????????????????????????????????
            if (!addMaterialIds.isEmpty()){
                wanbaoBarcodeRultDataService.updateByMaterial(addMaterialIds);
            }
        }


        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMaterial saveApi(BaseMaterial baseMaterial) {
        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", baseMaterial.getOrganizationId());
        criteria.andEqualTo("materialCode", baseMaterial.getMaterialCode());

        BaseMaterial baseMaterialExist=baseMaterialMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(baseMaterialExist)){
            baseMaterial.setCreateTime(new Date());
            baseMaterial.setCreateUserId((long) 1);
            baseMaterial.setModifiedUserId((long) 1);
            baseMaterial.setModifiedTime(new Date());
            baseMaterial.setIsDelete((byte) 1);

            baseMaterialMapper.insertUseGeneratedKeys(baseMaterial);

            //???????????? ?????????Option2????????????
            if(StringUtils.isNotEmpty(baseMaterial.getOption2())) {
                BaseTab baseTab = new BaseTab();
                baseTab.setMaterialId(baseMaterial.getMaterialId());
                baseTab.setMainUnit(baseMaterial.getOption2());
                baseTabMapper.insertSelective(baseTab);
            }
        }
        else {
            baseMaterial.setMaterialId(baseMaterialExist.getMaterialId());
            baseMaterial.setModifiedTime(new Date());
            baseMaterialMapper.updateByPrimaryKeySelective(baseMaterial);
        }

//        baseMaterialMapper.deleteByExample(example);
//        baseMaterial.setOrganizationId(baseMaterial.getOrganizationId());
//        int i = baseMaterialMapper.insertUseGeneratedKeys(baseMaterial);

        //????????????????????????
        BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
        BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
        baseHtMaterialMapper.insertSelective(baseHtMaterial);
        return baseMaterial;
    }


    @Override
    public  Map<String, Long> findIdByCode(List<String> materialCodes) {
        List<Map<String, Long>> list = baseMaterialMapper.findIdByCode(materialCodes);
        Map<String, Long> map = new HashMap<>();
        for(Map<String, Long> maps : list){
            map.put(String.valueOf( maps.get("material_code")),maps.get("material_id"));
        }
        return map;
    }

}
