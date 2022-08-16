package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface BaseOrganizationService extends IService<BaseOrganization> {

    List<BaseOrganizationDto> findList(Map<String, Object> map);

    int addUser(Long roleId, List<Long> userIds);

    Map<String, Object> importExcel(List<BaseOrganizationDto> baseOrganizationDtos);
}
