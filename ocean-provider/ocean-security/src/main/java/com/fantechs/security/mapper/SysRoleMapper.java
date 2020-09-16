package com.fantechs.security.mapper;

import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SysRoleMapper extends MyMapper<SysRole> {
    List<SysRole> findRolesByUserId(@Param(value = "userId") Long userId);
}
