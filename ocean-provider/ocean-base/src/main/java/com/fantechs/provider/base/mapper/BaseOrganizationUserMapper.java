package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseOrganizationUserDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrganizationUserMapper extends MyMapper<BaseOrganizationUser> {

    List<BaseOrganizationUserDto> findList(Map<String, Object> map);
}