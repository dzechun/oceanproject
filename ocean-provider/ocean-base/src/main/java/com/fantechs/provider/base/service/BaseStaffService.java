package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseStaffDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseStaffImport;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/16.
 */

public interface BaseStaffService extends IService<BaseStaff> {
    List<BaseStaffDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseStaffImport> baseStaffImports);
}
