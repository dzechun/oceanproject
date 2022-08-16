package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseTeamImport;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/15.
 */

public interface BaseTeamService extends IService<BaseTeam> {
    List<BaseTeamDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseTeamImport> baseTeamImports);
}
