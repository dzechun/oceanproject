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
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProductModel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productModelCode",smtProductModel.getProductModelCode());
        List<SmtProductModel> smtProductModels = smtProductModelMapper.selectByExample(example);
        if(null!=smtProductModels&&smtProductModels.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
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
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (Long  productModelId : productModelIds) {
            SmtProductModel smtProductModel = smtProductModelMapper.selectByPrimaryKey(productModelId);
            if(StringUtils.isEmpty(smtProductModel)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            //新增产品型号历史信息
            SmtHtProductModel smtHtProductModel=new SmtHtProductModel();
            BeanUtils.copyProperties(smtProductModel,smtHtProductModel);
            smtHtProductModel.setModifiedUserId(currentUser.getUserId());
            smtHtProductModel.setModifiedTime(new Date());
            list.add(smtHtProductModel);

            i+= smtProductModelMapper.deleteByPrimaryKey(productModelId);
        }

        if(StringUtils.isNotEmpty(list)){
            smtHtProductModelMapper.insertList(list);
        }

        return i;
    }

    @Override
    public SmtProductModel selectByKey(Long id) {
        return smtProductModelMapper.selectByPrimaryKey(id);
    }

}
