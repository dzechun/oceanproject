package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWarehouseAreaMapper;
import com.fantechs.provider.base.mapper.BaseStorageMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseAreaMapper;
import com.fantechs.provider.base.service.BaseWarehouseAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class BaseWarehouseAreaServiceImpl extends BaseService<BaseWarehouseArea> implements BaseWarehouseAreaService {

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;
    @Resource
    private BaseHtWarehouseAreaMapper baseHtWarehouseAreaMapper;
    @Resource
    private BaseStorageMapper baseStorageMapper;


    @Override
    public List<BaseWarehouseAreaDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseWarehouseAreaMapper.findList(map);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWarehouseArea baseWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        Example example = new Example(BaseWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseAreaCode", baseWarehouseArea.getWarehouseAreaCode());
        List<BaseWarehouseArea> baseWarehouseAreaList = baseWarehouseAreaMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouseAreaList)){
             throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }
        baseWarehouseArea.setCreateUserId(currentUser.getUserId());
        baseWarehouseArea.setCreateTime(new Date());
        baseWarehouseArea.setModifiedUserId(currentUser.getUserId());
        baseWarehouseArea.setModifiedTime(new Date());
        baseWarehouseArea.setOrganizationId(currentUser.getOrganizationId());
        baseWarehouseAreaMapper.insertUseGeneratedKeys(baseWarehouseArea);

        //新增历史记录
        BaseHtWarehouseArea baseHtWarehouseArea =new BaseHtWarehouseArea();
        BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
        i= baseHtWarehouseAreaMapper.insertSelective(baseHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWarehouseArea baseWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseAreaCode", baseWarehouseArea.getWarehouseAreaCode())
                .andNotEqualTo("warehouseAreaId",baseWarehouseArea.getWarehouseAreaId());
        List<BaseWarehouseArea> baseWarehouseAreaList = baseWarehouseAreaMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouseAreaList)){
            throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }

        int i=0;
        baseWarehouseArea.setModifiedUserId(currentUser.getUserId());
        baseWarehouseArea.setModifiedTime(new Date());
        baseWarehouseArea.setOrganizationId(currentUser.getOrganizationId());
        i= baseWarehouseAreaMapper.updateByPrimaryKeySelective(baseWarehouseArea);

        //新增历史记录
        BaseHtWarehouseArea baseHtWarehouseArea =new BaseHtWarehouseArea();
        BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
        baseHtWarehouseAreaMapper.insertSelective(baseHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        List<BaseHtWarehouseArea> baseHtWarehouseAreaList =  new LinkedList<>();
        for(String id :idsArr){
            BaseWarehouseArea baseWarehouseArea = baseWarehouseAreaMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseWarehouseArea)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }

            //被储位引用
            Example example = new Example(BaseStorage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseAreaId", baseWarehouseArea.getWarehouseAreaId());
            List<BaseStorage> baseStorages = baseStorageMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseStorages)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtWarehouseArea baseHtWarehouseArea =  new BaseHtWarehouseArea();
            BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
            baseHtWarehouseArea.setModifiedTime(new Date());
            baseHtWarehouseArea.setCreateUserId(currentUser.getUserId());
            baseHtWarehouseAreaList.add(baseHtWarehouseArea);
        }
        i= baseWarehouseAreaMapper.deleteByIds(ids);
        baseHtWarehouseAreaMapper.insertList(baseHtWarehouseAreaList);
        return i;
    }
}
