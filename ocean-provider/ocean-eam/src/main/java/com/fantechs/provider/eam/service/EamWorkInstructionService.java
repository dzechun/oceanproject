package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWorkInstruction;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EamWorkInstructionService extends IService<EamWorkInstruction> {
    List<EamWorkInstructionDto> findList(SearchEamWorkInstruction searchEamWorkInstruction);

    EamWorkInstructionDto importExcel(MultipartFile file) throws IOException;

    int save(EamWorkInstructionDto eamWorkInstructionDto);
}
