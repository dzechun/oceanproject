package com.fantechs.provider.lizi.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.provider.lizi.entity.LiziScanBarcodeLog;
import com.fantechs.provider.lizi.entity.dto.LiziScanBarcodeLogDto;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/16.
 */

public interface LiziScanBarcodeLogService extends IService<LiziScanBarcodeLog> {
    List<LiziScanBarcodeLogDto> findList(Map<String, Object> map);

    LiziScanBarcodeLogDto add(String sn);
}
