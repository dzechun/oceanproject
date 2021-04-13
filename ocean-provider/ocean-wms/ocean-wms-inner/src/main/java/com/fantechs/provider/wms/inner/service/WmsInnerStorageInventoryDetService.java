package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/04.
 */

public interface WmsInnerStorageInventoryDetService extends IService<WmsInnerStorageInventoryDet> {

    List<WmsInnerStorageInventoryDetDto> findList(Map<String, Object> map);

}
