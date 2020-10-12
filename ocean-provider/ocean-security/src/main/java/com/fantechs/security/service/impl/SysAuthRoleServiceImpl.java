package com.fantechs.security.service.impl;

import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysAuthRoleMapper;
import com.fantechs.security.service.SysAuthRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lfz on 2020/10/12.
 */
@Service
public class SysAuthRoleServiceImpl extends BaseService<SysAuthRole> implements SysAuthRoleService {

    @Resource
    private SysAuthRoleMapper smtAuthRoleMapper;

    @Override
    @Transactional
    public int updateBatch(List<SysAuthRole> list) {
        //清除现有角色菜单权限
        Example example = new Example(SysAuthRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",list.get(0).getRoleId());
        smtAuthRoleMapper.deleteByExample(example);
        return smtAuthRoleMapper.updateBatch(list);
    }
}
