package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtSpecItem;
import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;
import com.fantechs.common.base.entity.basic.search.SearchSmtSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtSpecItemMapper;
import com.fantechs.provider.imes.basic.mapper.SmtSpecItemMapper;
import com.fantechs.provider.imes.basic.service.SmtSpecItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmtSpecItemServiceImpl extends BaseService<SmtSpecItem> implements SmtSpecItemService {

    @Resource
    private SmtSpecItemMapper smtSpecItemMapper;

    @Resource
    private SmtHtSpecItemMapper smtHtSpecItemMapper;

    @Override
    public List<SmtSpecItem> selectSpecItems(SearchSmtSpecItem searchSmtSpecItem) {
        return smtSpecItemMapper.selectSpecItems(searchSmtSpecItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtSpecItem smtSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",smtSpecItem.getSpecCode());
        List<SmtSpecItem> smtSpecItems = smtSpecItemMapper.selectByExample(example);
        if(null!=smtSpecItems&&smtSpecItems.size()>0){
            return ErrorCodeEnum.OPT20012001.getCode();
        }

        smtSpecItem.setCreateUserId(currentUser.getUserId());
        smtSpecItem.setCreateTime(new Date());
        smtSpecItemMapper.insertUseGeneratedKeys(smtSpecItem);

        //新增配置项历史信息
        SmtHtSpecItem smtHtSpecItem=new SmtHtSpecItem();
        BeanUtils.copyProperties(smtSpecItem,smtHtSpecItem);
        int i = smtHtSpecItemMapper.insertSelective(smtHtSpecItem);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtSpecItem smtSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",smtSpecItem.getSpecCode());
        SmtSpecItem specItem = smtSpecItemMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(smtSpecItem)&&!specItem.getSpecId().equals(smtSpecItem.getSpecId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtSpecItem.setModifiedUserId(currentUser.getUserId());
        smtSpecItem.setModifiedTime(new Date());
        int i = smtSpecItemMapper.updateByPrimaryKeySelective(smtSpecItem);

        SmtHtSpecItem smtHtSpecItem=new SmtHtSpecItem();
        BeanUtils.copyProperties(smtSpecItem,smtHtSpecItem);
        smtHtSpecItem.setModifiedUserId(currentUser.getUserId());
        smtHtSpecItem.setModifiedTime(new Date());
        smtHtSpecItemMapper.insertSelective(smtHtSpecItem);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> specIds) {
        int i=0;
        List<SmtHtSpecItem> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (String specId : specIds) {
            SmtSpecItem smtSpecItem = smtSpecItemMapper.selectByPrimaryKey(specId);
            if(StringUtils.isEmpty(smtSpecItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtSpecItem smtHtSpecItem=new SmtHtSpecItem();
            BeanUtils.copyProperties(smtSpecItem,smtHtSpecItem);
            smtHtSpecItem.setModifiedUserId(currentUser.getUserId());
            smtHtSpecItem.setModifiedTime(new Date());
            list.add(smtHtSpecItem);
            smtSpecItemMapper.deleteByPrimaryKey(specId);

        }
        i=smtHtSpecItemMapper.insertList(list);
        return i;
    }

}
