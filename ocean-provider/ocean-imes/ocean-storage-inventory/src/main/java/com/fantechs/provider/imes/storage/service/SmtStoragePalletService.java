package com.fantechs.provider.imes.storage.service;

import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface SmtStoragePalletService extends IService<SmtStoragePallet> {

    List<SmtStoragePalletDto> findList(Map<String, Object> dynamicConditionByEntity);
}
