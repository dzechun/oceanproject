package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/03.
 */

public interface BaseWarningPersonnelService extends IService<BaseWarningPersonnel> {
    List<BaseWarningPersonnelDto> findList(Map<String, Object> map);
}
