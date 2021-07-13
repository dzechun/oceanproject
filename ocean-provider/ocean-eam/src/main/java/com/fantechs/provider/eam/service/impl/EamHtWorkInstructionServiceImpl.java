package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamHtWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtWorkInstruction;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamHtWorkInstructionMapper;
import com.fantechs.provider.eam.service.EamHtWorkInstructionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EamHtWorkInstructionServiceImpl extends BaseService<EamHtWorkInstruction> implements EamHtWorkInstructionService {

    @Resource
    private EamHtWorkInstructionMapper eamHtWorkInstructionMapper;

    @Override
    public List<EamHtWorkInstructionDto> findHtList(Map<String, Object> map) {
        return eamHtWorkInstructionMapper.findHtList(map);
    }

}
