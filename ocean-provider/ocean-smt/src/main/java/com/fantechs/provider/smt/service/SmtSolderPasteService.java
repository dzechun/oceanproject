package com.fantechs.provider.smt.service;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/07/22.
 */

public interface SmtSolderPasteService extends IService<SmtSolderPaste> {
    List<SmtSolderPasteDto> findList(Map<String, Object> map);

    SmtSolderPasteDto scanSolder(String barCode,Byte solderPasteStatus,Integer PASS);

    List<SmtSolderPasteDto> findHtList(Map<String ,Object> map);
}
