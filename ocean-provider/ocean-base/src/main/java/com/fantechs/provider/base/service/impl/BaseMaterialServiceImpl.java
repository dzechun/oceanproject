package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseMaterialService;
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
    private BcmFeignApi bcmFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseTabMapper baseTabMapper;
    @Resource
    private BaseUnitPriceMapper baseUnitPriceMapper;

    @Override
    public List<BaseMaterialDto> findList(Map<String, Object> map){
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseMaterial baseMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", baseMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(baseMaterial.getVersion())) {
            criteria.andEqualTo("version", baseMaterial.getVersion());
        }
        List<BaseMaterial> baseMaterials = baseMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterial.setCreateUserId(currentUser.getUserId());
        baseMaterial.setCreateTime(new Date());
        baseMaterial.setModifiedUserId(currentUser.getUserId());
        baseMaterial.setModifiedTime(new Date());
        int i = baseMaterialMapper.insertUseGeneratedKeys(baseMaterial);

        //新增物料页签信息
        BaseTab baseTab = baseMaterial.getBaseTabDto();
        if (0 >= baseTab.getTransferQuantity()){
            throw new BizErrorException("转移批量必须大于0");
        }
        baseTab.setMaterialId(baseMaterial.getMaterialId());
        baseTabMapper.insertSelective(baseTab);

        //新增物料历史信息
        BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
        BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
        baseHtMaterialMapper.insertSelective(baseHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseMaterial baseMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", baseMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(baseMaterial.getVersion())) {
            criteria.andEqualTo("version", baseMaterial.getVersion());
        }
        BaseMaterial material = baseMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(material) && !material.getMaterialId().equals(baseMaterial.getMaterialId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterial.setModifiedUserId(currentUser.getUserId());
        baseMaterial.setModifiedTime(new Date());
        int i = baseMaterialMapper.updateByPrimaryKeySelective(baseMaterial);

        BaseTab baseTab = baseMaterial.getBaseTabDto();
        if (StringUtils.isNotEmpty(baseTab)){
            if (StringUtils.isEmpty(baseTab.getTransferQuantity())){
                throw new BizErrorException("转移批量不能为空");
            }
            if (0 >= baseTab.getTransferQuantity()){
                throw new BizErrorException("转移批量必须大于0");
            }
            //判断该物料的页签是否存在
            Example example1 = new Example(BaseTab.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId",baseMaterial.getMaterialId());
            BaseTab baseTab1 = baseTabMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseTab1)){
                //新增页签
                baseTabMapper.insertSelective(baseTab);
            }else {
                baseTabMapper.updateByPrimaryKeySelective(baseTab);
            }

        }

        //新增物料历史信息
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
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseTab> baseTabs = new ArrayList<>();
        SearchBaseTab searchBaseTab = new SearchBaseTab();
        String[] idsArr = ids.split(",");
        for (String materialId : idsArr) {
            BaseMaterial baseMaterial = baseMaterialMapper.selectByPrimaryKey(materialId);
            if (StringUtils.isEmpty(baseMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            //被物料特征码引用
            Example example = new Example(BaseSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId", baseMaterial.getMaterialId());
            List<BaseSignature> baseSignatures = baseSignatureMapper.selectByExample(example);

            //被产品工艺路线引用
            Example example1 = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId", materialId);
            List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example1);

            //被产品BOM引用
            Example example2 = new Example(BaseProductBom.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("materialId", materialId);
            List<BaseProductBom> baseProductBoms = baseProductBomMapper.selectByExample(example2);

            //被产品BOM详细引用
            Example example3 = new Example(BaseProductBomDet.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("partMaterialId", materialId);
            List<BaseProductBomDet> baseProductBomDets = baseProductBomDetMapper.selectByExample(example3);

            //被物料编码关联客户料号引用
            Example example4 = new Example(BaseMaterialSupplier.class);
            Example.Criteria criteria4 = example4.createCriteria();
            criteria4.andEqualTo("materialId", materialId);
            List<BaseMaterialSupplier> baseMaterialSuppliers = baseMaterialSupplierMapper.selectByExample(example4);

            //被单价信息引用
            Example example5 = new Example(BaseUnitPrice.class);
            Example.Criteria criteria5 = example5.createCriteria();
            criteria5.andEqualTo("materialId",materialId);
            List<BaseUnitPrice> baseUnitPrices = baseUnitPriceMapper.selectByExample(example5);


            if (StringUtils.isNotEmpty(baseSignatures) || StringUtils.isNotEmpty(baseProductProcessRoutes)
                    || StringUtils.isNotEmpty(baseProductBoms) || StringUtils.isNotEmpty(baseProductBomDets)
                    || StringUtils.isNotEmpty(baseMaterialSuppliers) || StringUtils.isNotEmpty(baseUnitPrices)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //删除物料页签对应的履历
            Example example6 = new Example(BaseTab.class);
            Example.Criteria criteria6 = example6.createCriteria();
            criteria6.andEqualTo("materialId",materialId);
            baseTabMapper.deleteByExample(example6);

            //新增物料历史信息
            BaseHtMaterial baseHtMaterial = new BaseHtMaterial();
            BeanUtils.copyProperties(baseMaterial, baseHtMaterial);
            baseHtMaterial.setModifiedUserId(currentUser.getUserId());
            baseHtMaterial.setModifiedTime(new Date());
            list.add(baseHtMaterial);
        }

        i = baseMaterialMapper.deleteByIds(ids);
        //新增物料履历
        baseHtMaterialMapper.insertList(list);
        return i;
    }

    @Override
    public int batchUpdateByCode(List<BaseMaterial> baseMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(baseMaterials)) {
            for (BaseMaterial baseMaterial : baseMaterials) {
                baseMaterial.setModifiedTime(new Date());

                //更新页签
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

        List<BaseMaterial> baseMaterialAddList = new ArrayList<>();//物料新增集合
        List<BaseMaterial> baseMaterialUpdateList = new ArrayList<>();//物料更新集合

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
    public int batchSave(List<BaseMaterial> baseMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(baseMaterials)) {
            for (BaseMaterial baseMaterial : baseMaterials) {

                baseMaterial.setCreateTime(new Date());
                baseMaterial.setModifiedTime(new Date());

                //新增物料页签信息
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
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseHtMaterial> htList = new LinkedList<>();
        LinkedList<BaseTab> baseTabs = new LinkedList<>();

        ArrayList<BaseMaterialImport> materialImports = new ArrayList<>();


        for (int i = 0; i < baseMaterialImports.size(); i++) {
            BaseMaterialImport baseMaterialImport = baseMaterialImports.get(i);

            String materialCode = baseMaterialImport.getMaterialCode();
            String labelCode = baseMaterialImport.getLabelCode();//标签代码
            String supplierCode = baseMaterialImport.getSupplierCode();//供应商代码
            String inspectionItemCode = baseMaterialImport.getInspectionItemCode();//检验项目单号
            String inspectionTypeCode = baseMaterialImport.getInspectionTypeCode();//检验类型编码
            String labelCategoryCode = baseMaterialImport.getLabelCategoryCode();//标签类别编码
            String packageSpecificationCode = baseMaterialImport.getPackageSpecificationCode();//包装规格编码
            String productModelCode = baseMaterialImport.getProductModelCode();//产品型号编码
            String barcodeRuleSetCode = baseMaterialImport.getBarcodeRuleSetCode();//条码规则集合编码
            Integer transferQuantity = baseMaterialImport.getTransferQuantity();//转移批量

            if (StringUtils.isEmpty(
                    materialCode,transferQuantity
            ) || transferQuantity <= 0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialCode",materialCode);
            if (StringUtils.isNotEmpty(baseMaterialMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //标签编码不为空判断标签信息是否存在
            if (StringUtils.isNotEmpty(labelCode)){
                SearchBaseLabel searchBaseLabel = new SearchBaseLabel();
                searchBaseLabel.setCodeQueryMark(1);
                searchBaseLabel.setLabelCode(labelCode);
                List<BaseLabelDto> baseLabelDtos = bcmFeignApi.findLabelList(searchBaseLabel).getData();
                if (StringUtils.isEmpty(baseLabelDtos)){
                    fail.add(i+4);
                    continue;
                }

            }

            //供应商编码不为空则判断供应商信息是否存在
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example1 = new Example(BaseSupplier.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("supplierCode",supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setSupplierId(baseSupplier.getSupplierId());
            }

            //检验项目单号不为空判断检验项目信息是否存在
            if (StringUtils.isNotEmpty(inspectionItemCode)){
                SearchQmsInspectionItem searchQmsInspectionItem = new SearchQmsInspectionItem();
                searchQmsInspectionItem.setInspectionItemCode(inspectionItemCode);
                searchQmsInspectionItem.setCodeQueryMark((byte) 1);
                List<QmsInspectionItemDto> qmsInspectionItemDtos = qmsFeignApi.findInspectionItemList(searchQmsInspectionItem).getData();
                if (StringUtils.isEmpty(qmsInspectionItemDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setInspectionItemId(qmsInspectionItemDtos.get(0).getInspectionItemId());
            }

            //检验类型编码不为空则判断检查类型信息是否存在
            if (StringUtils.isNotEmpty(inspectionTypeCode)){
                SearchQmsInspectionType searchQmsInspectionType = new SearchQmsInspectionType();
                searchQmsInspectionType.setCodeQueryMark((byte) 1);
                searchQmsInspectionType.setInspectionTypeCode(inspectionTypeCode);
                List<QmsInspectionTypeDto> qmsInspectionTypeDtos = qmsFeignApi.findInspectionTypeList(searchQmsInspectionType).getData();
                if (StringUtils.isEmpty(qmsInspectionTypeDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setInspectionTypeId(qmsInspectionTypeDtos.get(0).getInspectionTypeId());
            }

            //标签类别编码不为空则判断标签类别信息是否存在
            if (StringUtils.isNotEmpty(labelCategoryCode)){
                SearchBaseLabelCategory searchBaseLabelCategory = new SearchBaseLabelCategory();
                searchBaseLabelCategory.setCodeQueryMark(1);
                searchBaseLabelCategory.setLabelCategoryCode(labelCategoryCode);
                List<BaseLabelCategoryDto> baseLabelCategoryDtos = bcmFeignApi.findLabelCategoryList(searchBaseLabelCategory).getData();
                if (StringUtils.isEmpty(baseLabelCategoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setLabelId(baseLabelCategoryDtos.get(0).getLabelCategoryId());
            }

            //包装规格编码不为空则判断包装规格信息是否存在
            if (StringUtils.isNotEmpty(packageSpecificationCode)){
                Example example1 = new Example(BasePackageSpecification.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("packageSpecificationCode",packageSpecificationCode);
                BasePackageSpecification basePackageSpecification = basePackageSpecificationMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(basePackageSpecification)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setPackageSpecificationId(basePackageSpecification.getPackageSpecificationId());
            }

            //产品型号编码不为空则判断产品型号不存在
            if (StringUtils.isNotEmpty(productModelCode)){
                Example example1 = new Example(BaseProductModel.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelCode",productModelCode);
                BaseProductModel baseProductModel = baseProductModelMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseProductModel)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setProductModelId(baseProductModel.getProductModelId());
            }

            //条码规则集合编码不为空则判断条码规则集合信息是否存在
            if (StringUtils.isNotEmpty(barcodeRuleSetCode)){
                SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet = new SearchSmtBarcodeRuleSet();
                searchSmtBarcodeRuleSet.setCodeQueryMark(1);
                searchSmtBarcodeRuleSet.setBarcodeRuleSetCode(barcodeRuleSetCode);
                List<SmtBarcodeRuleSetDto> smtBarcodeRuleSetDtos = pmFeignApi.findBarcodeRuleSetList(searchSmtBarcodeRuleSet).getData();
                if (StringUtils.isEmpty(smtBarcodeRuleSetDtos)){
                    fail.add(i+4);
                    continue;
                }
                baseMaterialImport.setBarcodeRuleSetId(smtBarcodeRuleSetDtos.get(0).getBarcodeRuleSetId());
            }

            //判断集合中是否存在重复数据
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
            }

            baseHtMaterialMapper.insertList(htList);
            baseTabMapper.insertList(baseTabs);
        }


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
