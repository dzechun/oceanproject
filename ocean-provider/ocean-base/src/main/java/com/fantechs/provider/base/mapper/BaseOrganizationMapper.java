package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrganizationMapper extends MyMapper<BaseOrganization> {

    List<BaseOrganizationDto> findList(Map<String, Object> map);

    int deleteUserByOrganization(Long organizationId);

    int insertUser(List<SysOrganizationUser> sysOrganizationUsers);

    List<BaseOrganizationDto> findOrganizationByUserId(Long userId);
}