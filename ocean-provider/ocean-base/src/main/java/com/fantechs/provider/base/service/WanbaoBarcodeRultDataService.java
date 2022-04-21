package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.WanbaoBarcodeRultDataDto;
import com.fantechs.common.base.general.dto.basic.imports.WanbaoBarcodeRultDataImportDto;
import com.fantechs.common.base.general.entity.basic.WanbaoBarcodeRultData;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2022/02/22.
 */

public interface WanbaoBarcodeRultDataService extends IService<WanbaoBarcodeRultData> {
    List<WanbaoBarcodeRultDataDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WanbaoBarcodeRultDataImportDto> list);

    /**
     * 同步物料时占用识别码
     * @param list
     * @return
     */
    int updateByMaterial(List<Long> list);
}
