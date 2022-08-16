package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseOrganizationUserDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface BaseOrganizationUserService extends IService<BaseOrganizationUser> {

    List<BaseOrganizationUserDto> findList(Map<String, Object> map);
}
