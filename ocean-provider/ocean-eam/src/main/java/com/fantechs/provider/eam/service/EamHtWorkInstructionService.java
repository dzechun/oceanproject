package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtWorkInstructionDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWorkInstruction;
import com.fantechs.common.base.general.entity.eam.history.EamHtWorkInstruction;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EamHtWorkInstructionService extends IService<EamHtWorkInstruction> {
    List<EamHtWorkInstructionDto> findHtList(Map<String,Object> map);
}
