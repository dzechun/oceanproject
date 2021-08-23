package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.EsopWorkInstruction;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWorkInstruction;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */

public interface EsopWorkInstructionService extends IService<EsopWorkInstruction> {
    List<EsopWorkInstructionDto> findList(SearchEsopWorkInstruction searchEsopWorkInstruction);

    EsopWorkInstructionDto findByEquipmentIp(SearchEsopWorkInstruction searchEsopWorkInstruction);

    EsopWorkInstructionDto importExcel(MultipartFile file) throws IOException;

    int save(EsopWorkInstructionDto EsopWorkInstructionDto);

    int update(EsopWorkInstructionDto EsopWorkInstructionDto);

    String download(HttpServletResponse response) throws IOException;

    int censor(EsopWorkInstructionDto EsopWorkInstructionDto);
}
