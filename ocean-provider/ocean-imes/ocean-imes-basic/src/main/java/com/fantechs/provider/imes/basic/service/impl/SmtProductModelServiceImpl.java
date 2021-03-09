package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.imports.SmtProductModelImport;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductFamily;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductModelMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductModelMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtProductModelService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Service
public class SmtProductModelServiceImpl extends BaseService<SmtProductModel> implements SmtProductModelService {

    @Resource
    private SmtProductModelMapper smtProductModelMapper;

    @Resource
    private SmtHtProductModelMapper smtHtProductModelMapper;

    @Resource
    private SmtMaterialMapper smtMaterialMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;

    @Override
    public List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel) {
        return smtProductModelMapper.selectProductModels(searchSmtProductModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProductModel smtProductModel) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode", smtProductModel.getProductModelCode());
        List<SmtProductModel> smtProductModels = smtProductModelMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProductModels)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtProductModel.setCreateUserId(currentUser.getUserId());
        smtProductModel.setCreateTime(new Date());
        smtProductModel.setModifiedUserId(currentUser.getUserId());
        smtProductModel.setModifiedTime(new Date());
        int i = smtProductModelMapper.insertUseGeneratedKeys(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel = new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel, smtHtProductModel);
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductModel smtProductModel) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode", smtProductModel.getProductModelCode());
        SmtProductModel productModel = smtProductModelMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(productModel) && !productModel.getProductModelId().equals(smtProductModel.getProductModelId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProductModel.setModifiedUserId(currentUser.getUserId());
        smtProductModel.setModifiedTime(new Date());
        int i = smtProductModelMapper.updateByPrimaryKeySelective(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel = new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel, smtHtProductModel);
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<SmtHtProductModel> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String productModelId : idsArr) {
            SmtProductModel smtProductModel = smtProductModelMapper.selectByPrimaryKey(productModelId);
            if (StringUtils.isEmpty(smtProductModel)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            //被物料引用
            SearchBaseTab searchBaseTab = new SearchBaseTab();
            searchBaseTab.setProductModelId(Long.valueOf(productModelId));
            List<BaseTabDto> baseTabs = baseFeignApi.findTabList(searchBaseTab).getData();
            if (StringUtils.isNotEmpty(baseTabs)) {
                BaseTab baseTab = baseTabs.get(0);
                if (StringUtils.isNotEmpty(baseTab)) {
                    throw new BizErrorException("数据被引用，无法删除");
                }

                //被产品工艺路线引用
                Example example1 = new Example(SmtProductProcessRoute.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelId", productModelId);
                List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example1);
                if (StringUtils.isNotEmpty(baseTab) || StringUtils.isNotEmpty(smtProductProcessRoutes)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012004);
                }
            }


            //新增产品型号历史信息
            SmtHtProductModel smtHtProductModel = new SmtHtProductModel();
            BeanUtils.copyProperties(smtProductModel, smtHtProductModel);
            smtHtProductModel.setModifiedUserId(currentUser.getUserId());
            smtHtProductModel.setModifiedTime(new Date());
            list.add(smtHtProductModel);

        }
        smtHtProductModelMapper.insertList(list);
        i = smtProductModelMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProductModelImport> smtProductModelImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProductModel> list = new LinkedList<>();
        LinkedList<SmtHtProductModel> htList = new LinkedList<>();
        ArrayList<SmtProductModelImport> smtProductModelImports1 = new ArrayList<>();

        for (int i = 0; i < smtProductModelImports.size(); i++) {
            SmtProductModelImport smtProductModelImport = smtProductModelImports.get(i);
            String productModelCode = smtProductModelImport.getProductModelCode();
            String productModelName = smtProductModelImport.getProductModelName();
            String productFamilyCode = smtProductModelImport.getProductFamilyCode();
            String organizationCode = smtProductModelImport.getOrganizationCode();
            if (StringUtils.isEmpty(
                    productModelCode, productModelName, productFamilyCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtProductModel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productModelCode", productModelCode);
            if (StringUtils.isNotEmpty(smtProductModelMapper.selectOneByExample(example))) {
                fail.add(i + 4);
                continue;
            }

            //判断产品族是否存在
            SearchBaseProductFamily searchBaseProductFamily = new SearchBaseProductFamily();
            searchBaseProductFamily.setProductFamilyCode(productFamilyCode);
            searchBaseProductFamily.setCodeQueryMark((byte) 1);
            BaseProductFamilyDto baseProductFamilyDto = baseFeignApi.findProductFamilyList(searchBaseProductFamily).getData().get(0);
            if (StringUtils.isEmpty(baseProductFamilyDto)) {
                fail.add(i + 4);
                continue;
            }
            smtProductModelImport.setProductFamilyId(baseProductFamilyDto.getProductFamilyId());

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(smtProductModelImports1)){
                for (SmtProductModelImport productModelImport : smtProductModelImports1) {
                    if (productModelImport.getProductModelCode().equals(productModelCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i + 4);
                continue;
            }

            smtProductModelImports1.add(smtProductModelImport);
        }
        if (StringUtils.isNotEmpty(smtProductModelImports1)){
            for (SmtProductModelImport smtProductModelImport : smtProductModelImports1) {
                SmtProductModel smtProductModel = new SmtProductModel();
                BeanUtils.copyProperties(smtProductModelImport,smtProductModel);
                smtProductModel.setCreateTime(new Date());
                smtProductModel.setCreateUserId(currentUser.getUserId());
                smtProductModel.setModifiedTime(new Date());
                smtProductModel.setModifiedUserId(currentUser.getUserId());
                list.add(smtProductModel);
            }

            success = smtProductModelMapper.insertList(list);

            for (SmtProductModel smtProductModel : list) {
                SmtHtProductModel smtHtProductModel = new SmtHtProductModel();
                BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
                htList.add(smtHtProductModel);
            }
            smtHtProductModelMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数", success);
        resutlMap.put("操作失败行数", fail);
        return resutlMap;
    }
}
