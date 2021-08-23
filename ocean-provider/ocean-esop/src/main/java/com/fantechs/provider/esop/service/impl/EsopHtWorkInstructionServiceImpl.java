package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.general.dto.esop.EsopHtWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWorkInstruction;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.esop.mapper.EsopHtWorkInstructionMapper;
import com.fantechs.provider.esop.service.EsopHtWorkInstructionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@Service
public class EsopHtWorkInstructionServiceImpl extends BaseService<EsopHtWorkInstruction> implements EsopHtWorkInstructionService {

    @Resource
    private EsopHtWorkInstructionMapper esopHtWorkInstructionMapper;

    @Override
    public List<EsopHtWorkInstructionDto> findHtList(Map<String, Object> map) {
        return esopHtWorkInstructionMapper.findHtList(map);
    }

}
