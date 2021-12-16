package com.fantechs.provider.restapi.mulinsen.service;

import com.fantechs.common.base.general.dto.mulinsen.HrUserInfoDto;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;

import java.util.List;

public interface HrUserInfoService {
    List<HrUserInfoDto> findList(SearchHrUserInfo searchHrUserInfo);

    int synchronizeHrUserInfo() throws Exception;
}
