package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplierReUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSupplierReUserMapper;
import com.fantechs.provider.base.mapper.BaseSupplierReUserMapper;
import com.fantechs.provider.base.service.BaseSupplierReUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class BaseSupplierReUserServiceImpl extends BaseService<BaseSupplierReUser> implements BaseSupplierReUserService {

    @Resource
    private BaseSupplierReUserMapper baseSupplierReUserMapper;
    @Resource
    private BaseHtSupplierReUserMapper baseHtSupplierReUserMapper;

    @Override
    public List<BaseSupplierReUser> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return baseSupplierReUserMapper.findList(map);
    }

    @Override
    public List<BaseHtSupplierReUser> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return baseHtSupplierReUserMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(Long supplierId, List<Long> userIds) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSupplierReUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("supplierId",supplierId);
        baseSupplierReUserMapper.deleteByExample(example);

        List<BaseSupplierReUser> list = new ArrayList<>();
        for (Long userId : userIds) {
            BaseSupplierReUser supplierReUser = new BaseSupplierReUser();
            supplierReUser.setSupplierId(supplierId);
            supplierReUser.setUserId(userId);
            list.add(supplierReUser);
        }

        int i = 0;
        if (StringUtils.isNotEmpty(list)){
            i = baseSupplierReUserMapper.insertList(list);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseSupplierReUser record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSupplierReUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("supplierId",record.getSupplierId())
                .andEqualTo("userId",record.getUserId());
        BaseSupplierReUser baseSupplierReUser = baseSupplierReUserMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSupplierReUser)){
            throw new BizErrorException("该绑定关系已存在");
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrganizationId(user.getOrganizationId());
        baseSupplierReUserMapper.insertUseGeneratedKeys(record);

        //新增履历信息
        BaseHtSupplierReUser baseHtSupplierReUser = new BaseHtSupplierReUser();
        BeanUtils.copyProperties(record, baseHtSupplierReUser);
        int i = baseHtSupplierReUserMapper.insertSelective(baseHtSupplierReUser);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseSupplierReUser entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSupplierReUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("supplierId",entity.getSupplierId())
                .andEqualTo("userId",entity.getUserId())
                .andNotEqualTo("supplierReUserId",entity.getSupplierReUserId());
        BaseSupplierReUser baseSupplierReUser = baseSupplierReUserMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSupplierReUser)){
            throw new BizErrorException("该绑定关系已存在");
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        baseSupplierReUserMapper.updateByPrimaryKeySelective(entity);

        //新增履历信息
        BaseHtSupplierReUser baseHtSupplierReUser = new BaseHtSupplierReUser();
        BeanUtils.copyProperties(entity, baseHtSupplierReUser);
        int i = baseHtSupplierReUserMapper.insertSelective(baseHtSupplierReUser);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtSupplierReUser> htList = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id : split){
            BaseSupplierReUser baseSupplierReUser = baseSupplierReUserMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSupplierReUser)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增履历信息
            BaseHtSupplierReUser baseHtSupplierReUser = new BaseHtSupplierReUser();
            BeanUtils.copyProperties(baseSupplierReUser, baseHtSupplierReUser);
            htList.add(baseHtSupplierReUser);
        }

        baseHtSupplierReUserMapper.insertList(htList);

        return baseSupplierReUserMapper.deleteByIds(ids);
    }
}
