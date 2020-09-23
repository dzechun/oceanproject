package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductModelMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductModelMapper;
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

    @Override
    public List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel) {
        return smtProductModelMapper.selectProductModels(searchSmtProductModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtProductModel smtProductModel) {
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode",smtProductModel.getProductModelCode());
        List<SmtProductModel> smtProductModels = smtProductModelMapper.selectByExample(example);
        if(null!=smtProductModels&&smtProductModels.size()>0){
            return ErrorCodeEnum.OPT20012001.getCode();
        }
        smtProductModel.setCreateUserId(currentUser.getUserId());
        smtProductModel.setCreateTime(new Date());
        int i = smtProductModelMapper.insertUseGeneratedKeys(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtProductModel smtProductModel) {
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        smtProductModel.setModifiedUserId(currentUser.getUserId());
        smtProductModel.setModifiedTime(new Date());
        int i= smtProductModelMapper.updateByPrimaryKeySelective(smtProductModel);

        //新增产品型号历史信息
        SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
        BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
        //smtHtProductModel.setHtProductModelId(UUIDUtils.getUUID());
        smtHtProductModel.setModifiedUserId(currentUser.getUserId());
        smtHtProductModel.setModifiedTime(new Date());
        smtHtProductModelMapper.insertSelective(smtHtProductModel);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> productModelIds) {
        int i=0;
        List<SmtHtProductModel> list=new ArrayList<>();
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        for (Long  productModelId : productModelIds) {
            SmtProductModel smtProductModel = smtProductModelMapper.selectByPrimaryKey(productModelId);
            if(StringUtils.isEmpty(smtProductModel)){
                //throw new BizErrorException("该产品型号已被删除。");
                return ErrorCodeEnum.OPT20012003.getCode();
            }
            //新增产品型号历史信息
            SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
            BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
            smtHtProductModel.setModifiedUserId(currentUser.getUserId());
            smtHtProductModel.setModifiedTime(new Date());
            list.add(smtHtProductModel);

            smtProductModelMapper.deleteByPrimaryKey(productModelId);
        }

       //i=smtHtProductModelMapper.addBatchHtProductModel(list);
        i=smtHtProductModelMapper.insertList(list);
        return i;
    }

    @Override
    public List<SmtProductModel> exportProductModels(SearchSmtProductModel searchSmtProductModel) {
        List<SmtProductModel> smtProductModels = this.selectProductModels(searchSmtProductModel);
        return smtProductModels;
    }

}
