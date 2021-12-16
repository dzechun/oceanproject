package com.fantechs.provider.restapi.mulinsen.mapper;

import com.fantechs.common.base.general.dto.mulinsen.HrUserInfoDto;
import com.fantechs.common.base.general.entity.mulinsen.HrUserInfo;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HrUserInfoMapper {
    List<HrUserInfoDto> findList(SearchHrUserInfo searchHrUserInfo);

    int batchUpdate(List<HrUserInfo> hrUserInfoList);
}