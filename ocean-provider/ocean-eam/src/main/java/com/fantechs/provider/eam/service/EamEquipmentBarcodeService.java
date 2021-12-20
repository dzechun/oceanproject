package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentBarcodeService extends IService<EamEquipmentBarcode> {
    List<EamEquipmentBarcode> findList(Map<String, Object> map);

    //增加设备当前使用次数
    int plusCurrentUsageTime(Long equipmentBarCodeId, Integer num);

    int equipmentWarning();

    int updateEquipmentStatus();

    int updateEquipmentStatusByLongTime();
}
