package com.fantechs.provider.smt.service;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteJobDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPasteJob;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/27.
 */

public interface SmtSolderPasteJobService extends IService<SmtSolderPasteJob> {
    List<SmtSolderPasteJobDto> findList(Map<String, Object> map);
}
