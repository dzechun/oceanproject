package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkShopMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtWorkShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
public class SmtWorkShopServiceImpl extends BaseService<SmtWorkShop> implements SmtWorkShopService {

    @Autowired
    private SmtWorkShopMapper smtWorkShopMapper;
    @Autowired
    private SmtHtWorkShopMapper smtHtWorkShopMapper;
    @Override
    public List<SmtWorkShopDto> findList(Map<String, Object> map) {
        return smtWorkShopMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtWorkShop smtWorkShop) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());

        SmtWorkShop odlSmtWorkShop = smtWorkShopMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlSmtWorkShop)){
            //return ConstantUtils.SYS_CODE_REPEAT;
        }
        smtWorkShop.setCreateUserId(user.getUserId());
        smtWorkShop.setCreateTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setStatus(StringUtils.isEmpty(smtWorkShop.getStatus())?1:smtWorkShop.getStatus());


        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
        smtHtWorkShopMapper.insert(smtHtWorkShop);

        return smtWorkShopMapper.insertSelective(smtWorkShop);
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
        SmtWorkShop smtWorkShop = smtWorkShopMapper.selectByPrimaryKey(id);

        if(StringUtils.isNotEmpty(smtWorkShop)){
            SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
            BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);

            smtHtWorkShop.setModifiedTime(new Date());
            smtHtWorkShop.setModifiedUserId(user.getUserId());
            smtHtWorkShop.setCreateTime(new Date());
            smtHtWorkShop.setCreateUserId(user.getUserId());

            smtHtWorkShopMapper.insert(smtHtWorkShop);
        }
        return smtWorkShopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<String> workShopIds) {
        int i=0;
        for(String id :workShopIds){
           i+= deleteById(id);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtWorkShop smtWorkShop) {
        SysUser user = null;
        try {
            user = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(user)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());

        SmtWorkShop odlsmtWorkShop = smtWorkShopMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtWorkShop)&&!odlsmtWorkShop.getWorkShopId().equals(smtWorkShop.getWorkShopId())){
            //return ConstantUtils.SYS_CODE_REPEAT;
        }
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());

        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
        smtHtWorkShop.setCreateUserId(user.getUserId());
        smtHtWorkShop.setCreateTime(new Date());

        smtHtWorkShopMapper.insert(smtHtWorkShop);

        return smtWorkShopMapper.updateByPrimaryKeySelective(smtWorkShop);
    }

    @Override
    public SmtWorkShop findById(String id) {
        return smtWorkShopMapper.selectByPrimaryKey(id);
    }
}
