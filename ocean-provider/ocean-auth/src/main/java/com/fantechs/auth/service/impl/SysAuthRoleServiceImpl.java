package com.fantechs.auth.service.impl;

import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.mapper.SysAuthRoleMapper;
import com.fantechs.auth.mapper.SysMenuInfoMapper;
import com.fantechs.auth.service.SysAuthRoleService;
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
    @Resource
    private SysMenuInfoMapper sysMenuInfoMapper;

    @Override
    @Transactional
    public int updateBatch(List<SysAuthRole> list, Byte menuType) {
        List<Long> menuIds = sysMenuInfoMapper.selectMenuId(menuType);

        if (StringUtils.isNotEmpty(menuIds) && StringUtils.isNotEmpty(list)) {
            //清除现有角色菜单权限
            Example example = new Example(SysAuthRole.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("roleId", list.get(0).getRoleId());
            criteria.andIn("menuId", menuIds);
            smtAuthRoleMapper.deleteByExample(example);
            if (list.get(0).getMenuId() == 0){
                return 1;
            }
        }
        return smtAuthRoleMapper.updateBatch(list);
    }

    @Override
    public SysAuthRole getSysAuthRole(Long roleId,Long menuId) {
        Example example = new Example(SysAuthRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",roleId);
        criteria.andEqualTo("menuId",menuId);
        return smtAuthRoleMapper.selectOneByExample(example);
    }
}
