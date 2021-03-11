package com.fantechs.provider.imes.basic.service.impl;


import cn.afterturn.easypoi.cache.manager.IFileLoader;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialDto;
import com.fantechs.common.base.dto.basic.imports.SmtMaterialImport;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseUnitPrice;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtMaterialService;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmtMaterialServiceImpl extends BaseService<SmtMaterial> implements SmtMaterialService {

    @Resource
    private SmtMaterialMapper smtMaterialMapper;
    @Resource
    private SmtHtMaterialMapper smtHtMaterialMapper;
    @Resource
    private SmtSignatureMapper smtSignatureMapper;
    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;
    @Resource
    private SmtProductBomMapper smtProductBomMapper;
    @Resource
    private SmtProductBomDetMapper smtProductBomDetMapper;
    @Resource
    private SmtMaterialSupplierMapper smtMaterialSupplierMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SmtSupplierMapper smtSupplierMapper;
    @Resource
    private SmtPackageSpecificationMapper smtPackageSpecificationMapper;
    @Resource
    private SmtProductModelMapper smtProductModelMapper;
    @Resource
    private BcmFeignApi bcmFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public List<SmtMaterialDto> findList(Map<String, Object> map){
        List<SmtMaterialDto> smtMaterialDtos = smtMaterialMapper.findList(map);
        if (StringUtils.isNotEmpty(smtMaterialDtos)) {
            for (SmtMaterialDto smtMaterialDto : smtMaterialDtos) {
                SearchBaseTab searchBaseTab = new SearchBaseTab();
                searchBaseTab.setMaterialId(smtMaterialDto.getMaterialId());
                List<BaseTabDto> baseTabs = baseFeignApi.findTabList(searchBaseTab).getData();
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
    public int save(SmtMaterial smtMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", smtMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(smtMaterial.getVersion())) {
            criteria.andEqualTo("version", smtMaterial.getVersion());
        }
        List<SmtMaterial> smtMaterials = smtMaterialMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtMaterials)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtMaterial.setCreateUserId(currentUser.getUserId());
        smtMaterial.setCreateTime(new Date());
        smtMaterial.setModifiedUserId(currentUser.getUserId());
        smtMaterial.setModifiedTime(new Date());
        int i = smtMaterialMapper.insertUseGeneratedKeys(smtMaterial);

        //新增物料页签信息
        BaseTab baseTab = smtMaterial.getBaseTabDto();
        if (0 >= baseTab.getTransferQuantity()){
            throw new BizErrorException("转移批量必须大于0");
        }
        baseTab.setMaterialId(smtMaterial.getMaterialId());
        baseFeignApi.addTab(baseTab);

        //新增物料历史信息
        SmtHtMaterial smtHtMaterial = new SmtHtMaterial();
        BeanUtils.copyProperties(smtMaterial, smtHtMaterial);
        smtHtMaterialMapper.insertSelective(smtHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtMaterial smtMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", smtMaterial.getMaterialCode());
        if (StringUtils.isNotEmpty(smtMaterial.getVersion())) {
            criteria.andEqualTo("version", smtMaterial.getVersion());
        }
        SmtMaterial material = smtMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(material) && !material.getMaterialId().equals(smtMaterial.getMaterialId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtMaterial.setModifiedUserId(currentUser.getUserId());
        smtMaterial.setModifiedTime(new Date());
        int i = smtMaterialMapper.updateByPrimaryKeySelective(smtMaterial);

        BaseTab baseTab = smtMaterial.getBaseTabDto();
        if (StringUtils.isNotEmpty(baseTab)){
            //判断该物料的页签是否存在
            SearchBaseTab searchBaseTab = new SearchBaseTab();
            searchBaseTab.setMaterialId(smtMaterial.getMaterialId());
            List<BaseTabDto> baseTabDtos = baseFeignApi.findTabList(searchBaseTab).getData();
            if (StringUtils.isEmpty(baseTabDtos)){
                //新增页签
                baseFeignApi.addTab(baseTab);
            }else {
                baseFeignApi.updateTab(baseTab);
            }

        }

        //新增物料历史信息
        SmtHtMaterial smtHtMaterial = new SmtHtMaterial();
        BeanUtils.copyProperties(smtMaterial, smtHtMaterial);
        smtHtMaterialMapper.insertSelective(smtHtMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<SmtHtMaterial> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseTab> baseTabs = new ArrayList<>();
        SearchBaseTab searchBaseTab = new SearchBaseTab();
        String[] idsArr = ids.split(",");
        for (String materialId : idsArr) {
            SmtMaterial smtMaterial = smtMaterialMapper.selectByPrimaryKey(materialId);
            if (StringUtils.isEmpty(smtMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            //被物料特征码引用
            Example example = new Example(SmtSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId", smtMaterial.getMaterialId());
            List<SmtSignature> smtSignatures = smtSignatureMapper.selectByExample(example);

            //被产品工艺路线引用
            Example example1 = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId", materialId);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example1);

            //被产品BOM引用
            Example example2 = new Example(SmtProductBom.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("materialId", materialId);
            List<SmtProductBom> smtProductBoms = smtProductBomMapper.selectByExample(example2);

            //被产品BOM详细引用
            Example example3 = new Example(SmtProductBomDet.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("partMaterialId", materialId);
            List<SmtProductBomDet> smtProductBomDets = smtProductBomDetMapper.selectByExample(example3);

            //被物料编码关联客户料号引用
            Example example4 = new Example(SmtMaterialSupplier.class);
            Example.Criteria criteria4 = example4.createCriteria();
            criteria4.andEqualTo("materialId", materialId);
            List<SmtMaterialSupplier> smtMaterialSuppliers = smtMaterialSupplierMapper.selectByExample(example4);

            //被单价信息引用
            SearchBaseUnitPrice searchBaseUnitPrice = new SearchBaseUnitPrice();
            searchBaseUnitPrice.setMaterialId(Long.valueOf(materialId));
            List<BaseUnitPriceDto> baseUnitPriceDtos = baseFeignApi.findUnitPriceList(searchBaseUnitPrice).getData();


            if (StringUtils.isNotEmpty(smtSignatures) || StringUtils.isNotEmpty(smtProductProcessRoutes)
                    || StringUtils.isNotEmpty(smtProductBoms) || StringUtils.isNotEmpty(smtProductBomDets)
                    || StringUtils.isNotEmpty(smtMaterialSuppliers) || StringUtils.isNotEmpty(baseUnitPriceDtos)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //查询该物料对应的页签
            searchBaseTab.setMaterialId(Long.valueOf(materialId));
            List<BaseTabDto> baseTabs1 = baseFeignApi.findTabList(searchBaseTab).getData();
            if (StringUtils.isNotEmpty(baseTabs1)) {
                baseTabs.add(baseTabs1.get(0));
            }
            //新增物料历史信息
            SmtHtMaterial smtHtMaterial = new SmtHtMaterial();
            BeanUtils.copyProperties(smtMaterial, smtHtMaterial);
            smtHtMaterial.setModifiedUserId(currentUser.getUserId());
            smtHtMaterial.setModifiedTime(new Date());
            list.add(smtHtMaterial);
        }
        //删除页签
        baseFeignApi.deleteTab(baseTabs);

        i = smtMaterialMapper.deleteByIds(ids);
        //新增物料履历
        smtHtMaterialMapper.insertList(list);
        return i;
    }

    @Override
    public int batchUpdateByCode(List<SmtMaterial> smtMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(smtMaterials)) {
            Example example = new Example(SmtMaterial.class);
            for (SmtMaterial smtMaterial : smtMaterials) {
                smtMaterial.setModifiedTime(new Date());

                //更新页签
                BaseTab baseTab = smtMaterial.getBaseTabDto();
                if (StringUtils.isNotEmpty(baseTab)) {
                    baseFeignApi.updateTab(baseTab);
                }
            }
            i = smtMaterialMapper.batchUpdateByCode(smtMaterials);


        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<SmtMaterial> smtMaterials) {

        List<SmtMaterial> smtMaterialAddList = new ArrayList<>();//物料新增集合
        List<SmtMaterial> smtMaterialUpdateList = new ArrayList<>();//物料更新集合

        int i = 0;
        for (SmtMaterial smtMaterial : smtMaterials) {
            if (StringUtils.isEmpty(smtMaterial.getMaterialCode())) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }

            Map<String,Object> map = new HashMap();
            map.put("materialCode",smtMaterial.getMaterialCode());
            map.put("codeQueryMark",1);
            List<SmtMaterialDto> returnList = smtMaterialMapper.findList(map);
            if (StringUtils.isEmpty(returnList)) {
                smtMaterialAddList.add(smtMaterial);
            } else {
                smtMaterialUpdateList.add(smtMaterial);
            }

            if(smtMaterialAddList.size() == 1000){
                i = batchSave(smtMaterialAddList);
                smtMaterialAddList.clear();
            }
            if(smtMaterialUpdateList.size() == 1000){
                i = batchUpdateByCode(smtMaterialUpdateList);
                smtMaterialUpdateList.clear();
            }
        }

        if(smtMaterialAddList.size() > 0){
            i = batchSave(smtMaterialAddList);
        }
        if(smtMaterialUpdateList.size() > 0){
            i = batchUpdateByCode(smtMaterialUpdateList);
        }
        return i;
    }

    @Override
    public int batchSave(List<SmtMaterial> smtMaterials) {
        int i = 0;
        if (StringUtils.isNotEmpty(smtMaterials)) {
            Example example = new Example(SmtMaterial.class);
            for (SmtMaterial smtMaterial : smtMaterials) {

                smtMaterial.setCreateTime(new Date());
                smtMaterial.setModifiedTime(new Date());

                //新增物料页签信息
                BaseTab baseTab = smtMaterial.getBaseTabDto();
                if (StringUtils.isNotEmpty(baseTab)) {
                    baseTab.setMaterialId(smtMaterial.getMaterialId());
                    baseFeignApi.addTab(baseTab);
                }
            }
            i = smtMaterialMapper.insertList(smtMaterials);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtMaterialImport> smtMaterialImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtHtMaterial> htList = new LinkedList<>();
        LinkedList<BaseTab> baseTabs = new LinkedList<>();

        ArrayList<SmtMaterialImport> materialImports = new ArrayList<>();


        for (int i = 0; i < smtMaterialImports.size(); i++) {
            SmtMaterialImport smtMaterialImport = smtMaterialImports.get(i);

            String materialCode = smtMaterialImport.getMaterialCode();
            String labelCode = smtMaterialImport.getLabelCode();//标签代码
            String supplierCode = smtMaterialImport.getSupplierCode();//供应商代码
            String inspectionItemCode = smtMaterialImport.getInspectionItemCode();//检验项目单号
            String inspectionTypeCode = smtMaterialImport.getInspectionTypeCode();//检验类型编码
            String labelCategoryCode = smtMaterialImport.getLabelCategoryCode();//标签类别编码
            String packageSpecificationCode = smtMaterialImport.getPackageSpecificationCode();//包装规格编码
            String productModelCode = smtMaterialImport.getProductModelCode();//产品型号编码
            String barcodeRuleSetCode = smtMaterialImport.getBarcodeRuleSetCode();//条码规则集合编码
            Integer transferQuantity = smtMaterialImport.getTransferQuantity();//转移批量

            if (StringUtils.isEmpty(
                    materialCode,transferQuantity
            ) || transferQuantity <= 0){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialCode",materialCode);
            if (StringUtils.isNotEmpty(smtMaterialMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //标签编码不为空判断标签信息是否存在
            if (StringUtils.isNotEmpty(labelCode)){
                SearchBcmLabel searchBcmLabel = new SearchBcmLabel();
                searchBcmLabel.setCodeQueryMark(1);
                searchBcmLabel.setLabelCode(labelCode);
                List<BcmLabelDto> bcmLabelDtos = bcmFeignApi.findLabelList(searchBcmLabel).getData();
                if (StringUtils.isEmpty(bcmLabelDtos)){
                    fail.add(i+4);
                    continue;
                }

            }

            //供应商编码不为空则判断供应商信息是否存在
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example1 = new Example(SmtSupplier.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("supplierCode",supplierCode);
                SmtSupplier smtSupplier = smtSupplierMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(smtSupplier)){
                    fail.add(i+4);
                    continue;
                }
                smtMaterialImport.setSupplierId(smtSupplier.getSupplierId());
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
                smtMaterialImport.setInspectionItemId(qmsInspectionItemDtos.get(0).getInspectionItemId());
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
                smtMaterialImport.setInspectionTypeId(qmsInspectionTypeDtos.get(0).getInspectionTypeId());
            }

            //标签类别编码不为空则判断标签类别信息是否存在
            if (StringUtils.isNotEmpty(labelCategoryCode)){
                SearchBcmLabelCategory searchBcmLabelCategory = new SearchBcmLabelCategory();
                searchBcmLabelCategory.setCodeQueryMark(1);
                searchBcmLabelCategory.setLabelCategoryCode(labelCategoryCode);
                List<BcmLabelCategoryDto> bcmLabelCategoryDtos = bcmFeignApi.findLabelCategoryList(searchBcmLabelCategory).getData();
                if (StringUtils.isEmpty(bcmLabelCategoryDtos)){
                    fail.add(i+4);
                    continue;
                }
                smtMaterialImport.setLabelId(bcmLabelCategoryDtos.get(0).getLabelCategoryId());
            }

            //包装规格编码不为空则判断包装规格信息是否存在
            if (StringUtils.isNotEmpty(packageSpecificationCode)){
                Example example1 = new Example(SmtPackageSpecification.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("packageSpecificationCode",packageSpecificationCode);
                SmtPackageSpecification smtPackageSpecification = smtPackageSpecificationMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(smtPackageSpecification)){
                    fail.add(i+4);
                    continue;
                }
                smtMaterialImport.setPackageSpecificationId(smtPackageSpecification.getPackageSpecificationId());
            }

            //产品型号编码不为空则判断产品型号不存在
            if (StringUtils.isNotEmpty(productModelCode)){
                Example example1 = new Example(SmtProductModel.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelCode",productModelCode);
                SmtProductModel smtProductModel = smtProductModelMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(smtProductModel)){
                    fail.add(i+4);
                    continue;
                }
                smtMaterialImport.setProductModelId(smtProductModel.getProductModelId());
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
                smtMaterialImport.setBarcodeRuleSetId(smtBarcodeRuleSetDtos.get(0).getBarcodeRuleSetId());
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(materialImports)){
                for (SmtMaterialImport materialImport : materialImports) {
                    if (materialImport.getMaterialCode().equals(materialCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            materialImports.add(smtMaterialImport);
        }

        if (StringUtils.isNotEmpty(materialImports)){
            for (SmtMaterialImport materialImport : materialImports) {
                SmtMaterial smtMaterial = new SmtMaterial();
                BeanUtils.copyProperties(materialImport,smtMaterial);
                smtMaterial.setCreateTime(new Date());
                smtMaterial.setCreateUserId(currentUser.getUserId());
                smtMaterial.setModifiedTime(new Date());
                smtMaterial.setMaterialId(currentUser.getUserId());
                smtMaterial.setOrganizationId(currentUser.getOrganizationId());
                smtMaterial.setStatus((byte) 1);

                success += smtMaterialMapper.insertUseGeneratedKeys(smtMaterial);

                BaseTab baseTab = new BaseTab();
                BeanUtils.copyProperties(materialImport,baseTab);
                baseTab.setMaterialId(smtMaterial.getMaterialId());
                baseTabs.add(baseTab);

                SmtHtMaterial smtHtMaterial = new SmtHtMaterial();
                BeanUtils.copyProperties(smtMaterial,smtHtMaterial);
                htList.add(smtHtMaterial);
            }

            smtHtMaterialMapper.insertList(htList);
            baseFeignApi.insertTabList(baseTabs);
        }


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
