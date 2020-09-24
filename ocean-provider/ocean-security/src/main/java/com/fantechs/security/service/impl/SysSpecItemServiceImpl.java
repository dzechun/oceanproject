package com.fantechs.security.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysHtSpecItemMapper;
import com.fantechs.security.mapper.SysSpecItemMapper;
import com.fantechs.security.service.SysSpecItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysSpecItemServiceImpl extends BaseService<SysSpecItem> implements SysSpecItemService {
    @Resource
    private SysSpecItemMapper sysSpecItemMapper;

    @Resource
    private SysHtSpecItemMapper SysHtSpecItemMapper;

    @Override
    public List<SysSpecItem> selectSpecItems(SearchSysSpecItem searchSysSpecItem) {
        return sysSpecItemMapper.selectSpecItems(searchSysSpecItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysSpecItem SysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",SysSpecItem.getSpecCode());
        List<SysSpecItem> SysSpecItems = sysSpecItemMapper.selectByExample(example);
        if(null!=SysSpecItems&&SysSpecItems.size()>0){
            return ErrorCodeEnum.OPT20012001.getCode();
        }

        SysSpecItem.setCreateUserId(currentUser.getUserId());
        SysSpecItem.setCreateTime(new Date());
        sysSpecItemMapper.insertUseGeneratedKeys(SysSpecItem);

        //新增配置项历史信息
        SysHtSpecItem SysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(SysSpecItem,SysHtSpecItem);
        int i = SysHtSpecItemMapper.insertSelective(SysHtSpecItem);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SysSpecItem SysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",SysSpecItem.getSpecCode());
        SysSpecItem specItem = sysSpecItemMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(SysSpecItem)&&!specItem.getSpecId().equals(SysSpecItem.getSpecId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        SysSpecItem.setModifiedUserId(currentUser.getUserId());
        SysSpecItem.setModifiedTime(new Date());
        int i = sysSpecItemMapper.updateByPrimaryKeySelective(SysSpecItem);

        SysHtSpecItem SysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(SysSpecItem,SysHtSpecItem);
        SysHtSpecItem.setModifiedUserId(currentUser.getUserId());
        SysHtSpecItem.setModifiedTime(new Date());
        SysHtSpecItemMapper.insertSelective(SysHtSpecItem);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> specIds) {
        int i=0;
        List<SysHtSpecItem> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        for (String specId : specIds) {
            SysSpecItem SysSpecItem = sysSpecItemMapper.selectByPrimaryKey(specId);
            if(StringUtils.isEmpty(SysSpecItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SysHtSpecItem SysHtSpecItem=new SysHtSpecItem();
            BeanUtils.copyProperties(SysSpecItem,SysHtSpecItem);
            SysHtSpecItem.setModifiedUserId(currentUser.getUserId());
            SysHtSpecItem.setModifiedTime(new Date());
            list.add(SysHtSpecItem);
            sysSpecItemMapper.deleteByPrimaryKey(specId);

        }
        i=SysHtSpecItemMapper.insertList(list);
        return i;
    }

}
