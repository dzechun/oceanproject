package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtFactoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtHtFactoryMapper;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
@Slf4j
public class SmtFactoryServiceImpl implements SmtFactoryService {
    @Autowired
    private SmtFactoryMapper smtFactoryMapper;
    @Autowired
    private SmtHtFactoryMapper smtHtFactoryMapper;
    @Override
    public List<SmtFactoryDto> findList(Map<String, Object> map) {
        return smtFactoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtFactory smtFactory) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        Example example = new Example(SmtFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("factoryCode", smtFactory.getFactoryCode());

        SmtFactory odlsmtFactory = smtFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)){
            //return ConstantUtils.SYS_CODE_REPEAT;
        }
        smtFactory.setCreateUserId(user.getUserId());
        smtFactory.setCreateTime(new Date());
        smtFactory.setModifiedUserId(user.getUserId());
        smtFactory.setModifiedTime(new Date());
        smtFactory.setStatus(StringUtils.isEmpty(smtFactory.getStatus())?1:smtFactory.getStatus());


        SmtHtFactory smtHtFactory  = new SmtHtFactory();
        BeanUtils.copyProperties(smtFactory,smtHtFactory);
        smtHtFactoryMapper.insert(smtHtFactory);

        return smtFactoryMapper.insert(smtFactory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        SmtFactory smtFactory = smtFactoryMapper.selectByPrimaryKey(id);

        if(StringUtils.isNotEmpty(smtFactory)){
            SmtHtFactory smtHtFactory  = new SmtHtFactory();
            BeanUtils.copyProperties(smtFactory,smtHtFactory);

            smtHtFactory.setModifiedTime(new Date());
            smtHtFactory.setModifiedUserId(user.getUserId());
            smtHtFactory.setCreateTime(new Date());
            smtHtFactory.setCreateUserId(user.getUserId());

            smtHtFactoryMapper.insert(smtHtFactory);
        }
        return smtFactoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> smtFactoryIds) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        List<SmtHtFactory>  smtHtFactorys = new LinkedList<>();
        for(String id : smtFactoryIds){
            SmtFactory smtFactory = smtFactoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isNotEmpty(smtFactory)){
                SmtHtFactory smtHtFactory  = new SmtHtFactory();
                BeanUtils.copyProperties(smtFactory,smtHtFactory);

                smtHtFactory.setModifiedTime(new Date());
                smtHtFactory.setModifiedUserId(user.getUserId());
                smtHtFactory.setCreateTime(new Date());
                smtHtFactory.setCreateUserId(user.getUserId());
                smtHtFactorys.add(smtHtFactory);
            }
        }
        if(StringUtils.isNotEmpty(smtHtFactorys)){
            smtHtFactoryMapper.insertList(smtHtFactorys);
        }
        return smtFactoryMapper.delBatch(smtFactoryIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtFactory smtFactory) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        if(StringUtils.isEmpty(user)){
            //return   ConstantUtils.USER_TOKEN_ERROR;
        }
        Example example = new Example(SmtFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("factoryCode", smtFactory.getFactoryCode());

        SmtFactory odlsmtFactory = smtFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)&&!odlsmtFactory.getFactoryId().equals(smtFactory.getFactoryId())){
            //return ConstantUtils.SYS_CODE_REPEAT;
        }
        smtFactory.setModifiedTime(new Date());
        smtFactory.setModifiedUserId(user.getUserId());

        SmtHtFactory smtHtFactory  = new SmtHtFactory();
        BeanUtils.copyProperties(smtFactory,smtHtFactory);
        smtHtFactory.setCreateUserId(user.getUserId());
        smtHtFactory.setCreateTime(new Date());

        smtHtFactoryMapper.insert(smtHtFactory);

        return smtFactoryMapper.updateByPrimaryKeySelective(smtFactory);
    }

    @Override
    public SmtFactory findById(String id) {
        return smtFactoryMapper.selectByPrimaryKey(id);
    }
}
