package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.entity.basic.BaseTab;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode",smtProductModel.getProductModelCode());
        List<SmtProductModel> smtProductModels = smtProductModelMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtProductModels)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtProductModel.setCreateUserId(currentUser.getUserId());
        smtProductModel.setCreateTime(new Date());
        smtProductModel.setModifiedUserId(currentUser.getUserId());
        smtProductModel.setModifiedTime(new Date());
        int i = smtProductModelMapper.insertUseGeneratedKeys(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductModel smtProductModel) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode",smtProductModel.getProductModelCode());
        SmtProductModel productModel = smtProductModelMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(productModel)&&!productModel.getProductModelId().equals(smtProductModel.getProductModelId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProductModel.setModifiedUserId(currentUser.getUserId());
        smtProductModel.setModifiedTime(new Date());
        int i= smtProductModelMapper.updateByPrimaryKeySelective(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtProductModel> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr =  ids.split(",");
        for (String productModelId : idsArr) {
            SmtProductModel smtProductModel = smtProductModelMapper.selectByPrimaryKey(productModelId);
            if(StringUtils.isEmpty(smtProductModel)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            //被物料引用
            SearchBaseTab searchBaseTab = new SearchBaseTab();
            searchBaseTab.setProductModelId(Long.valueOf(productModelId));
            List<BaseTabDto> baseTabs = baseFeignApi.findTabList(searchBaseTab).getData();
            if (StringUtils.isNotEmpty(baseTabs)){
                BaseTab baseTab = baseTabs.get(0);
                if (StringUtils.isNotEmpty(baseTab)){
                    throw new BizErrorException("数据被引用，无法删除");
                }

                //被产品工艺路线引用
                Example example1 = new Example(SmtProductProcessRoute.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelId",productModelId);
                List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example1);
                if(StringUtils.isNotEmpty(baseTab)||StringUtils.isNotEmpty(smtProductProcessRoutes)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012004);
                }
            }


            //新增产品型号历史信息
            SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
            BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
            smtHtProductModel.setModifiedUserId(currentUser.getUserId());
            smtHtProductModel.setModifiedTime(new Date());
            list.add(smtHtProductModel);

        }
            smtHtProductModelMapper.insertList(list);
           i=  smtProductModelMapper.deleteByIds(ids);
        return i;
    }

}
