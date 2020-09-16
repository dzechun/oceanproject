package com.fantechs.security.mapper;


import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysMenuInfoMapper extends Mapper<SysMenuInfo> {
    List<SysMenuInfoDto> findList(Map<String, Object> param);
    SysMenuInfoDto selectById(Long id);
}