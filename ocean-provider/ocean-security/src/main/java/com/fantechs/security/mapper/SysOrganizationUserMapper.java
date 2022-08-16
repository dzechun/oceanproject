package com.fantechs.security.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysOrganizationUserMapper extends MyMapper<SysOrganizationUser> {

}