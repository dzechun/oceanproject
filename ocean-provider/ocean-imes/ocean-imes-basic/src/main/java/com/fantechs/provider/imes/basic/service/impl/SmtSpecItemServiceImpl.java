package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtSpecItemExcelDTO;
import com.fantechs.common.base.entity.basic.SmtSpecItem;
import com.fantechs.common.base.entity.basic.history.SmtHtSpecItem;
import com.fantechs.common.base.entity.basic.search.SearchSmtSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
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
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        Example example = new Example(SmtSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",smtSpecItem.getSpecCode());
        List<SmtSpecItem> smtSpecItems = smtSpecItemMapper.selectByExample(example);
        if(null!=smtSpecItems&&smtSpecItems.size()>0){
            //return ConstantUtils.SYS_CODE_REPEAT;
        }

        smtSpecItem.setCreateUserId(currentUser.getUserId());
        smtSpecItem.setCreateTime(new Date());
        smtSpecItemMapper.insertSelective(smtSpecItem);

        //新增配置项历史信息
        SmtHtSpecItem smtHtSpecItem=new SmtHtSpecItem();
        BeanUtils.copyProperties(smtSpecItem,smtHtSpecItem);
        int i = smtHtSpecItemMapper.insertSelective(smtHtSpecItem);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtSpecItem smtSpecItem) {
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }
        int i = smtSpecItemMapper.updateByPrimaryKeySelective(smtSpecItem);

        //新增配置项历史信息
        //int i = insertHtSpecItem(smtSpecItem, currentUser);
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
        SysUser currentUser = null;
        try {
            currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(currentUser)){
            return ErrorCodeEnum.UAC10011039.getCode();
        }

        for (String specId : specIds) {
            SmtSpecItem smtSpecItem = smtSpecItemMapper.selectByPrimaryKey(specId);
            if(StringUtils.isEmpty(smtSpecItem)){
                throw new BizErrorException("该程序配置项已被删除。");
            }

            //新增角色历史信息
            //i= insertHtSpecItem(smtSpecItem, currentUser);
            SmtHtSpecItem smtHtSpecItem=new SmtHtSpecItem();
            BeanUtils.copyProperties(smtSpecItem,smtHtSpecItem);
            smtHtSpecItem.setModifiedUserId(currentUser.getUserId());
            smtHtSpecItem.setModifiedTime(new Date());
            list.add(smtHtSpecItem);

            smtSpecItemMapper.deleteByPrimaryKey(specId);

        }
        //i=smtHtSpecItemMapper.addBatchHtItem(list);
        i=smtHtSpecItemMapper.insertList(list);
        return i;
    }

    @Override
    public List<SmtSpecItemExcelDTO> exportSpecItems(SearchSmtSpecItem searchSmtSpecItem) {
        List<SmtSpecItem> smtSpecItems = this.selectSpecItems(searchSmtSpecItem);

        List<SmtSpecItemExcelDTO> dtoList=new ArrayList<>();
        for (SmtSpecItem smtSpecItem : smtSpecItems) {
            SmtSpecItemExcelDTO smtSpecItemExcelDTO =new SmtSpecItemExcelDTO();
            try {

                BeanUtils.copyProperties(smtSpecItem, smtSpecItemExcelDTO);

                dtoList.add(smtSpecItemExcelDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dtoList;
    }

    @Override
    public SmtSpecItem selectById(Long specId) {
        return smtSpecItemMapper.selectByPrimaryKey(specId);
    }
}
