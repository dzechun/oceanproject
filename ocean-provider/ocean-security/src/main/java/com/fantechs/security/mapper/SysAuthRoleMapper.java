package com.fantechs.security.mapper;


import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.mybatis.MyMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysAuthRoleMapper extends MyMapper<SysAuthRole> {

    int updateBatch(List<SysAuthRole> list);

    int delBatchByMenuIds(List<Long> menuIds);

}