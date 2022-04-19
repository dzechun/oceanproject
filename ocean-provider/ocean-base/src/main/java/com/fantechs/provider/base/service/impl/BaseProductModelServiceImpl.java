package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductModelImport;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductFamily;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProductModelService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BaseProductModelServiceImpl extends BaseService<BaseProductModel> implements BaseProductModelService {

    @Resource
    private BaseProductModelMapper baseProductModelMapper;
    @Resource
    private BaseHtProductModelMapper baseHtProductModelMapper;
    @Resource
    private BaseProductProcessRouteMapper baseProductProcessRouteMapper;
    @Resource
    private BaseTabMapper baseTabMapper;
    @Resource
    private BaseProductFamilyMapper baseProductFamilyMapper;

    @Override
    public List<BaseProductModel> selectProductModels(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (!StringUtils.isEmpty(user)) {
            map.put("orgId", user.getOrganizationId());
        }
        return baseProductModelMapper.selectProductModels(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductModel baseProductModel) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("productModelCode", baseProductModel.getProductModelCode());
        List<BaseProductModel> baseProductModels = baseProductModelMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseProductModels)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseProductModel.setCreateUserId(currentUser.getUserId());
        baseProductModel.setCreateTime(new Date());
        baseProductModel.setModifiedUserId(currentUser.getUserId());
        baseProductModel.setModifiedTime(new Date());
        baseProductModel.setOrganizationId(currentUser.getOrganizationId());
        int i = baseProductModelMapper.insertUseGeneratedKeys(baseProductModel);

        //新增产品型号历史信息
        BaseHtProductModel baseHtProductModel = new BaseHtProductModel();
        BeanUtils.copyProperties(baseProductModel, baseHtProductModel);
        baseHtProductModelMapper.insertSelective(baseHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductModel baseProductModel) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("productModelCode", baseProductModel.getProductModelCode());
        BaseProductModel productModel = baseProductModelMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(productModel) && !productModel.getProductModelId().equals(baseProductModel.getProductModelId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProductModel.setModifiedUserId(currentUser.getUserId());
        baseProductModel.setModifiedTime(new Date());
        baseProductModel.setOrganizationId(currentUser.getOrganizationId());
        int i = baseProductModelMapper.updateByPrimaryKeySelective(baseProductModel);

        //新增产品型号历史信息
        BaseHtProductModel baseHtProductModel = new BaseHtProductModel();
        BeanUtils.copyProperties(baseProductModel, baseHtProductModel);
        baseHtProductModelMapper.insertSelective(baseHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtProductModel> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String productModelId : idsArr) {
            BaseProductModel baseProductModel = baseProductModelMapper.selectByPrimaryKey(productModelId);

            //被物料引用
            SearchBaseTab searchBaseTab = new SearchBaseTab();
            searchBaseTab.setProductModelId(Long.valueOf(productModelId));
            List<BaseTabDto> baseTabs = baseTabMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTab));
            if (StringUtils.isNotEmpty(baseTabs)) {
                BaseTab baseTab = baseTabs.get(0);
                if (StringUtils.isNotEmpty(baseTab)) {
                    throw new BizErrorException("数据被引用，无法删除");
                }

                //被产品工艺路线引用
                Example example1 = new Example(BaseProductProcessRoute.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelId", productModelId);
                List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example1);
                if (StringUtils.isNotEmpty(baseTab) || StringUtils.isNotEmpty(baseProductProcessRoutes)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012004);
                }
            }


            //新增产品型号历史信息
            BaseHtProductModel baseHtProductModel = new BaseHtProductModel();
            BeanUtils.copyProperties(baseProductModel, baseHtProductModel);
            baseHtProductModel.setModifiedUserId(currentUser.getUserId());
            baseHtProductModel.setModifiedTime(new Date());
            list.add(baseHtProductModel);

        }
        baseHtProductModelMapper.insertList(list);
        i = baseProductModelMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProductModelImport> baseProductModelImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProductModel> list = new LinkedList<>();
        LinkedList<BaseHtProductModel> htList = new LinkedList<>();
        ArrayList<BaseProductModelImport> baseProductModelImports1 = new ArrayList<>();

        for (int i = 0; i < baseProductModelImports.size(); i++) {
            BaseProductModelImport baseProductModelImport = baseProductModelImports.get(i);
            String productModelCode = baseProductModelImport.getProductModelCode();
            String productModelName = baseProductModelImport.getProductModelName();
            if (StringUtils.isEmpty(
                    productModelCode, productModelName
            )) {
                fail.add(i + 4);
                continue;
            }

            baseProductModelImports1.add(baseProductModelImport);
        }
        if (StringUtils.isNotEmpty(baseProductModelImports1)){
            for (BaseProductModelImport baseProductModelImport : baseProductModelImports1) {
                BaseProductModel baseProductModel = new BaseProductModel();
                BeanUtils.copyProperties(baseProductModelImport, baseProductModel);
                baseProductModel.setCreateTime(new Date());
                baseProductModel.setCreateUserId(currentUser.getUserId());
                baseProductModel.setModifiedTime(new Date());
                baseProductModel.setModifiedUserId(currentUser.getUserId());
                baseProductModel.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseProductModel);
            }

            success = baseProductModelMapper.insertList(list);

            for (BaseProductModel baseProductModel : list) {
                BaseHtProductModel baseHtProductModel = new BaseHtProductModel();
                BeanUtils.copyProperties(baseProductModel, baseHtProductModel);
                htList.add(baseHtProductModel);
            }
            baseHtProductModelMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数", success);
        resutlMap.put("操作失败行数", fail);
        return resutlMap;
    }

    @Override
    public BaseProductModel addForReturn(BaseProductModel productModel) {
        baseProductModelMapper.insertUseGeneratedKeys(productModel);
        return productModel;
    }
}
