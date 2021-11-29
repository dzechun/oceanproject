package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.dto.jinan.Import.RfidAreaImport;
import com.fantechs.common.base.general.entity.jinan.RfidArea;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface RfidAreaService extends IService<RfidArea> {
    List<RfidArea> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<RfidAreaImport> rfidAreaImports);
}
