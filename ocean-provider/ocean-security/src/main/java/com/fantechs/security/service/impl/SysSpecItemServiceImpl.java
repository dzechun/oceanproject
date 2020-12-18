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
import java.util.Map;

@Service
public class SysSpecItemServiceImpl extends BaseService<SysSpecItem> implements SysSpecItemService {
    @Resource
    private SysSpecItemMapper sysSpecItemMapper;

    @Resource
    private SysHtSpecItemMapper sysHtSpecItemMapper;

    @Override
    public List<SysSpecItem> findList(SearchSysSpecItem searchSysSpecItem) {
        return sysSpecItemMapper.findList(searchSysSpecItem);
    }

    @Override
    public List<String> findModule() {
        return sysSpecItemMapper.findModule();
    }

    @Override
    public int addModule(String moduleName) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUser)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }
        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("affiliationModule",moduleName)
                .andIsNull("category");
        List<SysSpecItem> sysSpecItems = sysSpecItemMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(sysSpecItems)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002);
        }
        SysSpecItem sysSpecItem = new SysSpecItem();
        sysSpecItem.setAffiliationModule(moduleName);
        int i = sysSpecItemMapper.insert(sysSpecItem);
        return i;
    }

    @Override
    public int updateModule(Map<String, Object> map) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUser)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }
        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("affiliationModule",map.get("affiliationModule")==null?"":map.get("affiliationModule"))
                .andNotEqualTo("specId",map.get("specId")==null?"":map.get("specId"));
        List<SysSpecItem> sysSpecItems = sysSpecItemMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(sysSpecItems)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002);
        }
        int i = sysSpecItemMapper.updateModule(map);
        return i;
    }

    @Override
    public int deleteModule(String ids) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String specId : idsArr) {
            List<SysSpecItem> list = sysSpecItemMapper.examineModule(specId);
            if (StringUtils.isNotEmpty(list)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }
        }
        return sysSpecItemMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysSpecItem sysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",sysSpecItem.getSpecCode());
        List<SysSpecItem> sysSpecItems = sysSpecItemMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(sysSpecItems)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysSpecItem.setCreateUserId(currentUser.getUserId());
        sysSpecItem.setCreateTime(new Date());
        sysSpecItem.setModifiedTime(new Date());
        sysSpecItem.setModifiedUserId(currentUser.getUserId());
        sysSpecItemMapper.insertUseGeneratedKeys(sysSpecItem);

        //新增配置项历史信息
        SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
        int i = sysHtSpecItemMapper.insertSelective(sysHtSpecItem);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysSpecItem sysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",sysSpecItem.getSpecCode());
        SysSpecItem specItem = sysSpecItemMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(specItem)&&!specItem.getSpecId().equals(sysSpecItem.getSpecId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysSpecItem.setModifiedUserId(currentUser.getUserId());
        sysSpecItem.setModifiedTime(new Date());
        int i = sysSpecItemMapper.updateByPrimaryKeySelective(sysSpecItem);

        SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
        sysHtSpecItemMapper.insertSelective(sysHtSpecItem);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String specIds) {
        List<SysHtSpecItem> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = specIds.split(",");
        for (String specId : idsArr) {
            SysSpecItem sysSpecItem = sysSpecItemMapper.selectByPrimaryKey(specId);
            if(StringUtils.isEmpty(sysSpecItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
            BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
            sysHtSpecItem.setModifiedUserId(currentUser.getUserId());
            sysHtSpecItem.setModifiedTime(new Date());
            list.add(sysHtSpecItem);

        }
        sysHtSpecItemMapper.insertList(list);
        return sysSpecItemMapper.deleteByIds(specIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<SysSpecItem> list){
        List<SysHtSpecItem> htList=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        for (SysSpecItem sysSpecItem : list) {
            SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
            BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
            sysHtSpecItem.setModifiedUserId(currentUser.getUserId());
            sysHtSpecItem.setModifiedTime(new Date());
            htList.add(sysHtSpecItem);
        }
        sysSpecItemMapper.insertList(list);
        return sysHtSpecItemMapper.insertList(htList);
    }

}
