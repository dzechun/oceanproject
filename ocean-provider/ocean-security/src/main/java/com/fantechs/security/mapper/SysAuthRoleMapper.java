package com.fantechs.security.mapper;


import com.fantechs.common.base.entity.security.SysAuthRole;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysAuthRoleMapper extends Mapper<SysAuthRole> {

    int delBatchByMenuIds(List<Long> menuIds);

}