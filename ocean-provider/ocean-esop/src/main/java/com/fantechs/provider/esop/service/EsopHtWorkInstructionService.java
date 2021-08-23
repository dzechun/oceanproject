package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopHtWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWorkInstruction;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EsopHtWorkInstructionService extends IService<EsopHtWorkInstruction> {
    List<EsopHtWorkInstructionDto> findHtList(Map<String,Object> map);
}
