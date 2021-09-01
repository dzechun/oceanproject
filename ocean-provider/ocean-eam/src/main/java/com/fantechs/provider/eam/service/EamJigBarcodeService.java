package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamJigBarcodeService extends IService<EamJigBarcode> {
    List<EamJigBarcodeDto> findList(Map<String, Object> map);

    //增加治具当前使用次数
    int plusCurrentUsageTime(Long jigBarcodeId, Integer num);

    int jigWarning();
}
