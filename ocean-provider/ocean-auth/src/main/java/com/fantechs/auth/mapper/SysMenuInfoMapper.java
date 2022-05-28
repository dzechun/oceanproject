package com.fantechs.auth.mapper;


import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysMenuInfoMapper extends MyMapper<SysMenuInfo> {
    List<SysMenuInfoDto> findList(Map<String, Object> param);

    SysMenuInfoDto selectById(Long id);

    int delBatchByIds(List<Long> menuIds);

    List<Long> selectMenuId(Byte menuType);

    List<SysMenuInfoDto> findByUsreId(@Param("roleIds") List<String> roleIds,@Param("menuType") Object menuType);
}
